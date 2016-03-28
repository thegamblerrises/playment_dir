package com.playment.util;

public class KeyGenerator {

	private static int KEY_LIMIT = 1000;
	private static int counter = 0;
	private static Object lock = new Object();

	public static long getInvoiceKey() {
		synchronized (lock) {
			long val = System.currentTimeMillis();
			val = val * KEY_LIMIT + ++counter;
			if (counter > KEY_LIMIT)
				counter = 0;
			return val;
		}
	}

}
