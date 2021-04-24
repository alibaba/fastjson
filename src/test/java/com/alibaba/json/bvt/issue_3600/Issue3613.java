package com.alibaba.json.bvt.issue_3600;

import com.alibaba.fastjson.JSONPath;
import org.junit.Assert;
import org.junit.Test;

public class Issue3613 {
    private static final String long_json_str1 = "{\n" +
            "   \"soap:Envelope\":{\n" +
            "      \"-xmlns:xsi\":\"http://www.w3.org/2001/XMLSchema-instance\",\n" +
            "      \"-xmlns:soap\":\"http://schemas.xmlsoap.org/soap/envelope/\",\n" +
            "      \"-xmlns:xsd\":\"http://www.w3.org/2001/XMLSchema\",\n" +
            "      \"soap:Body\":{\n" +
            "         \"getCountryCityByIpResponse\":{\n" +
            "            \"-xmlns\":\"http://WebXml.com.cn/\",\n" +
            "            \"getCountryCityByIpResult\":{\n" +
            "               \"string\":[\n" +
            "                  \"30.40.202.23\",\n" +
            "                  \"美国 俄亥俄州哥伦布市国防部网络信息中心\"\n" +
            "               ]\n" +
            "            }\n" +
            "         }\n" +
            "      }\n" +
            "   }\n" +
            "}";
    private static final String long_json_str1_key_without_quota = "{\n" +
            "   \"soap_Envelope\":{\n" +
            "      \"-xmlns_xsi\":\"http://www.w3.org/2001/XMLSchema-instance\",\n" +
            "      \"-xmlns_soap\":\"http://schemas.xmlsoap.org/soap/envelope/\",\n" +
            "      \"-xmlns_xsd\":\"http://www.w3.org/2001/XMLSchema\",\n" +
            "      \"soap_Body\":{\n" +
            "         \"getCountryCityByIpResponse\":{\n" +
            "            \"-xmlns\":\"http://WebXml.com.cn/\",\n" +
            "            \"getCountryCityByIpResult\":{\n" +
            "               \"string\":[\n" +
            "                  \"30.40.202.23\",\n" +
            "                  \"美国 俄亥俄州哥伦布市国防部网络信息中心\"\n" +
            "               ]\n" +
            "            }\n" +
            "         }\n" +
            "      }\n" +
            "   }\n" +
            "}";
    private static final String json_path1 = "$.soap:Envelope.soap:Body.getCountryCityByIpResponse.getCountryCityByIpResult.string[*]";
    private static final String json_path1_without_quota = "$.soap_Envelope.soap_Body.getCountryCityByIpResponse.getCountryCityByIpResult.string[*]";

    @Test
    public void test_for_issue_1() {
        String result = JSONPath.extract(long_json_str1, json_path1).toString();
        System.out.println(result);
    }

    @Test
    public void test_for_issue_2() {
        Assert.assertNull(JSONPath.extract(long_json_str1, json_path1_without_quota));
    }

    @Test
    public void test_for_issue_3() {
        Assert.assertNull(JSONPath.extract(long_json_str1_key_without_quota, json_path1));
    }

    @Test
    public void test_for_issue_4() {
        String result = JSONPath.extract(long_json_str1_key_without_quota, json_path1_without_quota).toString();
        System.out.println(result);
    }

    private static final String short_json_str1 = "{\n" +
            "   \"soap:Envelope\":{\n" +
            "      \"-xmlns:xsi\":{\n" +
            "           \"a\":\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
            "       }\n" +
            "   }\n" +
            "}";
    private static final String short_json_str1_key_without_quota = "{\n" +
            "   \"soap_Envelope\":{\n" +
            "      \"xmlns,xsi\":{\n" +
            "           \"a\":\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
            "       }\n" +
            "   }\n" +
            "}";
    private static final String json_path2 = "$.soap:Envelope.-xmlns:xsi.a";
    private static final String json_path2_without_quota = "$.soap_Envelope.xmlns,xsi.a";

    @Test
    public void test_for_issue_5() {
        String result = JSONPath.extract(short_json_str1, json_path2).toString();
        System.out.println(result);
    }

    @Test
    public void test_for_issue_6() {
        Assert.assertNull(JSONPath.extract(short_json_str1, json_path2_without_quota));
    }

    @Test
    public void test_for_issue_7() {
        Assert.assertNull(JSONPath.extract(short_json_str1_key_without_quota, json_path2));
    }

    @Test
    public void test_for_issue_8() {
        String result = JSONPath.extract(short_json_str1_key_without_quota, json_path2_without_quota).toString();
        System.out.println(result);
    }
}