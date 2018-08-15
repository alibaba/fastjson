package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONCreator;
import junit.framework.TestCase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Bug_for_chengyi extends TestCase {
    public void test_0() throws Exception {
        List<Pair<String, Integer>> pairList = new ArrayList();
        Pair<String, Integer> pair = Pair.of("cy", 1);
        pairList.add(pair);

        final String s = JSON.toJSONString(pairList);
        final List<Pair> pairs = JSONArray.parseArray(s, Pair.class);
        System.out.println();
    }

    public static class Pair<A, B> implements Serializable {

        private static final long serialVersionUID = -2140946024027818984L;

        public final A fst;
        public final B snd;

        public Pair() {
            fst = null;
            snd = null;
        }

        @JSONCreator
        public Pair(A fst, B snd) {
            this.fst = fst;
            this.snd = snd;
        }

        @Override
        public String toString() {
            return "[" + fst + "," + snd + "]";
        }

        private boolean equals(Object x, Object y) {
            return (x == null && y == null) || (x != null && x.equals(y));
        }

        @SuppressWarnings("rawtypes")
        @Override
        public boolean equals(Object other) {
            return other instanceof Pair && equals(fst, ((Pair) other).fst)
                    && equals(snd, ((Pair) other).snd);
        }

        /**
         * 覆盖hashCode方法
         *
         * @return hashCode
         */
        @Override
        public int hashCode() {
            if (fst == null) {
                return (snd == null) ? 0 : snd.hashCode() + 1;
            } else if (snd == null) {
                return fst.hashCode() + 2;
            } else {
                return fst.hashCode() * 17 + snd.hashCode();
            }
        }

        public static <A, B> Pair<A, B> of(A a, B b) {
            return new Pair<A, B>(a, b);
        }

    }
}
