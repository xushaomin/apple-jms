package com.appleframework.jms.core.consumer;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;


/**
 * @author xusm
 * 
 */
public class ObjectMessageConusmer2 implements MessageListener {
	
	private IMessageConusmer<Object> messageConusmer;
	
	public void setMessageConusmer2(IMessageConusmer<Object> messageConusmer) {
		this.messageConusmer = messageConusmer;
	}

	public void onMessage(Message message) {
		try {
			Object object = ((ObjectMessage) message).getObject();
			messageConusmer.onMessage(object);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
	
}
