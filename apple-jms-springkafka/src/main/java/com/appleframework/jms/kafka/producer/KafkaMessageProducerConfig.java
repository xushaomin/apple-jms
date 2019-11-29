package com.appleframework.jms.kafka.producer;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

import com.appleframework.jms.core.producer.MessageProducer;
import com.appleframework.jms.core.producer.MessageProducer2;
import com.appleframework.jms.core.producer.MessageProducer3;

@Configuration
public class KafkaMessageProducerConfig {

	@Value("${spring.kafka.producer.topic:null}")
	private String topic;

	@Resource
	private KafkaTemplate<String, byte[]> kafkaTemplate;

	@Bean
	@ConditionalOnMissingBean(MessageProducer.class)
	public MessageProducer messageProducerFactory() {
		KafkaMessageProducer messageProducer = new KafkaMessageProducer();
		messageProducer.setKafkaTemplate(kafkaTemplate);
		messageProducer.setTopic(topic);
		return messageProducer;
	}

	@Bean
	@ConditionalOnMissingBean(MessageProducer2.class)
	public MessageProducer2 messageProducer2Factory() {
		KafkaMessageProducer2 messageProducer = new KafkaMessageProducer2();
		messageProducer.setKafkaTemplate(kafkaTemplate);
		return messageProducer;
	}

	@Bean
	@ConditionalOnMissingBean(MessageProducer3.class)
	public MessageProducer3 messageProducer3Factory() {
		KafkaMessageProducer3 messageProducer = new KafkaMessageProducer3();
		messageProducer.setKafkaTemplate(kafkaTemplate);
		return messageProducer;
	}

}
