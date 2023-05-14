package com.alibaba.json.test;

import com.alibaba.fastjson.util.IdentityHashMap;
import junit.framework.TestCase;

import java.util.Random;

/**
 * Created by wenshao on 14/08/2017.
 */
public class Issue1407 extends TestCase {
    public void test_for_issue() throws Exception {
        final String key = "k";
        final IdentityHashMap map = new IdentityHashMap(2);
        final Random ran = new Random();

        new Thread() {
            public void run() {
                while(true) {
                    String kk = (key + ran.nextInt(2));
                    if (map.get(kk) != null) {
//                        System.out.println("\tskip_a " + kk);
                        continue;
                    }
//			synchronized(map) {
                    map.put(kk, kk);

                    System.out.println("\tput_a " + kk);
//			}
                    Object val = map.get(kk);
                    if(val == null) {
                        System.err.println("err_a : " + kk);
                    }
                }
            }
        }.start();

        new Thread() {
            public void run() {
                while(true) {
                    String kk = (key + ran.nextInt(2));
//			synchronized(map) {
                    if (map.get(kk) != null) {
//                        System.out.println("\tskip_b " + kk);
                        continue;
                    }
                    map.put(kk, kk);

                    System.out.println("\tput_b " + kk);
//			}
                    Object val = map.get(kk);
                    if(val == null) {
                        System.err.println("err_b : " + kk);
                    }
                }
            }
        }.start();

        Thread.sleep(1000 * 1000);
    }

}
