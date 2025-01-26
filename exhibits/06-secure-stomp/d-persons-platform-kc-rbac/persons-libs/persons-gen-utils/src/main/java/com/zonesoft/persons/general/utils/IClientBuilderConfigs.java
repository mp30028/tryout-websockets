package com.zonesoft.persons.general.utils;

public interface IClientBuilderConfigs {

	String getProtocol();

	void setProtocol(String protocol);

	String getDomain();

	void setDomain(String domain);

	String getPort();

	void setPort(String port);

	String getPath();

	void setPath(String path);

	String getClientName();

	void setClientName(String clientName);

	String getClientType();

	void setClientType(String clientType);

}