package com.appleframework.jms.kafka.partitions.shard;

/**
 * hash环节点
 */
public class ShardContants {

	private static Integer shardNumber = 5 ;
	
	private static String shardNodePrefix  = "apple-shard-node";
	
	private static Boolean shardOpen = true;

	public static Integer getShardNumber() {
		return shardNumber;
	}

	public static void setShardNumber(Integer shardNumber) {
		ShardContants.shardNumber = shardNumber;
	}

	public static String getShardNodePrefix() {
		return shardNodePrefix;
	}

	public static void setShardNodePrefix(String shardNodePrefix) {
		ShardContants.shardNodePrefix = shardNodePrefix;
	}

	public static void setShardOpen(Boolean shardOpen) {
		ShardContants.shardOpen = shardOpen;
	}

	public static Boolean getShardOpen() {
		return shardOpen;
	}
	
}
