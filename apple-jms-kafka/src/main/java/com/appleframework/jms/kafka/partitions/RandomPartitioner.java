package com.appleframework.jms.kafka.partitions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appleframework.jms.kafka.utils.RandomUtility;

import kafka.producer.Partitioner;
import kafka.utils.VerifiableProperties;

/**
 * @author cruise.xu
 * 
 */
public class RandomPartitioner implements Partitioner {
	
	private final static Logger logger = LoggerFactory.getLogger(RandomPartitioner.class);

	public RandomPartitioner(VerifiableProperties props) {
	}

	@Override
	public int partition(Object key, int numPartitions) {
		int partition = RandomUtility.genRandom(0, numPartitions);
		if(logger.isDebugEnabled()) {
			logger.debug("The numPartitions = " + numPartitions + " and Random partition = " + partition);
		}
		return partition;
	}

}
