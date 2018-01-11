package com.appleframework.jms.rabbitmq.factory;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.FactoryBean;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class ConnectionFactoryBean implements FactoryBean<Connection> {

	private final static Logger logger = Logger.getLogger(ConnectionFactoryBean.class);
	
	private Connection connection;
	
	private String host = "locahost";
	private Integer port = 5672;
	private String username = "admin";
	private String password = "admin";
	private String virtualHost = "/";

	@Override
	public Connection getObject() throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(host);
		factory.setPort(port);
		factory.setUsername(username);
		factory.setPassword(password);
		factory.setVirtualHost(virtualHost);
		connection = factory.newConnection();
		return connection;
	}

	@Override
	public Class<Connection> getObjectType() {
		return Connection.class;
	}

	@Override
	public boolean isSingleton() {
		return false;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getVirtualHost() {
		return virtualHost;
	}

	public void setVirtualHost(String virtualHost) {
		this.virtualHost = virtualHost;
	}
	
	public void destory() {
		if (null != connection) {
			try {
				connection.close();
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}
	}

}
