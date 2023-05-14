package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import junit.framework.TestCase;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by wenshao on 11/03/2017.
 */
public class Issue1023 extends TestCase {
    public void test_for_issue() throws Exception {
        Date now = new Date();

        GregorianCalendar gregorianCalendar = (GregorianCalendar) GregorianCalendar.getInstance();
        gregorianCalendar.setTime(now);

        XMLGregorianCalendar calendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);

        String jsonString = JSON.toJSONString(calendar);
        // success
        calendar = JSON.parseObject(jsonString, XMLGregorianCalendar.class);

        Object toJSON1 = JSON.toJSON(calendar); // debug看到是 Long 类型
        // 所以这里会报错：
        // error: java.lang.ClassCastException: java.lang.Long cannot be cast to com.alibaba.fastjson.JSONObject
        //JSONObject jsonObject = (JSONObject) JSON.toJSON(calendar);
        // 所以 这里肯定会报错， 因为 jsonObject 不是JSONObject类型
        //calendar = jsonObject.toJavaObject(XMLGregorianCalendar.class);

        List<XMLGregorianCalendar> calendarList = new ArrayList<XMLGregorianCalendar>();
        calendarList.add(calendar);
        calendarList.add(calendar);
        calendarList.add(calendar);

        Object toJSON2 = JSON.toJSON(calendarList); // debug 看到是 JSONArray 类型

        // success： 通过 JSONArray.parseArray 方法可以正确转换
        JSONArray jsonArray = (JSONArray) JSON.toJSON(calendarList);
        jsonString = jsonArray.toJSONString();
        List<XMLGregorianCalendar> calendarList1 = JSONArray.parseArray(jsonString, XMLGregorianCalendar.class);

        // 通过 jsonArray.toJavaList 无法转换
        // error: com.alibaba.fastjson.JSONException: can not cast to : javax.xml.datatype.XMLGregorianCalendar
        List<XMLGregorianCalendar> calendarList2 = jsonArray.toJavaList(XMLGregorianCalendar.class);
        assertNotNull(calendarList2);
        assertEquals(3, calendarList2.size());
    }
}
