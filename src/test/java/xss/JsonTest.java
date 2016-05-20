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
        ParserConfig.getGlobalInstance().setAsmEnable(true);

        TestObj testObj = new TestObj();

        testObj.setScript("\74script>alert('xxx')</script>");

        testObj.setB("<script>alert('xxx')</script>");

        String jsonStr = JSON.toJSONString(testObj);

        TestObj obj = JSON.parseObject(jsonStr, TestObj.class);

        Assert.assertNotNull(obj);
    }
}
