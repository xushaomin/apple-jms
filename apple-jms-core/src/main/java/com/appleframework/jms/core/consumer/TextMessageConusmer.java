package com.appleframework.jms.core.consumer;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;


/**
 * @author xusm
 * 
 */
public abstract class TextMessageConusmer extends MessageConusmer<String> implements MessageListener {

	public void onMessage(Message message) {
		try {
			String object = ((TextMessage) message).getText();
			processMessage(object);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
	
}
