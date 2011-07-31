package com.alibaba.json.test.bvt.serializer.jmx;

import java.util.HashMap;
import java.util.Map;

import javax.management.openmbean.CompositeDataSupport;
import javax.management.openmbean.CompositeType;
import javax.management.openmbean.OpenType;
import javax.management.openmbean.SimpleType;
import javax.management.openmbean.TabularDataSupport;
import javax.management.openmbean.TabularType;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class TabularDataTest extends TestCase {

    public void test_tabularData() throws Exception {
        String[] itemNames = { "id", "name" };
        String[] itemDescriptions = itemNames;
        OpenType<?>[] itemTypes = new OpenType[] { SimpleType.INTEGER, SimpleType.STRING };
        CompositeType compositeType = new CompositeType("TypeName", "description", itemNames, itemDescriptions, itemTypes);

        TabularType tabularType = new TabularType("Table", "Table", compositeType, itemNames);
        TabularDataSupport data = new TabularDataSupport(tabularType);
        {
            Map<String, Object> items = new HashMap<String, Object>();
            items.put("id", 123);
            items.put("name", "加大伟");

            CompositeDataSupport row = new CompositeDataSupport(compositeType, items);
            data.put(row);
        }
        {
            Map<String, Object> items = new HashMap<String, Object>();
            items.put("id", 234);
            items.put("name", "加大爵");

            CompositeDataSupport row = new CompositeDataSupport(compositeType, items);
            data.put(row);
        }

        JSONObject json = JSON.parseObject(JSON.toJSONString(data));
        JSONArray columns = json.getJSONArray("columns");
        Assert.assertEquals(2, columns.size());
        Assert.assertEquals("id", columns.get(0));
        Assert.assertEquals("name", columns.get(1));

        JSONArray rows = json.getJSONArray("rows");
        Assert.assertEquals(2, rows.size());

        JSONArray row0 = (JSONArray) rows.get(0);
        Assert.assertEquals(2, row0.size());
        Assert.assertEquals(123, row0.get(0));
        Assert.assertEquals("加大伟", row0.get(1));

        JSONArray row1 = (JSONArray) rows.get(1);
        Assert.assertEquals(2, row1.size());
        Assert.assertEquals(234, row1.get(0));
        Assert.assertEquals("加大爵", row1.get(1));

        System.out.println(JSON.toJSONString(data));
    }
}
