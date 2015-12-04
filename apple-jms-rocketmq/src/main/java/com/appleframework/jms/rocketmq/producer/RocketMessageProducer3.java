package com.appleframework.jms.rocketmq.producer;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.remoting.exception.RemotingException;
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

	public void setProducer(RocketMQProducer producer) {
		this.producer = producer;
	}

	public void sendByte(String topic, String keys, byte[] message) throws MQException {
        Message msg = new Message(topic, null, keys, message);
        try {
        	SendResult result = producer.send(msg);
        	logger.info("msgId=" + result.getMsgId());
		} catch (MQClientException | RemotingException | MQBrokerException
				| InterruptedException e) {
			throw new MQException(e);
		}
	}

	public void sendObject(String topic, String keys, Serializable message) throws MQException {		
		Message msg = new Message(topic, null, ByteUtils.toBytes(message));
		try {
			SendResult result = producer.send(msg);
			logger.info("msgId=" + result.getMsgId());
		} catch (MQClientException | RemotingException | MQBrokerException
				| InterruptedException e) {
			throw new MQException(e);
		}
	}

	public void sendText(String topic, String keys, String message) throws MQException {		
		Message msg = new Message(topic, null, ByteUtils.toBytes(message));
		try {
			SendResult result = producer.send(msg);
			logger.info("msgId=" + result.getMsgId());
		} catch (MQClientException | RemotingException | MQBrokerException
				| InterruptedException e) {
			throw new MQException(e);
		}
	}	

}
