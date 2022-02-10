package SWE261P;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.issues3601.TestEntity;
import com.alibaba.fastjson.serializer.issues3601.TestEnum;
import org.junit.Assert;
import org.junit.Test;

public class SWE261P_A1_Test1 {
    @Test
    public void serializationTest() {
        TestEntity testEntity = new TestEntity();
        testEntity.setTestName("ganyu");
        testEntity.setTestEnum(TestEnum.test1);
        String json = JSON.toJSONString(testEntity);
        System.out.println(json);
        Assert.assertEquals("{\"testEnum\":\"test1\",\"testName\":\"ganyu\"}", json);
    }
}
