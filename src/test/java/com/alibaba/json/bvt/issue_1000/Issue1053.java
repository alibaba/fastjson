package com.alibaba.json.bvt.issue_1000;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import junit.framework.TestCase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by wenshao on 02/05/2017.
 */
public class Issue1053 extends TestCase {

    public void test_for_issue() throws InterruptedException {
        List<String> a = Arrays.asList("确定", "确认", "是的", "好的", "没错", "可以", "对的", "行的");
        List<String> b = Arrays.asList("别了", "停止", "取消", "不对", "不行", "不好", "不是", "算了", "不要");
        List<List> c = new ArrayList<List>();
        c.add(a);
        c.add(b);
        Request e = new Request("TYPE_MULTI_SELECTION", c);

        String expected = "{\"indexPhrase\":[[\"确定\",\"确认\",\"是的\",\"好的\",\"没错\",\"可以\",\"对的\",\"行的\"],[\"别了\",\"停止\",\"取消\",\"不对\",\"不行\",\"不好\",\"不是\",\"算了\",\"不要\"]],\"jsonObject\":{\"sel_index_phrase\":[{\"$ref\":\"$.indexPhrase[0]\"},{\"$ref\":\"$.indexPhrase[1]\"}]},\"paramType\":\"TYPE_MULTI_SELECTION\"}";
        String request = JSON.toJSONString(e);
        assertEquals(expected, request);

        Request request1 = null;
        try {
            request1 = JSON.parseObject(request, Request.class);
        } catch (Exception ex) {
            System.out.println("error  :  " + ex);
        }
        assertEquals(expected, JSON.toJSONString(request1));
    }

    public static class Request implements Serializable {

        public String paramType;
        public JSONObject jsonObject = new JSONObject();

        public Request(){

        }

        public Request(String paramType, List<? extends List> list) {
            this.paramType = paramType;
            if (null != list) {
                jsonObject.put("sel_index_phrase", list);
            }
        }

        public List<? extends List> getIndexPhrase() {
            Object phrases = jsonObject.get("sel_index_phrase");
            if (null != phrases) {
                return (List<? extends List>) phrases;
            } else {
                return new ArrayList();
            }
        }
    }
}
