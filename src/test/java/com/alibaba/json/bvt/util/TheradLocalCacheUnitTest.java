package com.alibaba.json.bvt.util;

import static com.alibaba.fastjson.util.ThreadLocalCache.CHARS_CACH_INIT_SIZE;
import static com.alibaba.fastjson.util.ThreadLocalCache.CHARS_CACH_INIT_SIZE_EXP;
import static com.alibaba.fastjson.util.ThreadLocalCache.CHARS_CACH_MAX_SIZE;
import static com.alibaba.fastjson.util.ThreadLocalCache.CHARS_CACH_MAX_SIZE_EXP;

import java.lang.reflect.Method;
import java.util.Random;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.alibaba.fastjson.util.ThreadLocalCache;

/**
 * Test new {@link #getAllocateLengthExp(int, int, int)} comparing with old
 * {@link #getAllocateLength(int, int, int)} method in {@link ThreadLocalCache}.
 */
public class TheradLocalCacheUnitTest {

	static Method getAllocateLengthMethod;
	static Method getAllocateLengthExpMethod;

	@BeforeClass
	public static void init() throws Exception {
		getAllocateLengthMethod = ThreadLocalCache.class.getDeclaredMethod("getAllocateLength",
				int.class, int.class, int.class);
		getAllocateLengthExpMethod = ThreadLocalCache.class.getDeclaredMethod(
				"getAllocateLengthExp", int.class, int.class, int.class);

		getAllocateLengthMethod.setAccessible(true);
		getAllocateLengthExpMethod.setAccessible(true);
	}

	@Test
	public void getAllocateLengthNewImplementationRandomTest() throws Exception {
		Random random = new Random();
		for (int i = 0; i < 100000000; i++) {
			int length = Math.abs(random.nextInt());
			testSample(length);
		}
	}

	@Test
	public void getAllocateLengthNewImplementationCertainTest() throws Exception {
		testSample(0);
		testSample(1);
		testSample(1024);
		testSample(CHARS_CACH_INIT_SIZE);
		testSample(CHARS_CACH_MAX_SIZE);
		testSample(114649);

		testSample(23);
		testSample(20);
		testSample(2048);
	}

	private void testSample(int length) throws Exception {
		if (length > CHARS_CACH_MAX_SIZE) {
			return;
		}
		int oldWay = getAllocateLength(CHARS_CACH_INIT_SIZE, CHARS_CACH_MAX_SIZE, length);
		int newWay = getAllocateLengthExp(CHARS_CACH_INIT_SIZE_EXP, CHARS_CACH_MAX_SIZE_EXP, length);
		// since new method always need premise length <= max value. we need to
		// skip this case.
		Assert.assertEquals("error when test length:" + length, oldWay, newWay);
	}

	private static int getAllocateLength(int init, int max, int length) throws Exception {
		return (Integer) getAllocateLengthMethod.invoke(ThreadLocalCache.class, init, max, length);
	}

	private static int getAllocateLengthExp(int minExp, int maxExp, int length) throws Exception {
		return (Integer) getAllocateLengthExpMethod.invoke(ThreadLocalCache.class, minExp, maxExp, length);
	}
}
