package com.appleframework.jms.kafka.partitions;

import java.util.List;
import java.util.Map;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appleframework.jms.kafka.utils.RandomUtility;

/**
 * @author cruise.xu
 * 
 */
public class RandomPartitioner implements Partitioner {

	private final static Logger logger = LoggerFactory.getLogger(RandomPartitioner.class);

	@Override
	public void configure(Map<String, ?> configs) {
	}

	@Override
	public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
		List<PartitionInfo> partitions = cluster.partitionsForTopic(topic);
	  	int numPartitions = partitions.size();
	  	int partition = RandomUtility.genRandom(numPartitions);
	  	if(logger.isDebugEnabled()) {
	  		logger.debug("The numPartitions = " + numPartitions + " and Random partition = " + partition);
	  	}
	  	return partition;
	}

	@Override
	public void close() {
	}

}
