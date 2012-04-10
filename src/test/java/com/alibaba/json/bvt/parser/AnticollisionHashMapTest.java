package com.alibaba.json.bvt.parser;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import com.alibaba.fastjson.util.AnticollisionHashMap;

public class SafelyHashMapTest extends TestCase {
	public void testHash() {
		try {
			InputStream in = SafelyHashMapTest.class
					.getResourceAsStream("/hashcollide.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String line = br.readLine();
			br.close();
			String[] kvs = line.split("&");
			long start = System.currentTimeMillis();
			Map<String, String> m = new AnticollisionHashMap<String, String>();
			for (String kv : kvs) {
				String[] cols = kv.split("=");
				m.put(cols[0], "test");
			}
			System.out.println("map size: " + m.size());
			System.out.println("take time:"
					+ (System.currentTimeMillis() - start));
		} catch (Exception e) {
			System.out.println("-----------------------------------------------");
			e.printStackTrace();
		}

		// start = System.currentTimeMillis();
		// Map<String, String> m1 = new HashMap<String, String>();
		// for (String kv : kvs) {
		// String[] cols = kv.split("=");
		// m1.put(cols[0], "test");
		// }
		// System.out.println("map size: " + m.size());
		// System.out.println("take time:" + (System.currentTimeMillis() -
		// start));
	}
}
