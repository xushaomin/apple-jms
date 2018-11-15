package com.appleframework.jms.kafka.partitions;

import java.util.List;
import java.util.Map;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;

import com.appleframework.jms.kafka.utils.RandomUtility;
import com.appleframework.jms.kafka.utils.StringUtils;

/**
 * @author cruise.xu
 * 
 */
public class SimplePartitioner implements Partitioner {

	@Override
	public void configure(Map<String, ?> configs) {		
	}

	@Override
	public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        List<PartitionInfo> partitions = cluster.partitionsForTopic(topic);
        int numPartitions = partitions.size();
		if (StringUtils.isEmpty(key) || key.equals("-1")) {
			return RandomUtility.genRandom(numPartitions);
		} else {
			int partitionKey = key.hashCode();
			int partition = partitionKey % numPartitions;
			return Math.abs(partition);
		}
	}

	@Override
	public void close() {		
	}
	
	

}
