package com.alibaba.fastjson;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

public class Issue2418Array {
    @Rule
    public ExpectedException thrownErrorArray = ExpectedException.none();

    @Test
    public void testIssue2418IntArray(){
        String text = "{\"value\":[3]}";
        TestIntArray tia = JSON.parseObject(text, TestIntArray.class);
        int[] expected = {3};
        assertArrayEquals(expected, tia.getValue());
    }

    @Test
    public void testIssue2418IntArrayError(){
        String text = "{\"value\":[3.1]}";
        thrownErrorArray.expect(JSONException.class);
        thrownErrorArray.expectMessage("error, casting decimal to int");
        TestIntArray tia = JSON.parseObject(text, TestIntArray.class);
    }
}

class TestIntArray {
    private int[] value;

    public int[] getValue(){
        return value;
    }

    public void setValue(int[] value){
        this.value = value;
    }
}