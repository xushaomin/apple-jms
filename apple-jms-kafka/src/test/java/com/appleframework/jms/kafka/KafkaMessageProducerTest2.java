package com.appleframework.jms.kafka;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.appleframework.jms.kafka.producer.KafkaMessageProducer2;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:config/spring-producer.xml" })
public class KafkaMessageProducerTest2 {

	private static Logger logger = Logger.getLogger(KafkaMessageProducerTest2.class.getName());
    
	@Resource
	private KafkaMessageProducer2 messageProducer2;

	@Test
	public void testAddOpinion1() {
		try {
			for (int i = 0; i < 10; i++) {
				messageProducer2.sendText(null, "xuxuux" + i);
			}
			logger.error("------------------");
			System.in.read();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
