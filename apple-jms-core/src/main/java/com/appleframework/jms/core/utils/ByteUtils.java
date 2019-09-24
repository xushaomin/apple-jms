package com.appleframework.jms.core.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ByteUtils {

	private final static Logger logger = LoggerFactory.getLogger(ByteUtils.class);

	public static Object fromByte(byte[] bytes) {
		Object object = null;
		ByteArrayInputStream bais = null;
		ObjectInputStream ois = null;
		try {
			bais = new ByteArrayInputStream(bytes);
			ois = new ObjectInputStream(bais);
			object = ois.readObject();
		} catch (Exception e) {
			logger.error("ByteUtils.fromByte fail : ", e);
		} finally {
			if (null != ois) {
				try {
					ois.close();
				} catch (IOException e) {
					logger.error("ObjectInputStream close fail : ", e);
				}
			}
			if (null != bais) {
				try {
					bais.close();
				} catch (IOException e) {
					logger.error("ByteArrayInputStream close fail : ", e);
				}
			}
		}
		return object;
	}

	public static byte[] toBytes(Object object) {
		byte[] bytes = null;
		ByteArrayOutputStream baos = null;
		ObjectOutputStream oos = null;
		try {
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
			oos.flush();
			bytes = baos.toByteArray();
		} catch (Exception e) {
			logger.error("ByteUtils.toBytes fail : ", e);
		} finally {
			if (null != oos) {
				try {
					oos.close();
				} catch (IOException e) {
					logger.error("ObjectOutputStream close fail : ", e);
				}
			}
			if (null != baos) {
				try {
					baos.close();
				} catch (IOException e) {
					logger.error("ByteArrayOutputStream close fail : ", e);
				}
			}
		}
		return bytes;
	}

}