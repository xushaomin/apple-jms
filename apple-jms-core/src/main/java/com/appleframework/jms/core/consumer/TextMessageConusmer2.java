package com.appleframework.jms.core.consumer;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;


/**
 * @author xusm
 * 
 */
public class TextMessageConusmer2 implements MessageListener {
	
	private IMessageConusmer<String> messageConusmer;
	
	public void setMessageConusmer(IMessageConusmer<String> messageConusmer) {
		this.messageConusmer = messageConusmer;
	}

	public void onMessage(Message message) {
		try {
			String object = ((TextMessage) message).getText();
			messageConusmer.onMessage(object);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
	
}
