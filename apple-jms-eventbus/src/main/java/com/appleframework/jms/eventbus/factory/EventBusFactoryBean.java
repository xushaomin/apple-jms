package com.appleframework.jms.eventbus.factory;

import org.springframework.beans.factory.FactoryBean;

import com.google.common.eventbus.EventBus;

public class EventBusFactoryBean implements FactoryBean<EventBus> {

	private Object consumer;
	private String name = "event-bus";

	public void setConsumer(Object consumer) {
		this.consumer = consumer;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public EventBus getObject() throws Exception {
		EventBus eventBus = new EventBus(name);
		eventBus.register(consumer);
		return eventBus;
	}

	@Override
	public Class<?> getObjectType() {
		return EventBus.class;
	}

	@Override
	public boolean isSingleton() {
		return false;
	}

}
