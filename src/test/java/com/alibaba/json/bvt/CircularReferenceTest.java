package com.alibaba.json.bvt;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

import junit.framework.TestCase;

import com.alibaba.json.test.entity.case2.Category;

public class CircularReferenceTest extends TestCase {

    public void test_0() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream objectOut = new ObjectOutputStream(out);

        Category p = new Category();
        p.setId(1);
        p.setName("root");
        {
            Category child = new Category();
            child.setId(2);
            child.setName("child");
            p.getChildren().add(child);
            child.setParent(p);
        }
        objectOut.writeObject(p);
    }

}
