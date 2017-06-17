package com.appleframework.jms.kafka.utils;

import java.util.Random;

public class RandomUtility {
	
	private static Random random = new Random();

	public static int genRandom(int min, int max) {
		int s = random.nextInt(max) % (max - min + 1) + min;
		return s;
	}
	
	public static int genRandom(int max) {
		return random.nextInt(max);
	}

}
