package com.appleframework.jms.kafka;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:config/kafka-consumer.xml" })
public class KafkaMessageComsumerTest {
    
	@Test
	public void testAddOpinion1() {
		try {
			System.in.read();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
