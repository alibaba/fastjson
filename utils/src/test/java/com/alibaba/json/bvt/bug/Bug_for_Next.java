package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import junit.framework.TestCase;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import java.util.*;


public class Bug_for_Next extends TestCase {

    public static void main(String[] args) throws Exception {
        Result result = JUnitCore.runClasses(Bug_for_Next.class);
        for (Failure fail : result.getFailures()) {
            System.out.println(fail.toString());
        }
        if (result.wasSuccessful()) {
            System.out.println("All tests finished successfully...");
        }
    }

    public void testPrimitiveArray() throws Exception {

        showTitle("1=====================================");
        String text= JSON.toJSONString("testbytearray".getBytes());
        showMesg("text : " + text);
        byte[] byteArray = JSON.parseObject(text, byte[].class);
        showMesg("byteArray : " + byteArrayToHexString(byteArray));

        int[][] ii = new int[][]{
                new int[]{ 1, 2, 3},
                new int[]{ 2, 3, 4, 5}
        };

        text = JSON.toJSONString(ii);
        showMesg("text : " + text);
        int[][] pii = JSON.parseObject(text, int[][].class);
        showMesg("pii : " + Arrays.toString(pii));
        showMesg("pii0 : " + Arrays.toString(pii[0]));
        showMesg("pii1 : " + Arrays.toString(pii[1]));

        showTitle("2=====================================");
        List<byte[]> blist = new ArrayList<byte[]>();
        blist.add("byte[]".getBytes());
        blist.add("blist".getBytes());
        text = JSON.toJSONString(blist);
        showMesg("text : " + text);
        blist = JSON.parseObject(text, new TypeReference<List<byte[]>>(byte[].class){}.getType());
        showMesg("blist : " + blist);
        showMesg("blist1 : " + byteArrayToHexString(blist.get(0)));
        showMesg("blist2 : " + byteArrayToHexString(blist.get(1)));

        List<char[]> clist = new ArrayList<char[]>();
        clist.add(new char[]{'1',',','2'});
        clist.add(new char[]{'2',',','1'});
        text = JSON.toJSONString(clist);
        showMesg("text " + text);
        clist = JSON.parseObject(text, new TypeReference<List<char[]>>(char[].class){});
        showMesg("clist : " + clist);
        showMesg("clist1 : " + Arrays.toString(clist.get(0)));
        showMesg("clist2 : " + Arrays.toString(clist.get(1)));

        List<int[]> ilist = new ArrayList<int[]>();
        ilist.add(new int[]{11,22,33});
        ilist.add(new int[]{33,22,11});
        text = JSON.toJSONString(ilist);
        showMesg("text " + text);
        ilist = JSON.parseObject(text, new TypeReference<List<int[]>>(int[].class){});
        showMesg("ilist : " + ilist);
        showMesg("ilist1 : " + Arrays.toString(ilist.get(0)));
        showMesg("ilist2 : " + Arrays.toString(ilist.get(1)));

        List<float[]> flist = new ArrayList<float[]>();
        flist.add(new float[]{11.2f,22.3f,33.4f});
        flist.add(new float[]{33.1f,22.2f,11.3f});
        text = JSON.toJSONString(flist);
        showMesg("text " + text);
        flist = JSON.parseObject(text, new TypeReference<List<float[]>>(float[].class){});
        showMesg("flist : " + flist);
        showMesg("flist1 : " + Arrays.toString(flist.get(0)));
        showMesg("flist2 : " + Arrays.toString(flist.get(1)));

        List<int[][]> iilist = new ArrayList<int[][]>();
        iilist.add(new int[][] {
                new int[]{9,6,3},
                new int[]{8,5,2}
        });
        iilist.add(new int[][] {
                new int[]{7,4,1},
                new int[]{0}
        });
        text = JSON.toJSONString(iilist);
        showMesg("text : " + text);
        iilist = JSON.parseObject(text, new TypeReference<List<int[][]>>(int[][].class){});
        showMesg("iilist : " + iilist);
        showMesg("iilist1 : " + Arrays.toString(iilist.get(0)[0]));
        showMesg("iilist2 : " + Arrays.toString(iilist.get(1)[0]));

        showTitle("3=====================================");
        Map<String, byte[]> sbmap = new HashMap<String, byte[]>();
        sbmap.put("key1", "key1".getBytes());
        sbmap.put("key2", "key2".getBytes());
        text = JSON.toJSONString(sbmap);
        showMesg("sbmap : " + text);
        sbmap = JSON.parseObject(text, new TypeReference<Map<String, byte[]>>(String.class, byte[].class){});
        showMesg("sbmap : " + sbmap);
        showMesg("sbmap key1 : " + byteArrayToHexString(sbmap.get("key1")));
        showMesg("sbmap key2 : " + byteArrayToHexString(sbmap.get("key2")));

        showTitle("4=====================================");
        Map<String, Byte[]> sbcmap = new HashMap<String, Byte[]>();
        sbcmap.put("key1", new Byte[]{ 1, 2, 3 });
        sbcmap.put("key2", new Byte[]{ 3, 2, 1 });
        text = JSON.toJSONString(sbcmap);
        showMesg("sbcmap json : " + text);
        sbcmap = JSON.parseObject(text, new TypeReference<Map<String, Byte[]>>(String.class, Byte[].class){});
        showMesg("sbcmap : " + sbcmap);
        showMesg("sbcmap key1 : " + Arrays.toString(sbcmap.get("key1")));
        showMesg("sbcmap key1 : " + Arrays.toString(sbcmap.get("key2")));

        showTitle("5=====================================");
        int[] intArray = new int[]{ 11, 22, 33 };
        text = JSON.toJSONString(intArray);
        showMesg("intArray json : " + text);
        intArray = JSON.parseObject(text, int[].class);
        showMesg("intArray : " + Arrays.toString(intArray));

        showTitle("6=====================================");
        Map<String, int[]> simap = new HashMap<String, int[]>();
        simap.put("key1", new int[]{ 11, 22, 33 });
        simap.put("key2", new int[]{ 33, 22, 11 });
        text = JSON.toJSONString(simap, SerializerFeature.WriteClassName);
        showMesg("simap json : " + text);
        simap = JSON.parseObject(text, new TypeReference<Map<String, int[]>>(String.class, int[].class){});
        showMesg("simap : " + simap);
        showMesg("simap key1 : " + Arrays.toString(simap.get("key1")));
        showMesg("simap key1 : " + Arrays.toString(simap.get("key2")));

        showTitle("7=====================================");
        Map<String, Integer[]> sicmap = new HashMap<String, Integer[]>();
        sicmap.put("key1", new Integer[]{ 12, 23, 34 });
        sicmap.put("key2", new Integer[]{ 34, 23, 12 });
        text = JSON.toJSONString(sicmap, SerializerFeature.WriteClassName);
        showMesg("sicmap json : " + text);
        sicmap = JSON.parseObject(text, new TypeReference<Map<String, Integer[]>>(String.class, Integer[].class){});
        showMesg("sicmap : " + sicmap);
        showMesg("sicmap key1 : " + Arrays.toString(sicmap.get("key1")));
        showMesg("sicmap key1 : " + Arrays.toString(sicmap.get("key2")));

        showTitle("8=====================================");
        HashMap<byte[], String> bsmap = new HashMap<byte[], String>();
        bsmap.put("testbytearray".getBytes(), "testbytearray");
        bsmap.put(new byte[] { 0, 1, 2}, "012");
        text = JSON.toJSONString(bsmap);
        showMesg("text : " + text);
        bsmap = JSON.parseObject(text, new TypeReference<Map<byte[], String>>(byte[].class, String.class){}.getType());
        showMesg("bsmap : " + bsmap);

        Iterator<byte[]> it = bsmap.keySet().iterator();
        int i = 0;
        while (it.hasNext()) {
            byte[] bs = it.next();
            showMesg("bsmap key" + i++ + " : " + byteArrayToHexString(bs));
        }

        Map<String, TestBean[]> stmap = new HashMap<String, TestBean[]>();
        stmap.put("key1", new TestBean[]{ new TestBean(), new TestBean()});
        stmap.put("key2", new TestBean[]{ new TestBean(), new TestBean(), new TestBean()});
        text = JSON.toJSONString(stmap);
        showMesg("stmap json : " + text);
        stmap = JSON.parseObject(text, new TypeReference<Map<String, TestBean[]>>(String.class, TestBean[].class){});
        showMesg("stmap : " + stmap);
        showMesg("key1 : " + Arrays.toString(stmap.get("key1")));
        showMesg("key2 : " + Arrays.toString(stmap.get("key2")));

    }

    private void showTitle(String title) {
        System.out.println("test " + title);
    }

    private void showMesg(String mesg) {
        System.out.println("  " + mesg);
    }

    private static String byteArrayToHexString(byte[] data) {
        return byteArrayToHexString(data, 0, data.length);
    }

    private static String byteArrayToHexString(byte[] data, int offest, int len) {
        if (data == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder();

        if(offest < 0 || offest > data.length){
            offest = 0;
        }

        int total = Math.min(len, data.length);

        int index = offest;

        while (total > 0) {
            if (total >= 16) {
                sb.append(String.format("%02x %02x %02x %02x %02x %02x %02x %02x %02x %02x %02x %02x %02x %02x %02x %02x ",
                        data[index], data[index + 1], data[index + 2],
                        data[index + 3], data[index + 4],
                        data[index + 5], data[index + 6],
                        data[index + 7], data[index + 8],
                        data[index + 9], data[index + 10],
                        data[index + 11], data[index + 12],
                        data[index + 13], data[index + 14],
                        data[index + 15]));
                index += 16;
                total -= 16;
            } else {
                for (int i = 0; i < total; i++) {
                    sb.append(String.format("%02x ", data[index]));
                    index++;
                }
                total = 0;
            }
        }

        return sb.toString();
    }

    static class TestBean {
        byte b;
        byte[] bs = "bs".getBytes();
        int i;
        int[] is = new int[]{ 753, 159 };
        String s;

        public byte getB() {
            return b;
        }

        public void setB(byte b) {
            this.b = b;
        }

        public byte[] getBs() {
            return bs;
        }

        public void setBs(byte[] bs) {
            this.bs = bs;
        }

        public int getI() {
            return i;
        }

        public void setI(int i) {
            this.i = i;
        }

        public int[] getIs() {
            return is;
        }

        public void setIs(int[] is) {
            this.is = is;
        }

        public String getS() {
            return s;
        }

        public void setS(String s) {
            this.s = s;
        }

        @Override
        public String toString() {
            return "TestBean{" +
                    "b=" + b +
                    ", bs=" + Arrays.toString(bs) +
                    ", i=" + i +
                    ", is=" + Arrays.toString(is) +
                    ", s='" + s + '\'' +
                    '}';
        }
    }
}
