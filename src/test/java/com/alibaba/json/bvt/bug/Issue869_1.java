package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wenshao on 2016/11/13.
 */
public class Issue869_1 extends TestCase {
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
        assertEquals("[{\"endPoint\":{\"x\":22,\"y\":35},\"startPoint\":{\"$ref\":\"$[0].endPoint\"}},{\"endPoint\":{\"x\":16,\"y\":18},\"startPoint\":{\"$ref\":\"$[1].endPoint\"}}]", json);
    }

    public void test_for_issue_parse() throws Exception {

        String text = "[{\"endPoint\":{\"x\":22,\"y\":35},\"startPoint\":{\"$ref\":\"$[0].endPoint\"}},{\"endPoint\":{\"$ref\":\"$[1].startPoint\"},\"startPoint\":{\"x\":16,\"y\":18}}]";
        List<Issue869.DoublePoint> doublePointList = JSON.parseObject(text, new TypeReference<List<Issue869.DoublePoint>>(){});
        assertNotNull(doublePointList.get(0));
        assertNotNull(doublePointList.get(1));
        assertSame(doublePointList.get(0).startPoint, doublePointList.get(0).endPoint);
        assertSame(doublePointList.get(1).startPoint, doublePointList.get(1).endPoint);
    }

    public static class DoublePoint{
        public Point startPoint;
        public Point endPoint;
    }

    public static class Point {
        public int x;
        public int y;
        public Properties properties;

        public Point() {

        }

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public static class Properties{
        public String id;
        public String title;
    }


}
