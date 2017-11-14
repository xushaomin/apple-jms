package com.appleframework.jms.rabbitmq.consumer;

import java.io.IOException;

import com.appleframework.jms.core.consumer.BytesMessageConusmer;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

/**
 * @author Cruise.Xu
 * 
 */
public abstract class BaseMessageConsumer extends BytesMessageConusmer {

	private Channel channel;

	private String topic;

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	protected void init() throws IOException {
		Consumer consumer = new DefaultConsumer(channel) {  
            @Override  
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)  
                    throws IOException {
            	processByteMessage(body);
            }  
        };  
        channel.basicConsume(topic, true, consumer);  
	}

	public String getTopic() {
		return topic;
	}
	
	
}