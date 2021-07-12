package com.alibaba.fastjson.deserializer.issue3752;


import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.deserializer.issue3752.beans.InstantiableWrapper;
import com.alibaba.fastjson.deserializer.issue3752.beans.InterfaceWrapper;
import com.alibaba.fastjson.deserializer.issue3752.beans.UserDefinedCollectionInstance;
import org.junit.Assert;
import org.junit.Test;


/**
 * Issue 3752 test class
 */
public class TestIssue3752 {

    /**
     * Positive Test Scenario: Deserialize a user defined instantiable
     * collection.
     * The user defined collection can create instance directly.
     */
    @Test
    public void testDeserializeUserDefinedInstantiableCollection(){
        String json = "{\"ic\":[\"TestIssue3752\"]}";
        InstantiableWrapper wrapper =JSONObject.parseObject(json, InstantiableWrapper.class);
        Assert.assertEquals(wrapper.getIc().get(0), "TestIssue3752");
    }

    /**
     * Negative Test Scenario: Deserialize a user defined interface
     * collection with a specified instance created at initialization.
     */
    @Test
    public void testDeserializeUserDefinedInterfaceCollection() {
        String json = "{\"collection\":[\"TestIssue3752\"]}";
        InterfaceWrapper wrapper = JSONObject.parseObject(json, InterfaceWrapper.class);
        Assert.assertTrue(wrapper.getCollection() instanceof UserDefinedCollectionInstance);
        Assert.assertEquals(wrapper.getCollection().get(0), "TestIssue3752");
    }

}