package com.alibaba.fastjson;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

public class Issue2418 {
    @Rule
    public ExpectedException thrownError = ExpectedException.none();

    @Test
    public void testIssue2418Long(){
        String text = "{\"value\":3}";
        TestLong tl = JSON.parseObject(text, TestLong.class);
        assertEquals(3, tl.getValue());
    }

    @Test
    public void testIssue2418LongError(){
        String text = "{\"value\":3.1}";
        thrownError.expect(JSONException.class);
        thrownError.expectMessage("error, casting decimal to long");
        TestLong tl = JSON.parseObject(text, TestLong.class);
    }

    @Test
    public void testIssue2418Int(){
        String text = "{\"value\":3}";
        TestInt ti = JSON.parseObject(text, TestInt.class);
        assertEquals(3, ti.getValue());
    }

    @Test
    public void testIssue2418IntError(){
        String text = "{\"value\":3.1}";
        thrownError.expect(JSONException.class);
        thrownError.expectMessage("error, casting decimal to int");
        TestInt ti = JSON.parseObject(text, TestInt.class);
    }
}

class TestLong {
    private long value;

    public long getValue(){
        return value;
    }

    public void setValue(long value){
        this.value = value;
    }
}

class TestInt {
    private int value;

    public int getValue(){
        return value;
    }

    public void setValue(int value){
        this.value = value;
    }
}