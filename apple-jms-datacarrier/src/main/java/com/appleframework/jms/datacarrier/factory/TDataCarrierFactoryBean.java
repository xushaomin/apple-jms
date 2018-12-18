package com.appleframework.jms.datacarrier.factory;

import org.springframework.beans.factory.FactoryBean;

import com.a.eye.datacarrier.DataCarrier;
import com.a.eye.datacarrier.buffer.BufferStrategy;
import com.a.eye.datacarrier.consumer.IConsumer;
import com.a.eye.datacarrier.partition.IDataPartitioner;

@SuppressWarnings("rawtypes")
public class TDataCarrierFactoryBean implements FactoryBean<DataCarrier> {

	private int channelSize = 10;
	private int bufferSize = 10000;
	private BufferStrategy BUFFERSTRATEGY = BufferStrategy.IF_POSSIBLE;
	private IDataPartitioner dataPartitioner;
	private IConsumer consumer;
	private Integer num = 10;

	public void setChannelSize(int channelSize) {
		this.channelSize = channelSize;
	}

	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
	}

	@SuppressWarnings("unchecked")
	@Override
	public DataCarrier getObject() throws Exception {
		DataCarrier<Object> carrier = new DataCarrier<Object>(channelSize, bufferSize);
		carrier.setBufferStrategy(BUFFERSTRATEGY);
		if (null != dataPartitioner) {
			carrier.setPartitioner(dataPartitioner);
		}
		carrier.consume(consumer, num);
		return carrier;
	}

	@Override
	public Class<DataCarrier> getObjectType() {
		return DataCarrier.class;
	}

	@Override
	public boolean isSingleton() {
		return false;
	}

	public void setBufferStrategy(String bufferStrategy) {
		if (bufferStrategy.equalsIgnoreCase("BLOCKING")) {
			BUFFERSTRATEGY = BufferStrategy.BLOCKING;
		} else if (bufferStrategy.equalsIgnoreCase("OVERRIDE")) {
			BUFFERSTRATEGY = BufferStrategy.OVERRIDE;
		} else {
			BUFFERSTRATEGY = BufferStrategy.IF_POSSIBLE;
		}
	}

	public void setDataPartitionerClass(String dataPartitionerClass) {
		if (null != dataPartitionerClass) {
			Class<?> clazz;
			try {
				clazz = Class.forName(dataPartitionerClass);
				dataPartitioner = (IDataPartitioner) clazz.newInstance();
			} catch (Exception e) {
			}
		}
	}

	public void setConsumer(IConsumer consumer) {
		this.consumer = consumer;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

}
