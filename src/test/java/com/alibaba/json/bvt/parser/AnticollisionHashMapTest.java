package com.alibaba.json.bvt.parser;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

import junit.framework.TestCase;

import com.alibaba.fastjson.util.AnticollisionHashMap;

public class AnticollisionHashMapTest extends TestCase {
	
	public void testHash() {
		try {
			InputStream in = AnticollisionHashMapTest.class
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

			
			m = new AnticollisionHashMap<String, String>();
			m.put("axmantest", "12345");
			m.put("axmantest1", "123451");
			m.put("axmantest2", "123452");
			m.put("axmantest3", "123453");
			System.out.println(m.get("axmantest"));
			System.out.println(m.get("axmantest1"));
			System.out.println(m.get("axmantest2"));
			System.out.println(m.get("axmantest3"));
			ByteArrayOutputStream buf = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(buf);
			out.writeObject(m);
			out.flush();
			byte[] data = buf.toByteArray();
			out.close();
			
			ByteArrayInputStream inbuf = new ByteArrayInputStream(data);
			ObjectInputStream oin = new ObjectInputStream(inbuf);
			
			@SuppressWarnings("unchecked")
			AnticollisionHashMap<String, String> m1 = (AnticollisionHashMap<String, String>)oin.readObject();
			oin.close();
			System.out.println(m1.get("axmantest"));
			System.out.println(m1.get("axmantest1"));
			System.out.println(m1.get("axmantest2"));
			System.out.println(m1.get("axmantest3"));
			
//			start = System.currentTimeMillis();
//			Map<String, String> m1 = new HashMap<String, String>();
//			for (String kv : kvs) {
//				String[] cols = kv.split("=");
//				m1.put(cols[0], "test");
//			}
//			System.out.println("map size: " + m.size());
//			System.out.println("take time:"
//					+ (System.currentTimeMillis() - start));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
