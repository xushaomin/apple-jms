package com.appleframework.jms.datacarrier.producer;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a.eye.datacarrier.DataCarrier;
import com.appleframework.jms.core.exception.JmsException;
import com.appleframework.jms.core.producer.MessageProducer;

/**
 * @author Cruise.Xu
 * 
 */
public class ObjectMessageProducer implements MessageProducer {
	
	private static Logger logger = LoggerFactory.getLogger(ObjectMessageProducer.class);

	private DataCarrier<Object> carrier;
	
	public void setCarrier(DataCarrier<Object> carrier) {
		this.carrier = carrier;
	}

	@Override
	public void sendByte(byte[] message) throws JmsException {
		try {
			carrier.produce(message);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	@Override
	public void sendObject(Serializable message) throws JmsException {
		try {
			carrier.produce(message);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	@Override
	public void sendText(String message) throws JmsException {
		try {
			carrier.produce(message);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

}