package com.appleframework.jms.ons.consumer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aliyun.openservices.shade.com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.client.exception.MQClientException;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.common.consumer.ConsumeFromWhere;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.common.message.MessageExt;
import com.appleframework.jms.core.consumer.AbstractMessageConusmer;
import com.appleframework.jms.ons.RocketMQPushConsumer;


/**
 * @author Cruise.Xu
 * 
 */
public abstract class BytesMessageConsumer extends AbstractMessageConusmer<byte[]> {
	
	private final static Logger logger = LoggerFactory.getLogger(BytesMessageConsumer.class);
	
	private RocketMQPushConsumer consumer;
	
	private String topic;
	
	private String tags;
		
	public void setConsumer(RocketMQPushConsumer consumer) {
		this.consumer = consumer;
	}
		
	public void setTopic(String topic) {
		this.topic = topic;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	protected void init() throws MQClientException {
        consumer.subscribe(topic, tags);
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);  
        consumer.registerMessageListener(new MessageListenerConcurrently() {
                public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext context) {
                	MessageExt msg = list.get(0);
                    logger.info(msg.toString());
                    byte[] message = msg.getBody();
                    processMessage(message);
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
            }
        );
        consumer.start();
	}
}
