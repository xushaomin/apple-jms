package com.appleframework.jms.activemq.consumer;

import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.activemq.command.ActiveMQBytesMessage;

import com.appleframework.jms.core.consumer.AbstractMessageConusmer;

/**
 * @author Cruise.Xu
 * 
 */
public abstract class BaseMessageConsumer extends AbstractMessageConusmer<byte[]> implements MessageListener {

	@Override
	public void onMessage(Message message) {
		ActiveMQBytesMessage msg = (ActiveMQBytesMessage) message;
		byte[] data = msg.getContent().getData();
		processMessage(data);
	}		
	
}
