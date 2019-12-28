package com.appleframework.jms.core.utils;

import java.util.UUID;

public class UuidUtils {

    public static String genUUID() {
    	return UUID.randomUUID().toString();
    }

}
