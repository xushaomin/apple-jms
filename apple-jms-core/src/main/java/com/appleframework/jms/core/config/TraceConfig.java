package com.appleframework.jms.core.config;

public class TraceConfig {

	private static Boolean KEY_FOR_TRACE_ID_SET = null;

	private static boolean KEY_FOR_TRACE_ID_DEFAULT = false;

	public static String KEY_TRACE_ID_SET = null;

	public static String KEY_TRACE_ID_DEFAULT = "traceId";

	public static final String KEY_FOR_TRACE_SWITCH = "kafka.trace.switch";

	public static final String KEY_FOR_TRACE_ID = "kafka.trace.key";

	public static boolean isSwitchTrace() {
		if (null == KEY_FOR_TRACE_ID_SET) {
			String keyForTraceId = System.getProperty(KEY_FOR_TRACE_SWITCH);
			if (null != keyForTraceId) {
				KEY_FOR_TRACE_ID_SET = Boolean.parseBoolean(keyForTraceId);
			} else {
				KEY_FOR_TRACE_ID_SET = KEY_FOR_TRACE_ID_DEFAULT;
			}
		}
		return KEY_FOR_TRACE_ID_SET;
	}

	public static String getTraceIdKey() {
		if (null == KEY_TRACE_ID_SET) {
			String keyForTraceId = System.getProperty(KEY_FOR_TRACE_ID);
			if (null != keyForTraceId) {
				KEY_TRACE_ID_SET = keyForTraceId;
			} else {
				KEY_TRACE_ID_SET = KEY_TRACE_ID_DEFAULT;
			}
		}
		return KEY_TRACE_ID_SET;
	}
}
