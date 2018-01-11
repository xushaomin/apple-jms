package com.appleframework.jms.spring.creator;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.jms.core.MessageCreator;

public class JmsByteMessageCreator implements MessageCreator {
	
	private byte[] message;

	public JmsByteMessageCreator(byte[] message) {
		this.message = message;
	}
	
	@Override
	public Message createMessage(Session session) throws JMSException {
		BytesMessage bytesMessage = session.createBytesMessage();
		try {
			bytesMessage.writeBytes(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bytesMessage;
	}
}
