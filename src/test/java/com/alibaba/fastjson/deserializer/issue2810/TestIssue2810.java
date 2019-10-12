package com.alibaba.fastjson.deserializer.issue2810;

import com.alibaba.fastjson.JSON;
import org.junit.Assert;
import org.junit.Test;



public class TestIssue2810 {

    @Test
    public void testIssue2810(){
        String s = "{1\"local\":{1\"id\":376648,1\"bit\":45,1\"frame\":151},1\"remote\":[1{1\"id\":814457,1\"bit\":58,1\"frame\":8,1\"w\":240,1\"h\":1801},1{1\"id\":1317201,1\"bit\":59,1\"frame\":10,1\"w\":240,1\"h\":1801},1{1\"id\":1337865,1\"bit\":61,1\"frame\":10,1\"w\":240,1\"h\":1801},1{1\"id\":1462739,1\"bit\":57,1\"frame\":8,1\"w\":240,1\"h\":1801}1]1}";
        boolean b = JSON.isValidObject(s);
        Assert.assertFalse(b);
    }

}
