package com.appleframework.jms.core.sender;

import java.io.Serializable;

public class MessageObject<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	private T object;
	private String msgId;
	private String trackId;

	public T getObject() {
		return object;
	}

	public void setObject(T object) {
		this.object = object;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public String getTrackId() {
		return trackId;
	}

	public void setTrackId(String trackId) {
		this.trackId = trackId;
	}

	public MessageObject(T object, String trackId) {
		super();
		this.object = object;
		this.trackId = trackId;
	}
	
	public MessageObject(T object, String trackId, String msgId) {
		super();
		this.object = object;
		this.trackId = trackId;
		this.msgId = msgId;
	}

	public MessageObject() {
		super();
	}

}
