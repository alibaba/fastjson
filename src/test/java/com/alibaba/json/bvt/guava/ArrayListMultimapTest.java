package com.alibaba.json.bvt.guava;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.TreeMultimap;
import com.google.common.primitives.Ints;
import junit.framework.TestCase;

/**
 * Created by wenshao on 15/01/2017.
 */
public class ArrayListMultimapTest extends TestCase {
    public void test_for_multimap() throws Exception {
        ArrayListMultimap<String, Integer> multimap = ArrayListMultimap.create();
        multimap.putAll("b", Ints.asList(2, 4, 6));
        multimap.putAll("a", Ints.asList(4, 2, 1));
        multimap.putAll("c", Ints.asList(2, 5, 3));


        String json = JSON.toJSONString(multimap);
        assertEquals("{\"a\":[4,2,1],\"b\":[2,4,6],\"c\":[2,5,3]}", json);

        TreeMultimap treeMultimap = TreeMultimap.create(multimap);
        String json2 = JSON.toJSONString(treeMultimap);
        assertEquals("{\"a\":[1,2,4],\"b\":[2,4,6],\"c\":[2,3,5]}", json2);
    }
}
