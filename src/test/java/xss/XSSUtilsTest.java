package xss;


import com.alibaba.fastjson.util.XSSUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Zhenwei on 5/20/16.
 */
public class XSSUtilsTest {

    @Test
    public void testEscapeHtml() {
        String input = "\74script>alert('xxx')<\\/script>";
        String result = XSSUtils.escapeHtml(input);
        Assert.assertNotNull(result);
    }

}
