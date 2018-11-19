package com.appleframework.jms.rabbitmq.factory;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.FactoryBean;


public class ArgumentsFactoryBean implements FactoryBean<Map<String, Object>> {
	
	private static Map<String, Class<?>> argClazz = new HashMap<String, Class<?>>();
	
	static {
		argClazz.put("x-message-ttl", Long.class);
		argClazz.put("x-expires", Long.class);
		argClazz.put("x-max-length", Long.class);
		argClazz.put("x-max-length-bytes", Long.class);
		argClazz.put("x-dead-letter-exchange", String.class);
		argClazz.put("x-dead-letter-routing-key", String.class);
		argClazz.put("x-max-priority", Long.class);
		argClazz.put("x-queue-mode", String.class);
		argClazz.put("x-queue-master-locator", String.class);
	}
	
	private Map<String, String> arguments;
	
	@Override
	public Map<String, Object> getObject() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		for (String key : arguments.keySet()) { 
			String value = arguments.get(key); 
			Class<?> clz = argClazz.get(key);
			if(null != clz) {
				if(clz.isAssignableFrom(Long.class)) {
					map.put(key, Long.parseLong(value));
				}
				else {
					map.put(key, value);
				}
			}
		}
		return map;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Class<Map> getObjectType() {
		return Map.class;
	}

	@Override
	public boolean isSingleton() {
		return false;
	}

	public void setArguments(Map<String, String> arguments) {
		this.arguments = arguments;
	}

}
