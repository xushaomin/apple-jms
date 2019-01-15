package com.appleframework.jms.core.consumer;


/**
 * 消费者端处理错误消息重试处理器
 * 
 * @description <br>
 * @date 2016年10月25日
 */
public interface ErrorMessageProcessor<T>  {

	public void processErrorMessage(T message,  AbstractMessageConusmer<T> messageConusmer);
	
	public void processErrorMessage(T message,  IMessageConusmer<T> messageConusmer);

	public void close();
}
