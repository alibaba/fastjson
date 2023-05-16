package com.alibaba.fastjson.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.JSONScanner;
import com.diffblue.deeptestutils.Reflector;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Locale;

public class Issue3968 {
    /**
     * Test scientific notation in double
     */
    @Test
    public void testDouble() throws Throwable {
        String str1 = "{\"rank\": 2.444444444444444E7}";
        String str2 = "{\"rank\": 2.4E7}";
        JSONObject jo1 = JSON.parseObject(str1);
        JSONObject jo2 = JSON.parseObject(str2);

        // NumberFormat numFormat = new DecimalFormat();

        // public Double parseDouble
        // numFormat = new DecimalFormat("0.#E0");

        Double d = Double.parseDouble(jo1.getString("rank"));
        Float f = Float.parseFloat(jo1.getString("rank"));
        System.out.println(jo1.getString("rank"));
        System.out.println(jo1.getDouble("rank"));
        System.out.println("string: " + jo1.getIntValue("rank"));
        System.out.println(jo1.getFloat("rank"));
        System.out.println(d);
        System.out.println(f);
        // System.out.println(numFormat.format(d));
        // jo1.put("rank", 2.33333E7);
        System.out.println(JSON.toJSONString(jo1)); // {"rank":24444444.44444444}
        System.out.println(JSON.toJSONString(jo2));
        System.out.println(
                JSON.toJSONStringScientificNotation(jo1, "rank")); // {"rank":2.4444444E7}

        Assert.assertEquals("{\"rank\":2.4444444E7}", JSON.toJSONStringScientificNotation(jo1, "rank"));
    }

}

