package com.zonesoft.persons.general.utils;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DoNothing {
	private static final Logger LOGGER = LoggerFactory.getLogger(DoNothing.class);
	private static final int MINIMUM_WAIT_MS =30;
	private static final int MAXIMUM_WAIT_MS = 2100;
	
	public static void pretendDoingSomething() {
        try {
        	int waitMilliseconds = new Random().nextInt(MAXIMUM_WAIT_MS - MINIMUM_WAIT_MS + 1) + MINIMUM_WAIT_MS;
        	LOGGER.debug("Starting waitMilliseconds={}", waitMilliseconds);
            Thread.sleep(waitMilliseconds);
            LOGGER.debug("Finished waitMilliseconds={}", waitMilliseconds);
        } catch (InterruptedException e) {
            System.err.format("IOException: %s%n", e);
        }
	}
	
}
