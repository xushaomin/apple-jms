package com.appleframework.jms.core.utils;

import java.util.UUID;

import org.slf4j.MDC;

import com.appleframework.jms.core.config.TraceConfig;

public class TraceUtils {

    public static String getTraceId() {
    	String traceId = MDC.get(TraceConfig.getTraceIdKey());
    	if(null == traceId) {
    		traceId = UUID.randomUUID().toString();
    	}
    	return traceId;
    }

}
