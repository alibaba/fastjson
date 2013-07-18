package data.media.writeAsArray;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.json.test.benchmark.decode.EishayDecodeBytes;

import data.media.Image;
import data.media.Media;
import data.media.MediaContent;

public class TestWriteAsArray extends TestCase {

    public void test_0() throws Exception {
        SerializeConfig config = new SerializeConfig();
        config.put(MediaContent.class, new MediaContentSerializer());
        config.put(Media.class, new MediaSerializer());
        config.put(Image.class, new ImageSerializer());

        String text = JSON.toJSONString(EishayDecodeBytes.instance.getContent(), config);
        System.out.println(text);
        
        ParserConfig parserConfig = new ParserConfig();
        parserConfig.putDeserializer(MediaContent.class, new MediaContentDeserializer());
        parserConfig.putDeserializer(Media.class, new MediaDeserializer());
        parserConfig.putDeserializer(Image.class, new ImageDeserializer());
        
        JSON.parseObject(text, MediaContent.class, parserConfig, JSON.DEFAULT_PARSER_FEATURE);
    }
}
