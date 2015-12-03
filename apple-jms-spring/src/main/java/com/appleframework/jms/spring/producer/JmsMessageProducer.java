package com.appleframework.jms.spring.producer;

import java.io.Serializable;

import javax.jms.Destination;

import org.springframework.jms.core.JmsTemplate;

import com.appleframework.jms.spring.creator.JmsByteMessageCreator;
import com.appleframework.jms.spring.creator.JmsObjectMessageCreator;
import com.appleframework.jms.spring.creator.JmsTextMessageCreator;
import com.appleframework.jms.core.exception.JmsException;
import com.appleframework.jms.core.exception.MQException;
import com.appleframework.jms.core.producer.MessageProducer;

/**
 * @author xusm
 * 
 */
public class JmsMessageProducer implements MessageProducer {

	private JmsTemplate jmsTemplate;
	private Destination destination;

	public void sendObject(Serializable message) throws JmsException {
		try {
			this.jmsTemplate.send(destination, new JmsObjectMessageCreator(message));
		} catch (Exception e) {
			throw new MQException(e);
		}
	}
	
	public void sendByte(byte[] message) throws JmsException {
		try {
			this.jmsTemplate.send(destination, new JmsByteMessageCreator(message));
		} catch (Exception e) {
			throw new MQException(e);
		}
	}
	
	public void sendText(String message) throws JmsException {
		try {
			this.jmsTemplate.send(destination, new JmsTextMessageCreator(message));
		} catch (Exception e) {
			throw new MQException(e);
		}
	}

	public JmsTemplate getJmsTemplate() {
		return jmsTemplate;
	}

	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

	public Destination getDestination() {
		return destination;
	}

	public void setDestination(Destination destination) {
		this.destination = destination;
	}

}
