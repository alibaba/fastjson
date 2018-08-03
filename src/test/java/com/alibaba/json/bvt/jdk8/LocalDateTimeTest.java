package com.alibaba.json.bvt.jdk8;

import java.time.LocalDateTime;

import com.alibaba.fastjson.serializer.SerializerFeature;
import org.junit.Assert;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class LocalDateTimeTest extends TestCase {

    public void test_for_issue() throws Exception {
        VO vo = new VO();
        vo.setDate(LocalDateTime.now().minusNanos(10L));
        
        String text = JSON.toJSONString(vo);
        
        VO vo1 = JSON.parseObject(text, VO.class);
        
        Assert.assertEquals(JSON.toJSONString(vo.getDate()), JSON.toJSONString(vo1.getDate()));
    }

    /**
     * 方法描述: 测试LocalDateTime 转化时间戳等 操作
     *  问题点1、 LocalDateTime 进来的值无法确定其时区,所以此处统一按着系统时区走。
     *  问题点2、 如果设置 SerializerFeature.WriteDateUseDateFormat 时按着 "yyyy-MM-dd HH:mm:ss" 进行格式化
     *  问题点3：  如果设置 SerializerFeature.UseISO8601DateFormat 时按着ISO8601的标准 "yyyy-MM-dd'T'HH:mm:ss"进行格式化
     *  问题点4:
     *      1)格式化LocalDateTime时， 默认格式成 时间戳格式，
     *      2)如设置WriteDateUseDateFormat 按 "yyyy-MM-dd HH:mm:ss" 进行格式化
     *      3)如设置UseISO8601DateFormat 按ISO8601的标准 "yyyy-MM-dd'T'HH:mm:ss"进行格式化
     *      4)如设置WriteDateUseDateFormat、UseISO8601DateFormat 同时设置,则按ISO8601的标准 "yyyy-MM-dd'T'HH:mm:ss"进行格式化
     * @author wuqiong  2017/11/22 15:08
     */
    public void test_toJsonString_ofLong()throws Exception {
        VO vo = new VO();
        vo.setDate(LocalDateTime.now());

        VO vo1 = JSON.parseObject("{\"date\":1511334591189}", VO.class);

        String text2 = JSON.toJSONString(vo, SerializerFeature.WriteDateUseDateFormat);
        System.out.println(text2);//{"date":"2017-11-22 15:09:51"}
        VO vo2 = JSON.parseObject(text2, VO.class);

        String text3 = JSON.toJSONString(vo, SerializerFeature.UseISO8601DateFormat);
        System.out.println(text3);//{"date":"2017-11-22T15:09:51"}
        VO vo3 = JSON.parseObject(text3, VO.class);

        String text4 = JSON.toJSONString(vo, SerializerFeature.UseISO8601DateFormat, SerializerFeature.WriteDateUseDateFormat);
        System.out.println(text4);//{"date":"2017-11-22T15:09:51"}
        VO vo4 = JSON.parseObject(text4, VO.class);
    }

    public  void test_for_issue_1() throws Exception {
        String text = "{\"date\":\"2018-08-03 22:38:33.145\"}";
        VO vo1 = JSON.parseObject(text, VO.class);
        assertNotNull(vo1.date);
    }

    public static class VO {

        private LocalDateTime date;

        public LocalDateTime getDate() {
            return date;
        }

        public void setDate(LocalDateTime date) {
            this.date = date;
        }

    }
}
