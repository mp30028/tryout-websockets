package com.zonesoft.sayings.api.event_marshallers;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import com.zonesoft.sayings.api.events.MessageEvent;

import reactor.core.publisher.FluxSink;

@Component
public class MessageEventMarshall implements ApplicationListener<MessageEvent>, Consumer<FluxSink<MessageEvent>>{
	private static final Logger LOGGER = LoggerFactory.getLogger(MessageEventMarshall.class);
    private final BlockingQueue<MessageEvent> queue = new LinkedBlockingQueue<>();	
    private final Executor executor;
    
    @Autowired
	public MessageEventMarshall(Executor executor) {
		this.executor = executor;
	}
    
    
	@Override
	public void onApplicationEvent(MessageEvent event) {
		LOGGER.debug("Event recieved by onApplicationEvent Listener. event={}", event);
		this.queue.offer(event);		
	}

	@Override
	public void accept(FluxSink<MessageEvent> sink) {
		LOGGER.debug("From accept");
		this.executor.execute(() -> {
			while (true)
				try {
					MessageEvent event = queue.take();
					sink.next(event);
				} catch (InterruptedException e) {
					ReflectionUtils.rethrowRuntimeException(e);
				}
			});
		
	}

}
