package com.appleframework.jms.jedis.consumer;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:config/apple-jms-jedis-consumer.xml" })
public class QueueMessageConsumerTest {
	
	@Resource
	private QueueMessageConsumerImpl messageConsumer;
    
	@Test
	public void testAddOpinion1() {
		try {
			//messageConsumer.init();
			System.in.read();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}