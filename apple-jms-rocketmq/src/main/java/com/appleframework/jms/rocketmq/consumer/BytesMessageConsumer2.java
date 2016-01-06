package com.appleframework.jms.rocketmq.consumer;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.consumer.ConsumeFromWhere;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.appleframework.jms.core.consumer.MessageConusmer2;
import com.appleframework.jms.rocketmq.RocketMQPushConsumer;

/**
 * @author Cruise.Xu
 * 
 */
public class BytesMessageConsumer2 {
	
	private final static Logger logger = LoggerFactory.getLogger(BytesMessageConsumer2.class);
	
	private RocketMQPushConsumer consumer;
	
	private String topic;
	
	private String tags;
	
	@Resource
	private MessageConusmer2<byte[]> messageConusmer2;
	
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
                public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext Context) {
                    Message msg = list.get(0);
                    logger.info(msg.toString());
                    messageConusmer2.processMessage(msg.getBody());
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
            }
        );
        consumer.start();
	}

	public void setMessageConusmer2(MessageConusmer2<byte[]> messageConusmer2) {
		this.messageConusmer2 = messageConusmer2;
	}
}
