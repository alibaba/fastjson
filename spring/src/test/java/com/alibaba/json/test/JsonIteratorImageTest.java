package com.alibaba.json.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexerBase;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.jsoniter.JsonIterator;
import com.jsoniter.spi.TypeLiteral;
import junit.framework.TestCase;

import java.lang.reflect.Type;

/**
 * Created by wenshao on 27/12/2016.
 */
public class JsonIteratorImageTest extends TestCase {
    private String input = "{\"bitrate\":262144,\"duration\":18000000,\"format\":\"video/mpg4\",\"height\":480,\"persons\":[\"Bill Gates\",\"Steve Jobs\"],\"player\":\"JAVA\",\"size\":58982400,\"title\":\"Javaone Keynote\",\"uri\":\"http://javaone.com/keynote.mpg\",\"width\":640}";
    private byte[] inputBytes = input.getBytes();
    private TypeLiteral<Model> modelTypeLiteral; // this is thread-safe can reused
    private JsonIterator iter;

    private int COUNT = 1000 * 1000 * 1;

    protected void setUp() throws Exception {
        inputBytes = input.getBytes();
        iter = new JsonIterator();
        modelTypeLiteral = new TypeLiteral<Model>() {
        };
    }

    public void test_for_iterator() throws Exception {
        iter.reset(inputBytes);
        Model m2 = iter.read(modelTypeLiteral);

        fastjson();
        for (int i = 0; i < 5; ++i) {
            long startMillis = System.currentTimeMillis();
            fastjson();
            long millis = System.currentTimeMillis() - startMillis;
            System.out.println("fastjson : " + millis);
        }

//        jsoniterator();
//        for (int i = 0; i < 5; ++i) {
//            long startMillis = System.currentTimeMillis();
//            jsoniterator();
//            long millis = System.currentTimeMillis() - startMillis;
//            System.out.println("jsoniterator : " + millis);
//        }
    }

    private void jsoniterator() throws java.io.IOException {
        for (int i = 0; i < COUNT; ++i){
            iter.reset(inputBytes);
            Model model2 = iter.read(modelTypeLiteral);
        }
    }

    private void fastjson() throws java.io.IOException {
        for (int i = 0; i < COUNT; ++i){
            Model model2 = JSON.parseObject(input, Model.class);
        }
    }

    public static class Model {
        public int id;
        public String name;
    }

    public static Object decode_(JsonIterator iter) throws java.io.IOException {
        if (iter.readNull()) {
            com.jsoniter.CodegenAccess.resetExistingObject(iter);
            return null;
        }
        JsonIteratorImageTest.Model obj = (com.jsoniter.CodegenAccess.existingObject(iter) == null ? new JsonIteratorImageTest.Model() : (JsonIteratorImageTest.Model)com.jsoniter.CodegenAccess.resetExistingObject(iter));
        if (!com.jsoniter.CodegenAccess.readObjectStart(iter)) { return obj; }
        switch (com.jsoniter.CodegenAccess.readObjectFieldAsHash(iter)) {
            case 926444256:
                obj.id = (int)iter.readInt();
                break;
            case -1925595674:
                obj.name = (String)iter.readString();
                break;
            default:
                iter.skip();
        }
        while (com.jsoniter.CodegenAccess.nextToken(iter) == ',') {
            switch (com.jsoniter.CodegenAccess.readObjectFieldAsHash(iter)) {
                case 926444256:
                    obj.id = (int)iter.readInt();
                    continue;
                case -1925595674:
                    obj.name = (String)iter.readString();
                    continue;
            }
            iter.skip();
        }
        return obj;
    }


}
