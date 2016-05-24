package com.appleframework.jms.core.consumer;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;


/**
 * @author xusm
 * 
 */
public abstract class ObjectMessageConusmer extends MessageConusmer<Object> implements MessageListener {
	
	public void onMessage(Message message) {
		try {
			Object object = ((ObjectMessage) message).getObject();
			processMessage(object);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}
