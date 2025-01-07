package com.zonesoft.sayings.api.events.forwarders;

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

import com.zonesoft.sayings.api.events.TriggerEvent;

import reactor.core.publisher.FluxSink;

@Component
public class TriggerEventForwarder implements ApplicationListener<TriggerEvent>, Consumer<FluxSink<TriggerEvent>>{
	private static final Logger LOGGER = LoggerFactory.getLogger(TriggerEventForwarder.class);
    private final BlockingQueue<TriggerEvent> queue = new LinkedBlockingQueue<>();	
    private final Executor executor;
    
    @Autowired
	public TriggerEventForwarder(Executor executor) {
		this.executor = executor;
	}
    
    
	@Override
	public void onApplicationEvent(TriggerEvent event) {
		LOGGER.debug("Event recieved by onApplicationEvent Listener. event={}", event);
		this.queue.offer(event);		
	}

	@Override
	public void accept(FluxSink<TriggerEvent> sink) {
		LOGGER.debug("From accept");
		this.executor.execute(() -> {
			while (true)
				try {
					TriggerEvent event = queue.take();
					sink.next(event);
				} catch (InterruptedException e) {
					ReflectionUtils.rethrowRuntimeException(e);
				}
			});
		
	}

}
