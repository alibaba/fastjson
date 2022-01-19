package com.alibaba.json.bvt.issue_1800;

import java.time.LocalTime;
import java.util.Date;

import com.alibaba.fastjson.JSON;

import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.json.bvt.issue_3300.Issue3361;
import junit.framework.TestCase;
import springfox.documentation.spring.web.json.Json;

/**
 * @author toretto.huang
 * @date 2022/1/19
 */
public class Issue1858 extends TestCase {

    public void test_for_issue() {
        LocalTime localDate =LocalTime.of(20,30,0);
        String json = JSON.toJSONStringWithLocalTimeFormat(localDate, "HH:mm:ss");
        assertEquals("\"20:30:00\"", json);
        String json2 = JSON.toJSONStringWithLocalTimeFormat(localDate, "HH:mm");
        assertEquals("\"20:30\"", json2);
        LocalTime localDate2 =LocalTime.of(20,30,1);
        String json3 = JSON.toJSONStringWithLocalTimeFormat(localDate2, "HH:mm:ss");
        assertEquals("\"20:30:01\"", json3);
        String json4 = JSON.toJSONStringWithLocalTimeFormat(localDate2, "HH:mm");
        assertEquals("\"20:30\"", json4);
        System.out.println(JSON.toJSONString(localDate, SerializerFeature.UseDefaultLocalTimeFormat));
    }

    public void test_for_issue2() throws Exception {
        VO vo=new VO();
        vo.localTime=LocalTime.of(20,30,00);
        FastJsonConfig config = new FastJsonConfig();
        config.setSerializerFeatures(SerializerFeature.WriteMapNullValue);
        config.setWriteContentLength(false);
        JSON.DEFFAULT_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS";
        config.setDateFormat(JSON.DEFFAULT_DATE_FORMAT);
        config.setLocalTimeFormat("HH:mm:ss");
        String string = JSON.toJSONString(vo,
            config.getSerializeConfig(),
            config.getSerializeFilters(),
            config.getDateFormat(),
            config.getLocalTimeFormat(),
            JSON.DEFAULT_GENERATE_FEATURE,
            config.getSerializerFeatures());
        assertEquals("{\"localTime\":\"20:30:00\"}", string);
    }

    public static class VO{
        public LocalTime localTime;
    }
}
