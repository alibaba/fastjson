package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wenshao on 15/06/2017.
 */
public class Bug_for_luogongwu extends TestCase {

    public void test_for_issue() throws Exception {
        List<IflowItemImage> imageList = new ArrayList<IflowItemImage>();
        IflowItemImage image = new IflowItemImage();
        image.id = "72c7275c6b";
        imageList.add(image);

        imageList = new ArrayList();
        image = new IflowItemImage();
        image.id = "72c7275c6c";
        imageList.add(image);

        // force ASM
        boolean asm = SerializeConfig.globalInstance.isAsmEnable();
        SerializeConfig.globalInstance.setAsmEnable(true);

        // Test ASM
        Foo foo = new Foo();
        foo.thumbnails = imageList;

        String jsonString = JSON.toJSONString(foo);
        System.out.println(jsonString);

        Foo foo1 = JSON.parseObject(jsonString, Foo.class);

        assertEquals(1, foo1.thumbnails.size());
        assertNotNull(foo1.thumbnails.get(0));
        assertSame(foo1.getThumbnail(), foo1.thumbnails.get(0));


        // test Not ASM
        SerializeConfig.globalInstance.setAsmEnable(false);
        FooNotAsm fooNotAsm = new FooNotAsm();
        fooNotAsm.thumbnails = imageList;

        jsonString = JSON.toJSONString(foo);
        System.out.println(jsonString);

        FooNotAsm fooNotAsm1 = JSON.parseObject(jsonString, FooNotAsm.class);

        assertEquals(1, fooNotAsm1.thumbnails.size());
        assertNotNull(fooNotAsm1.thumbnails.get(0));
        assertSame(fooNotAsm1.getThumbnail(), fooNotAsm1.thumbnails.get(0));

        // restore
        SerializeConfig.globalInstance.setAsmEnable(asm);
    }

    @JSONType(asm=false)
    public static class FooNotAsm {
        @JSONField(serialzeFeatures = SerializerFeature.DisableCircularReferenceDetect)
        public List<IflowItemImage> thumbnails;

        public IflowItemImage getThumbnail() {
            return thumbnails != null && thumbnails.size() > 0 ? thumbnails.get(0) : null;
        }
    }

    public static class Foo {
        @JSONField(serialzeFeatures = SerializerFeature.DisableCircularReferenceDetect)
        public List<IflowItemImage> thumbnails;

        public IflowItemImage getThumbnail() {
            return thumbnails != null && thumbnails.size() > 0 ? thumbnails.get(0) : null;
        }
    }

    public static class IflowItemImage {
        public String id;
    }
}
