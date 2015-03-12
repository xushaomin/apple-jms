package com.appleframework.jms.kafka.producer;

import java.io.Serializable;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;

import org.springframework.jms.JmsException;

import com.appleframework.jms.core.producer.MessageProducer;
import com.appleframework.jms.core.utils.ByteUtils;
import com.appleframework.jms.kafka.utils.RandomUtility;

/**
 * @author Cruise.Xu
 * 
 */
public class KafkaMessageProducer2 implements MessageProducer {

	private Producer<String, byte[]> producer;
	private String topic;
	private Integer partitionsNum; 

	public void setProducer(Producer<String, byte[]> producer) {
		this.producer = producer;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}
	
	public void setPartitionsNum(Integer partitionsNum) {
		this.partitionsNum = partitionsNum;
	}
	
	private String getRandomNumValue() {
		int randomNum = RandomUtility.main(0, partitionsNum - 1);
		return String.valueOf(randomNum);
	}

	public void sendByte(byte[] message) throws JmsException {
		String partition = getRandomNumValue();
		KeyedMessage<String, byte[]> producerData 
			= new KeyedMessage<String, byte[]>(topic, partition, message);
		producer.send(producerData);
	}

	@Override
	public void sendObject(Serializable message) throws JmsException {
		String partition = getRandomNumValue();
		KeyedMessage<String, byte[]> producerData 
			= new KeyedMessage<String, byte[]>(topic, partition, ByteUtils.toBytes(message));
		producer.send(producerData);
	}

	@Override
	public void sendText(String message) throws JmsException {
		String partition = getRandomNumValue();
		KeyedMessage<String, byte[]> producerData 
			= new KeyedMessage<String, byte[]>(topic, partition, ByteUtils.toBytes(message));
		producer.send(producerData);
	}	

}
