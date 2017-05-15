package com.appleframework.jms.rocketmq.consumer;

import java.util.List;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.consumer.ConsumeFromWhere;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.appleframework.jms.core.consumer.MessageConusmer;
import com.appleframework.jms.rocketmq.RocketMQPushConsumer;


/**
 * @author Cruise.Xu
 * 
 */
public abstract class BaseMessageConsumer extends MessageConusmer<Message> {
	
	//private final static Logger logger = LoggerFactory.getLogger(BaseMessageConsumer.class);
	
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
                public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext Context) {
                    Message message = list.get(0);
                    //logger.info(message.toString());
                    processMessage(message);
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
            }
        );
        consumer.start();
	}
	
}
