package com.alibaba.json.bvt.serializer;

import com.alibaba.fastjson.JSON;
import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class JSONSerializerTest1 extends TestCase {
    public void test_0 () throws Exception {
        SerializeWriter out = new SerializeWriter();
        JSONSerializer serializer = new JSONSerializer(out);
        
        Assert.assertEquals(0, serializer.getNameFilters().size());
        Assert.assertEquals(0, serializer.getNameFilters().size());
        
        Assert.assertEquals(0, serializer.getValueFilters().size());
        Assert.assertEquals(0, serializer.getValueFilters().size());
        
        Assert.assertEquals(0, serializer.getPropertyFilters().size());
        Assert.assertEquals(0, serializer.getPropertyFilters().size());
        
        serializer.writeWithFormat("123", null);
    }

    public void test_1() throws Exception {
        SerializeWriter out = new SerializeWriter();
        JSONSerializer serializer = new JSONSerializer(out);

        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(2019, Calendar.SEPTEMBER, 5);
        Date date = calendar.getTime();

        String dateFormatPattern = "yyyy/MM/dd";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormatPattern);

        serializer.writeWithFormat(date, dateFormatPattern);

        assertEquals("\"" + sdf.format(date) + "\"", serializer.out.toString());
    }

    public void test_2() throws Exception {
        SerializeWriter out = new SerializeWriter();
        JSONSerializer serializer = new JSONSerializer(out);

        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(2019, Calendar.SEPTEMBER, 5);
        Date date = calendar.getTime();

        String dateFormatPattern = "yyyy.MM.dd";
        String temp = JSON.DEFFAULT_DATE_FORMAT;
        JSON.DEFFAULT_DATE_FORMAT = dateFormatPattern;

        SimpleDateFormat sdf = new SimpleDateFormat(JSON.DEFFAULT_DATE_FORMAT);
        //传入null时调用JSON.DEFFAULT_DATE_FORMAT
        serializer.writeWithFormat(date, null);

        JSON.DEFFAULT_DATE_FORMAT = temp;

        assertEquals("\"" + sdf.format(date) + "\"", serializer.out.toString());
    }
}
