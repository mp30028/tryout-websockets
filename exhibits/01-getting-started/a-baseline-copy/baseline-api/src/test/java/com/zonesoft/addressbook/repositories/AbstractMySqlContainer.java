package com.zonesoft.addressbook.repositories;

import org.testcontainers.containers.MySQLContainer;

@SuppressWarnings("rawtypes")
abstract class AbstractMySqlContainer {
	private static final String IMAGE_NAME = "mysql:8.0.19";

    static final MySQLContainer MYSQL_CONTAINER;

    static {
    	MYSQL_CONTAINER = new MySQLContainer(IMAGE_NAME)
    			.withDatabaseName("addressbook");
        MYSQL_CONTAINER.start();
    }
}
