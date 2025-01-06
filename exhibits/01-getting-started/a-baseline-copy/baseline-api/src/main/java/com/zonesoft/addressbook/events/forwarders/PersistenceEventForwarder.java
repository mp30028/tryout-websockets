package com.zonesoft.addressbook.events.forwarders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import com.zonesoft.addressbook.events.PersistenceEvent;
import com.zonesoft.addressbook.events.PersistenceEventData;

import reactor.core.publisher.FluxSink;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

@Component
public class PersistenceEventForwarder implements ApplicationListener<PersistenceEvent>, Consumer<FluxSink<PersistenceEvent>> {

	private final Executor executor;
	private final BlockingQueue<PersistenceEvent> queue = new LinkedBlockingQueue<>();
	private static final Logger LOGGER = LoggerFactory.getLogger(PersistenceEventForwarder.class);

	@Autowired
	public PersistenceEventForwarder(Executor executor) {
		this.executor = executor;
	}

	@Override
	public void onApplicationEvent(PersistenceEvent event) {
		PersistenceEventData source = ((PersistenceEventData) event.getSource());
		LOGGER.info("[PERSISTENCE-{}-TRIGGERED] person: {}", source.getEventType().toString().toUpperCase(), source.getPerson().toString());
		this.queue.offer(event);
	}

	@Override
	public void accept(FluxSink<PersistenceEvent> sink) {
		LOGGER.debug("From accept");
		this.executor.execute(() -> {
			while (true)
				try {
					PersistenceEvent event = queue.take();
					sink.next(event);
				} catch (InterruptedException e) {
					ReflectionUtils.rethrowRuntimeException(e);
				}
		});
	}


}
