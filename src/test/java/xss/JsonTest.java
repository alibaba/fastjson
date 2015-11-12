package xss;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Jintao on 2015/11/3.
 */
public class JsonTest {

    @Test
    public void Test(){
        ParserConfig.getGlobalInstance().setAsmEnable(false);

        TestObj testObj = new TestObj();
        testObj.setA("<script>");
        testObj.setB("<script>");

        String jsonStr = JSON.toJSONString(testObj);

        TestObj obj = JSON.parseObject(jsonStr, TestObj.class);

        Assert.assertNotNull(obj);
    }
}
