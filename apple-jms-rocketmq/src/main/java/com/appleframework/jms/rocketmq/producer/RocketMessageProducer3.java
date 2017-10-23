package com.appleframework.jms.rocketmq.producer;

import java.io.Serializable;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appleframework.jms.core.exception.MQException;
import com.appleframework.jms.core.producer.MessageProducer3;
import com.appleframework.jms.core.utils.ByteUtils;
import com.appleframework.jms.rocketmq.RocketMQProducer;

/**
 * @author Cruise.Xu
 * 
 */
public class RocketMessageProducer3 implements MessageProducer3 {
	
	private final static Logger logger = LoggerFactory.getLogger(RocketMessageProducer3.class);

	private RocketMQProducer producer;
	private String tag;
	
	public void sendByte(String topic, String keys, byte[] message) throws MQException {
        Message msg = new Message(topic, tag, keys, message);
        try {
        	SendResult result = producer.send(msg);
        	logger.info("msgId=" + result.getMsgId());
		} catch (MQClientException | RemotingException | MQBrokerException
				| InterruptedException e) {
			throw new MQException(e);
		}
	}

	public void sendObject(String topic, String keys, Serializable message) throws MQException {		
		Message msg = new Message(topic, tag, ByteUtils.toBytes(message));
		try {
			SendResult result = producer.send(msg);
			logger.info("msgId=" + result.getMsgId());
		} catch (MQClientException | RemotingException | MQBrokerException
				| InterruptedException e) {
			throw new MQException(e);
		}
	}

	public void sendText(String topic, String keys, String message) throws MQException {		
		Message msg = new Message(topic, tag, ByteUtils.toBytes(message));
		try {
			SendResult result = producer.send(msg);
			logger.info("msgId=" + result.getMsgId());
		} catch (MQClientException | RemotingException | MQBrokerException
				| InterruptedException e) {
			throw new MQException(e);
		}
	}

	public void setTag(String tag) {
		this.tag = tag;
	}
	
	public void destory() {
		try {
			producer.shutdown();
		} catch (Exception e) {
		}
	}

}
