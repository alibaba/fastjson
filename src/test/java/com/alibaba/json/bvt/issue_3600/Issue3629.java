package com.alibaba.json.bvt.issue_3600;

import com.alibaba.fastjson.JSONPath;
import java.util.List;
import junit.framework.TestCase;

public class Issue3629 extends TestCase {
    public void test_for_issue() throws Exception {
        String text1 = "[\n" +
                "    {\n" +
                "        \"author\": \"Nigel Rees\",\n" +
                "        \"category\": \"reference\",\n" +
                "        \"price\": 8.95,\n" +
                "        \"title\": \"Sayings of the Century\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"author\": \"Evelyn Waugh\",\n" +
                "        \"category\": \"fiction\",\n" +
                "        \"price\": 12.99,\n" +
                "        \"title\": \"Sword of Honour\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"author\": \"Herman Melville\",\n" +
                "        \"category\": \"fiction\",\n" +
                "        \"isbn\": \"0-553-21311-3\",\n" +
                "        \"price\": 8.99,\n" +
                "        \"title\": \"Moby Dick\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"author\": \"J. R. R. Tolkien\",\n" +
                "        \"category\": \"fiction\",\n" +
                "        \"isbn\": \"0-395-19395-8\",\n" +
                "        \"price\": 22.99,\n" +
                "        \"title\": \"The Lord of the Rings\"\n" +
                "    }\n" +
                "]";


        List<Object> extract = (List) JSONPath.extract(text1, "$..[?(@.price < 10)]");
        assertEquals(2, extract.size());
    }
}
