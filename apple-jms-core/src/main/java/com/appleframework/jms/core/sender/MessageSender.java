package com.appleframework.jms.core.sender;

import java.io.Serializable;

import com.appleframework.jms.core.exception.JmsException;

/**
 * 队列消息发送接口
 * @author cruise.xu
 *
 */
public interface MessageSender {
	
	/**
	 * 发送对象信息
	 * @param code
	 */
	public String send(String topic, Serializable message, String trackId) throws JmsException;
	
}