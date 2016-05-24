package com.appleframework.jms.core.producer;

import java.io.Serializable;

import com.appleframework.jms.core.exception.JmsException;

/**
 * 队列消息发送接口
 * @author cruise.xu
 *
 */
public interface MessageProducer2 {
	
	/**
	 * 发送对象信息
	 * @param code
	 */
	public void sendObject(String topic, Serializable message) throws JmsException;
	
	/**
	 * 发送字符串
	 * @param code
	 */
	public void sendText(String topic, String message) throws JmsException;
	
	/**
	 * 发送比特流
	 * @param code
	 */
	public void sendByte(String topic, byte[] message) throws JmsException;
	
}