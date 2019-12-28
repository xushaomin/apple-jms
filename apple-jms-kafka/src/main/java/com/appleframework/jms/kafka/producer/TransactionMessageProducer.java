package com.appleframework.jms.kafka.producer;

import java.io.Serializable;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.appleframework.jms.core.exception.JmsException;
import com.appleframework.jms.core.exception.MQException;
import com.appleframework.jms.core.producer.MessageProducer;
import com.appleframework.jms.core.utils.ByteUtils;
import com.appleframework.jms.core.utils.TraceUtils;


/**
 * @author Cruise.Xu
 * 
 */
public class TransactionMessageProducer implements MessageProducer {

	private Producer<String, byte[]> producer;
	
	private String topic;
		
	public void setProducer(Producer<String, byte[]> producer) {
		this.producer = producer;
	}
	 
	public void setTopic(String topic) {
		this.topic = topic;
	}
	
	public void init() {
		 producer.initTransactions();
	}

	public void sendByte(byte[] message) throws JmsException {
		try {
			producer.beginTransaction();
			ProducerRecord<String, byte[]> producerData = new ProducerRecord<String, byte[]>(topic, 
					TraceUtils.getTraceId(), message);
			producer.send(producerData);
			producer.commitTransaction();
		} catch (Exception e) {
			throw new MQException(e);
		}
		
	}

	@Override
	public void sendObject(Serializable message) throws JmsException {
		try {
			producer.beginTransaction();
			ProducerRecord<String, byte[]> producerData = new ProducerRecord<String, byte[]>(topic, 
					TraceUtils.getTraceId(), ByteUtils.toBytes(message));
			producer.send(producerData);
			producer.commitTransaction();
		} catch (Exception e) {
			throw new MQException(e);
		}
	}

	@Override
	public void sendText(String message) throws JmsException {
		try {
			producer.beginTransaction();
			ProducerRecord<String, byte[]> producerData = new ProducerRecord<String, byte[]>(topic, 
					TraceUtils.getTraceId(), message.getBytes());
			producer.send(producerData);
			producer.commitTransaction();
		} catch (Exception e) {
			throw new MQException(e);
		}
	}

	public void destory() {
		try {
			producer.close();
		} catch (Exception e) {
		}
	}
}