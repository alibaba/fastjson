package com.alibaba.json.bvt.issue_3600;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import junit.framework.TestCase;
import lombok.Data;

public class Issue3682 extends TestCase {
    public void test_for_issue() throws Exception {
        Cid cid = JSON.parseObject(SOURCE, Cid.class);
        System.out.println(cid);
    }

    @Data
    static public class Cid {

        @JSONField(name = "/")
        private String hash;

    }

    static final String SOURCE = "{\n" +
            "    \"jsonrpc\": \"2.0\",\n" +
            "    \"result\": {\n" +
            "        \"Version\": 0,\n" +
            "        \"To\": \"t1iceld4fv44xgjqfcx5lwz45pubheu3c7c2nmlua\",\n" +
            "        \"From\": \"t152xual7ze57jnnioucuv4lmtxarewtzhkqojboy\",\n" +
            "        \"Nonce\": 4,\n" +
            "        \"Value\": \"9999999938462317355\",\n" +
            "        \"GasLimit\": 609960,\n" +
            "        \"GasFeeCap\": \"101083\",\n" +
            "        \"GasPremium\": \"100029\",\n" +
            "        \"Method\": 0,\n" +
            "        \"Params\": null,\n" +
            "        \"CID\": {\n" +
            "            \"/\": \"bafy2bzacedgpr5pmkvu4rkq26uv4hidpfrn3gdvtgkp3hpxss3bgmodrgqtk6\"\n" +
            "        }\n" +
            "    },\n" +
            "    \"id\": 1\n" +
            "}";
}
