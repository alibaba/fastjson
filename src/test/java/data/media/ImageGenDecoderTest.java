package data.media;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;


public class ImageGenDecoderTest extends TestCase {
    public void test_0() throws Exception {
        ParserConfig config = new ParserConfig();
        config.putDeserializer(Image.class, new ImageGenDecoder(config, Image.class));
        
        String text = "{\"height\":101,\"width\":123}";
        Image image = JSON.parseObject(text, Image.class, config, JSON.DEFAULT_PARSER_FEATURE);
        Assert.assertEquals(123, image.getWidth());
    }
}
