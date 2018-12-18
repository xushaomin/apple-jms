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
@SuppressWarnings("unchecked")
public class TMessageProducer implements MessageProducer {

	private static Logger logger = LoggerFactory.getLogger(TMessageProducer.class);

	@SuppressWarnings("rawtypes")
	private DataCarrier carrier;

	@SuppressWarnings("rawtypes")
	public void setCarrier(DataCarrier carrier) {
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