package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import junit.framework.TestCase;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wenshao on 2016/10/19.
 */
public class Issue869 extends TestCase {
    public void test_for_issue() throws Exception {
        List<DoublePoint> doublePointList = new ArrayList<DoublePoint>();
        {
            DoublePoint doublePoint = new DoublePoint();
            doublePoint.startPoint = new Point(22, 35);
            doublePoint.endPoint = doublePoint.startPoint;
            doublePointList.add(doublePoint);
        }
        {
            DoublePoint doublePoint = new DoublePoint();
            doublePoint.startPoint = new Point(16, 18);
            doublePoint.endPoint = doublePoint.startPoint;
            doublePointList.add(doublePoint);
        }

        String json = JSON.toJSONString(doublePointList);
        assertEquals("[{\"endPoint\":{\"x\":22,\"y\":35},\"startPoint\":{\"x\":22,\"y\":35}},{\"endPoint\":{\"x\":16,\"y\":18},\"startPoint\":{\"x\":16,\"y\":18}}]", json);
    }

    public void test_for_issue_parse() throws Exception {

        String text = "[{\"endPoint\":{\"x\":22,\"y\":35},\"startPoint\":{\"$ref\":\"$[0].endPoint\"}},{\"endPoint\":{\"$ref\":\"$[1].startPoint\"},\"startPoint\":{\"x\":16,\"y\":18}}]";
        List<DoublePoint> doublePointList = JSON.parseObject(text, new TypeReference<List<DoublePoint>>(){});
        assertNotNull(doublePointList.get(0));
        assertNotNull(doublePointList.get(1));
        assertSame(doublePointList.get(0).startPoint, doublePointList.get(0).endPoint);
        assertSame(doublePointList.get(1).startPoint, doublePointList.get(1).endPoint);
    }

    public static  class DoublePoint{
        public Point startPoint;
        public Point endPoint;
    }
}
