package com.alibaba.json.bvt.date;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import junit.framework.TestCase;
import org.junit.Assert;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.GregorianCalendar;

public class XMLGregorianCalendarTest extends TestCase {
    public void test_for_issue() throws Exception {
        GregorianCalendar gregorianCalendar = (GregorianCalendar) GregorianCalendar.getInstance();

        XMLGregorianCalendar calendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
        
        String text = JSON.toJSONString(calendar);
        Assert.assertEquals(Long.toString(gregorianCalendar.getTimeInMillis()), text);

        XMLGregorianCalendar calendar1 = JSON.parseObject(text, XMLGregorianCalendar.class);

        assertEquals(calendar.toGregorianCalendar().getTimeInMillis(), calendar1.toGregorianCalendar().getTimeInMillis());

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("calendar", calendar);

        String json = JSON.toJSONString(jsonObject);

        Model model = JSON.parseObject(json).toJavaObject(Model.class);

        assertEquals(calendar.toGregorianCalendar().getTimeInMillis(), model.calendar.toGregorianCalendar().getTimeInMillis());
    }

    public static class Model {
        public XMLGregorianCalendar calendar;
    }
}
