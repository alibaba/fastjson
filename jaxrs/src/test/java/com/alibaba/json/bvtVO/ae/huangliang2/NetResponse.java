package com.alibaba.json.bvtVO.ae.huangliang2;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by huangliang on 17/5/9.
 */

public class NetResponse {
    public Head head;

    public static class Head {
        public String message;
        public String code;
        public String serverErrorCode;
        public long serverTime;
        public String traceId;
        public String op;
        public String ab;
    }

    public JSONObject body;
}
