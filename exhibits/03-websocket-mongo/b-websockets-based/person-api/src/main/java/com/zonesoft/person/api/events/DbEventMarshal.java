package com.zonesoft.person.api.events;

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

import reactor.core.publisher.FluxSink;

@Component
public class DbEventMarshal<T> implements ApplicationListener<DbEvent<T>>, Consumer<FluxSink<DbEvent<T>>>{
	private static final Logger LOGGER = LoggerFactory.getLogger(DbEventMarshal.class);
    private final BlockingQueue<DbEvent<T>> queue = new LinkedBlockingQueue<>();	
    private final Executor executor;
    
    @Autowired
	public DbEventMarshal(Executor executor) {
		this.executor = executor;
	}
    
    
	@Override
	public void onApplicationEvent(DbEvent<T> event) {
		LOGGER.debug("Event recieved by onApplicationEvent Listener. event={}", event);
		this.queue.offer(event);		
	}

	@Override
	public void accept(FluxSink<DbEvent<T>> sink) {
		LOGGER.debug("From accept");
		this.executor.execute(() -> {
			while (true)
				try {
					DbEvent<T> event = queue.take();
					sink.next(event);
				} catch (InterruptedException e) {
					ReflectionUtils.rethrowRuntimeException(e);
				}
			});
		
	}

}
