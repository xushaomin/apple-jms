package com.appleframework.jms.kafka.partitions;

import java.util.List;
import java.util.Map;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;

import com.appleframework.jms.kafka.utils.RandomUtility;

/**
 * @author cruise.xu
 * 
 */
public class ModPartitioner implements Partitioner {
	
	public ModPartitioner() {
		super();
	}
	
	@Override
	public void configure(Map<String, ?> configs) {
	}

	@Override
	public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        List<PartitionInfo> partitions = cluster.partitionsForTopic(topic);
        int numPartitions = partitions.size();
		if (null == key) {
			return RandomUtility.genRandom(numPartitions);
		} else if (key instanceof String) {
			int partition = Integer.parseInt(key.toString()) % numPartitions;
			return Math.abs(partition);
		} else if (key instanceof Integer) {
			int partition = (Integer) key % numPartitions;
			return Math.abs(partition);
		} else if (key instanceof Long) {
			Long partition = (Long) key % numPartitions;
			return Math.abs(partition.intValue());
		} else {
			return RandomUtility.genRandom(numPartitions);
		}
	}

	@Override
	public void close() {		
	}
	
}