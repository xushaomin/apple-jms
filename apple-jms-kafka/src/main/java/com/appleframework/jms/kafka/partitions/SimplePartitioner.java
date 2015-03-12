package com.appleframework.jms.kafka.partitions;

import kafka.producer.Partitioner;
import kafka.utils.VerifiableProperties;

/**
 * @author caicq
 * 
 */
public class SimplePartitioner implements Partitioner {
	
	public SimplePartitioner(VerifiableProperties props) {
	}

	@Override
	public int partition(Object key, int numPartitions) {
		int partition = Integer.parseInt(key.toString());
		return partition;
	}

}
