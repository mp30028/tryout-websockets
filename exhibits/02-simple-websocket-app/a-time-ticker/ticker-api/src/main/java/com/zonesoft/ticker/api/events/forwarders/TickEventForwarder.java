package com.zonesoft.ticker.api.events.forwarders;

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

import com.zonesoft.ticker.api.events.TickEvent;

import reactor.core.publisher.FluxSink;

@Component
public class TickEventForwarder implements ApplicationListener<TickEvent>, Consumer<FluxSink<TickEvent>>{
	private static final Logger LOGGER = LoggerFactory.getLogger(TickEventForwarder.class);
    private final BlockingQueue<TickEvent> queue = new LinkedBlockingQueue<>();	
    private final Executor executor;
    
    @Autowired
	public TickEventForwarder(Executor executor) {
		this.executor = executor;
	}
    
    
	@Override
	public void onApplicationEvent(TickEvent event) {
		LOGGER.debug("Event recieved by onApplicationEvent Listener. event={}", event);
		this.queue.offer(event);		
	}

	@Override
	public void accept(FluxSink<TickEvent> sink) {
		LOGGER.debug("From accept");
		this.executor.execute(() -> {
			while (true)
				try {
					TickEvent event = queue.take();
					sink.next(event);
				} catch (InterruptedException e) {
					ReflectionUtils.rethrowRuntimeException(e);
				}
			});
		
	}

}
