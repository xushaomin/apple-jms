package com.appleframework.jms.kafka.partitions;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appleframework.jms.kafka.partitions.shard.ShardContants;
import com.appleframework.jms.kafka.partitions.shard.ShardNode;
import com.appleframework.jms.kafka.partitions.shard.ShardPartition;

/**
 * 环型分区算法
 */
public class ShardPartitioner implements Partitioner {

	private static Logger logger = LoggerFactory.getLogger(ShardPartitioner.class);

	private volatile ShardPartition shardPartition;

	private static volatile ConcurrentHashMap<String, Integer> cache = new ConcurrentHashMap<>();

	private volatile Object lock = new Object();
	
	private static String shardNodePrefix = "";

	public ShardPartitioner() {
		super();
	}
	
	@Override
	public void configure(Map<String, ?> configs) {
		Object shardNodePrefixO = configs.get("shard.node.prefix");
		if(null != shardNodePrefixO) {
			shardNodePrefix = shardNodePrefixO.toString();
		}
		Object shardOpenO = configs.get("shard.open");
		if(null != shardOpenO) {
			ShardContants.setShardOpen(Boolean.parseBoolean(shardOpenO.toString()));
		}
	}

	@Override
	public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
		List<PartitionInfo> partitions = cluster.partitionsForTopic(topic);
		int numPartitions = partitions.size();
		String sn = String.valueOf(key);
		if (shardPartition != null) {
			return getCachePartition(sn);
		}
		synchronized (lock) {
			if (shardPartition == null) {
				shardPartition = new ShardPartition(numPartitions, shardNodePrefix);
				shardPartition.partition();
			}
		}
		return getCachePartition(sn);
	}

	@Override
	public void close() {
	}

	private int getCachePartition(String key) {
		if (!ShardContants.getShardOpen()) {
			return getPartitionByKey(key);
		}
		Integer partitionCache = cache.get(key);
		if (partitionCache != null) {
			return partitionCache;
		}
		synchronized (cache) {
			partitionCache = cache.get(key);
			if (partitionCache == null) {
				partitionCache = getPartitionByKey(key);
				cache.put(key, partitionCache);
			}
		}
		return partitionCache;
	}

	private Integer getPartitionByKey(String key) {
		ShardNode shardNode = shardPartition.getShardInfo(String.valueOf(key));
		String partition = shardNode.getNode();
		if (logger.isDebugEnabled()) {
			logger.debug("key = {},partition = {},numPartitions = {}", key, partition, shardPartition.getShardNumber());
		}
		return Integer.parseInt(partition);
	}

}