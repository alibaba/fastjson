package com.alibaba.json.bvt.issue_1400;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.annotation.JSONField;
import junit.framework.TestCase;

import java.util.List;

public class Issue1486 extends TestCase {
    public void test_for_issue() throws Exception {

        String json = "[{\"song_list\":[{\"val\":1,\"v_al\":2},{\"val\":2,\"v_al\":2},{\"val\":3,\"v_al\":2}],\"songlist\":\"v_al\"}]";
        List<Value> parseObject = JSON.parseObject(json, new TypeReference<List<Value>>() {
        });
        for (Value value : parseObject) {
            System.out.println(value.songList + "  " );
        }
    }

    public static class Value {
        @JSONField(alternateNames = {"song_list", "songList"})
        List<Value2> songList;

        @JSONField(alternateNames = {"songlist"})
        String songlist;

        public List<Value2> getSongList() {
            return songList;
        }

        public void setSongList(List<Value2> songList) {
            this.songList = songList;
        }

        public String getSonglist() {
            return songlist;
        }

        public void setSonglist(String songlist) {
            this.songlist = songlist;
        }
    }

    public static class Value2 {
        int val;

        int v_al;

        public int getVal() {
            return val;
        }

        public void setVal(int val) {
            this.val = val;
        }

        public int getV_al() {
            return v_al;
        }

        public void setV_al(int v_al) {
            this.v_al = v_al;
        }

        @Override
        public String toString() {
            return "Value2{" +
                    "val=" + val +
                    ", v_al=" + v_al +
                    '}';
        }
    }
}
