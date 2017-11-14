package com.appleframework.jms.rabbitmq;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.appleframework.jms.core.producer.MessageProducer;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:META-INF/apple/rabbitmq-producer2.xml" })
public class RabbitMessageProducerTest {

	private static Logger logger = Logger.getLogger(RabbitMessageProducerTest.class.getName());
    
	@Resource
	private MessageProducer messageProducer;

	@Test
	public void testAddOpinion1() {
		try {
			long t = System.currentTimeMillis();
			for (int i = 1; i <= 30000; i++) {
				messageProducer.sendText("ddddddddd====" + i);
				System.out.println(i);
			}
			System.out.println(System.currentTimeMillis() - t);
			logger.error("------------------");
			System.in.read();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
