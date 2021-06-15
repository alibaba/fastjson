package com.alibaba.fastjson.deserializer.issue3804;
import com.alibaba.fastjson.JSONValidator;
import org.junit.Test;

public class TestIssue3804 {
    @Test
    public void testIssue3804() {
        String textResponse="{\"error\":false,\"code\":0}";
        JSONValidator validator = JSONValidator.from(textResponse);
        if (validator.validate() && validator.getType() == JSONValidator.Type.Object) {
            System.out.println("Yes, it is Object");
        } else {
            System.out.println("No, it is not Object");
        }
    }
}
