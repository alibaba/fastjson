package com.alibaba.json.bvt.issue_1300;

import com.alibaba.fastjson.JSON;

import com.alibaba.fastjson.serializer.BeanContext;
import com.alibaba.fastjson.serializer.ContextValueFilter;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.ValueFilter;
import junit.framework.TestCase;
import org.junit.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kimmking on 02/07/2017.
 */
public class Issue1307 extends TestCase {
    ContextValueFilter contextValueFilter = new ContextValueFilter() {
     public Object process(BeanContext beanContext, Object obj, String name, Object value) {
            return "mark-"+value;
        }
    };
    ValueFilter valueFilter = new ValueFilter() {
        public Object process(Object object, String name, Object value) {
            return value;
        }
    };

    @Test
    public void test_context_value_filter_not_effected () {
        List<Object> params = new ArrayList<Object>();
        Map data = new HashMap();
        data.put("name", "ace");
        params.add(data);
        //fail Actual   :[{"name":"ace"}]
        Assert.assertEquals("[{\"name\":\"mark-ace\"}]", JSON.toJSONString(params,
                        new SerializeFilter[]{
                                contextValueFilter
                        })
        );

    }

    @Test
    public void test_context_value_filter_effected() {
        List<Object> params = new ArrayList<Object>();
        Map data = new HashMap();
        data.put("name", "ace");
        params.add(data);
        //success
        Assert.assertEquals("[{\"name\":\"mark-ace\"}]", JSON.toJSONString(params,
                        new SerializeFilter[]{
                                contextValueFilter,
                                valueFilter
                        })
        );
    }
}
