package com.appleframework.jms.ons.consumer;

import java.util.List;

import org.apache.log4j.Logger;

import com.aliyun.openservices.shade.com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.client.exception.MQClientException;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.common.consumer.ConsumeFromWhere;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.common.message.MessageExt;
import com.appleframework.jms.core.consumer.MessageConusmer;
import com.appleframework.jms.core.utils.ByteUtils;
import com.appleframework.jms.ons.RocketMQPushConsumer;

/**
 * @author Cruise.Xu
 * 
 */
public abstract class ObjectMessageConsumer extends MessageConusmer<Object> {
	
	private final static Logger logger = Logger.getLogger(ObjectMessageConsumer.class);
	
	private RocketMQPushConsumer consumer;
	
	private String topic;
	
	private String tags;
			
	protected void init() throws MQClientException {
        consumer.subscribe(topic, tags);
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);  
        consumer.registerMessageListener(new MessageListenerConcurrently() {
                public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext Context) {
                	MessageExt msg = list.get(0);
                    logger.info(msg.toString());
                    Object message = ByteUtils.fromByte(msg.getBody());
                    processMessage(message);
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
            }
        );
        consumer.start();
	}

	public void setTopic(String topic) {
		this.topic = topic.trim().replaceAll(" ", "");
	}
	
	public void setConsumer(RocketMQPushConsumer consumer) {
		this.consumer = consumer;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}
	
	
}
