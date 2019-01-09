package com.appleframework.jms.rabbitmq.factory;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class ChannelFactoryBean implements FactoryBean<Channel>, DisposableBean {

	private final static Logger logger = LoggerFactory.getLogger(ChannelFactoryBean.class);

	private Connection connection;
	private Channel channel;

	private String queue;
	private Boolean durable = true;
	private Boolean exclusive = false;
	private Boolean autoDelete = false;
	private Map<String, Object> arguments = null;
	
	private Integer prefetchCount;

	@Override
	public Channel getObject() throws Exception {
		channel = connection.createChannel();
		channel.queueDeclare(queue, durable, exclusive, autoDelete, arguments);
		if(null != prefetchCount && prefetchCount > 0) {
			channel.basicQos(prefetchCount);
		}
		return channel;
	}

	@Override
	public Class<Channel> getObjectType() {
		return Channel.class;
	}

	@Override
	public boolean isSingleton() {
		return false;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public String getQueue() {
		return queue;
	}

	public void setQueue(String queue) {
		this.queue = queue;
	}

	public Boolean getDurable() {
		return durable;
	}

	public void setDurable(Boolean durable) {
		this.durable = durable;
	}

	public Boolean getExclusive() {
		return exclusive;
	}

	public void setExclusive(Boolean exclusive) {
		this.exclusive = exclusive;
	}

	public Boolean getAutoDelete() {
		return autoDelete;
	}

	public void setAutoDelete(Boolean autoDelete) {
		this.autoDelete = autoDelete;
	}

	public Map<String, Object> getArguments() {
		return arguments;
	}

	public void setArguments(Map<String, Object> arguments) {
		this.arguments = arguments;
	}

	public Connection getConnection() {
		return connection;
	}
	
	public void setPrefetchCount(Integer prefetchCount) {
		this.prefetchCount = prefetchCount;
	}

	public void destroy() {
		if (null != channel) {
			try {
				channel.close();
			} catch (IOException | TimeoutException e) {
				logger.error(e.getMessage());
			}
		}
	}

}
