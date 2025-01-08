package com.zonesoft.person.api.configs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
//import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zonesoft.person.api.events.DbEvent;
import com.zonesoft.person.api.events.DbEventContent;
import com.zonesoft.person.api.events.DbEventMarshal;

import reactor.core.publisher.Flux;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
public class WebsocketConfig {
	private static final Logger LOGGER = LoggerFactory.getLogger(WebsocketConfig.class);
	
    @Bean
    public Executor executor() {
        return Executors.newSingleThreadExecutor();
    }

    @Bean
    public HandlerMapping handlerMapping(WebSocketHandler wsh) {
    	LOGGER.debug("-----------  HandlerMapping invoked ----------------- ");
        return new SimpleUrlHandlerMapping() {
            {
            	Map<String, WebSocketHandler> urlMappings = Collections.singletonMap("/websockets/events", wsh);
            	for(String key: urlMappings.keySet()) {
            		LOGGER.debug("key = {}, value = [CLASS:]{}", key, urlMappings.get(key).getClass().getName());
            	}
                setUrlMap(urlMappings);
                setOrder(10);
            }
        };
    }

    @Bean
    public <T> WebSocketHandler webSocketHandler(DbEventMarshal<T> eventPublisher) {
    	LOGGER.debug("-----------  WebSocketHandler Bean invoked ----------------- ");
        Flux<DbEvent<T>> publish = Flux.create(eventPublisher).share();
        return session -> {	
        	//Guess this is the place to check the session entitlements and data visibility
            Flux<WebSocketMessage> messageFlux = publish.map(evt -> {
					@SuppressWarnings("unchecked")
					DbEventContent<T> source = (DbEventContent<T>) evt.getSource();
					ObjectMapper mapper = new ObjectMapper();
					try {
						String sourceJson = mapper.writeValueAsString(source);
						return sourceJson;
					} catch (JsonProcessingException e) {						
						e.printStackTrace();
						return null;
					}
            }).map(message -> {
            	LOGGER.info("----  WebSocketHandler about to send following message ---- \n{}\n", message);
                return session.textMessage(message);
            });
            return session.send(messageFlux);
        };
    }

//  @Bean
//  public WebSocketHandlerAdapter webSocketHandlerAdapter() {
//      return new WebSocketHandlerAdapter();
//  }
    
}
