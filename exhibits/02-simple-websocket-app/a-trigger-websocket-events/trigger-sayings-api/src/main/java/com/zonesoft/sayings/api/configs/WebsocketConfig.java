package com.zonesoft.sayings.api.configs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;

import com.zonesoft.sayings.api.entities.MessageEventContent;
import com.zonesoft.sayings.api.event_marshallers.MessageEventMarshall;
import com.zonesoft.sayings.api.events.MessageEvent;

//import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
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
    public WebSocketHandler webSocketHandler(MessageEventMarshall eventPublisher) {
    	LOGGER.debug("-----------  WebSocketHandler Bean invoked ----------------- ");
        Flux<MessageEvent> publish = Flux.create(eventPublisher).share();
        return session -> {		
            Flux<WebSocketMessage> messageFlux = publish.map(evt -> {
                    MessageEventContent source = (MessageEventContent) evt.getSource();
                    return source.toString();
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
