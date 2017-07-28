package com.appleframework.jms.kafka.partitions;

import com.appleframework.jms.kafka.utils.RandomUtility;

import kafka.producer.Partitioner;
import kafka.utils.VerifiableProperties;

/**
 * @author cruise.xu
 * 
 */
@SuppressWarnings("deprecation")
public class ModPartitioner implements Partitioner {

	public ModPartitioner(VerifiableProperties props) {
	}

	@Override
	public int partition(Object key, int numPartitions) {
		if (null == key) {
			return RandomUtility.genRandom(numPartitions);
		} else if(key instanceof Integer) {
			int partition = (Integer)key % numPartitions;
			return Math.abs(partition);
		} else if(key instanceof Long) {
			Long partition = (Long)key % numPartitions;
			return Math.abs(partition.intValue());
		} else {
			return RandomUtility.genRandom(numPartitions);
		}
	}

}
