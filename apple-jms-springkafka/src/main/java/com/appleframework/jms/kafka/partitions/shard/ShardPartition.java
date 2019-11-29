package com.appleframework.jms.kafka.partitions.shard;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appleframework.jms.kafka.utils.GsonUtils;

public class ShardPartition {

	private static Logger logger = LoggerFactory.getLogger(ShardPartition.class);
	
	private TreeMap<Long, ShardNode> nodes;
	
	private List<ShardNode> shards ;
	
	private int nodeNum ;
	
	private final int NODE_NUM = 100; // 每个机器节点关联的虚拟节点个数
	
	private AtomicBoolean isInit = new AtomicBoolean(false) ;
	
	private AtomicBoolean isPartition = new AtomicBoolean(false) ;
	
	private Integer shardNumber;
	
	private String shardNodePrefix ;
	
	public ShardPartition() {
		super();
	}

	public ShardPartition(Integer shardNumber, String shardNodePrefix) {
		super();
		this.shardNumber = shardNumber;
		this.shardNodePrefix = shardNodePrefix;
	}

	/**
	 * 初始化
	 */
	private void init(){
		if(isInit.compareAndSet(false, true)){
			String nodePrefixStr = shardNodePrefix ;
			nodeNum = shardNumber ;
			if(shardNumber==null){
				nodeNum = ShardContants.getShardNumber();
			}
			if(shardNodePrefix==null){
				 nodePrefixStr = ShardContants.getShardNodePrefix();
			}
			shards = new ArrayList<>();
			ShardNode node = null ;
			for(int i=0;i<nodeNum;i++){
				node = new ShardNode();
				node.setNode(nodePrefixStr+i);
				shards.add(node);
			}
			logger.info("hash环初始化完成,节点个数number:{},节点nodes:{}",nodeNum, GsonUtils.toJson(shards));
		}
	}
	
	/**
	 * 分割初始化
	 */
	public void partition(){
		if(!isInit.get()){
			init();
		}
		if(isPartition.compareAndSet(false, true)){
			nodes = new TreeMap<Long, ShardNode>();
			for (int i = 0; i != shards.size(); ++i){
				ShardNode shardInfo = shards.get(i); 
				for (int n = 0; n < NODE_NUM; n++){
					 nodes.put(hash("SHARD-" + i + "-NODE-" + n), shardInfo);
				}
			}
		}
	}
	
	/**
	 * 获取分割节点
	 * @param key
	 * @return
	 */
	public ShardNode getShardInfo(String key) {  
		SortedMap<Long, ShardNode> tail = nodes.tailMap(hash(key));
        if (tail.size() == 0) {  
            return nodes.get(nodes.firstKey());  
        }  
        return tail.get(tail.firstKey());
	}
	
	private Long hash(String key) {  
        ByteBuffer buf = ByteBuffer.wrap(key.getBytes());  
        int seed = 0x1234ABCD;  
        ByteOrder byteOrder = buf.order();  
        buf.order(ByteOrder.LITTLE_ENDIAN);  
        long m = 0xc6a4a7935bd1e995L;  
        int r = 47;  
        long h = seed ^ (buf.remaining() * m);  
        long k;  
        while (buf.remaining() >= 8) {  
            k = buf.getLong();  
            k *= m;  
            k ^= k >>> r;  
            k *= m;  
            h ^= k;  
            h *= m;  
        }  
        if (buf.remaining() > 0) {  
            ByteBuffer finish = ByteBuffer.allocate(8).order(  
                    ByteOrder.LITTLE_ENDIAN);  
            // for big-endian version, do this first:   
            // finish.position(8-buf.remaining());   
            finish.put(buf).rewind();  
            h ^= finish.getLong();  
            h *= m;  
        }  
        h ^= h >>> r;  
        h *= m;  
        h ^= h >>> r;  
        buf.order(byteOrder);  
        return h;  
    }

	public List<ShardNode> getShards() {
		return new ArrayList<>(shards);
	}

	public Integer getShardNumber() {
		return shardNumber;
	}

	public void setShardNumber(Integer shardNumberKey) {
		this.shardNumber = shardNumberKey;
	}

	public String getShardNodePrefix() {
		return shardNodePrefix;
	}

	public void setShardNodePrefixKey(String shardNodePrefix) {
		this.shardNodePrefix = shardNodePrefix;
	}
}
