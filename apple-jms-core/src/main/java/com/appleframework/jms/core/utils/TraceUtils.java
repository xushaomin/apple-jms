package com.appleframework.jms.core.utils;

import java.util.UUID;

import org.slf4j.MDC;

public class TraceUtils {

    public static String getTraceId() {
    	String traceId = MDC.get(Contants.KEY_TRACE_ID);
    	if(null == traceId) {
    		traceId = UUID.randomUUID().toString();
    	}
    	return traceId;
    }

}
