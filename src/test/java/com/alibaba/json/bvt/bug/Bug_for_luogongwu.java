package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wenshao on 15/06/2017.
 */
public class Bug_for_luogongwu extends TestCase {
    public void test_for_issue() throws Exception {
        Foo foo = new Foo();
        List<IflowItemImage> imageList = new ArrayList<IflowItemImage>();
        IflowItemImage image = new IflowItemImage();
        image.id = "72c7275c6c";
        image.localUrl = "file:///data/user/0/com.uc.iflow.ark/cache/img_cache/offline_1ab359f34f78f28ff6fa2bf09c1492ea.jpg";
        image.optimalHeight = 299;
        image.optimalWidth = 0;
        image.original_save_url = "offline@!@http://hl-img.peco.uodoo.com/hubble/app/sm/ddc78ebbc17a5145b2357a6a65fa16be.jpg";
        image.title = "Manisha Kumari";
        image.type = "jpg";
        image.url = "offline@!@http://hl-img.peco.uodoo.com/hubble/app/sm/ddc78ebbc17a5145b2357a6a65fa16be.jpg;,,jpg;3,480x";
        imageList.add(image);
        //、foo.images = imageList;

        imageList = new ArrayList();
        image = new IflowItemImage();
        image.id = "72c7275c6c";
        image.localUrl = "file:///data/user/0/com.uc.iflow.ark/cache/img_cache/offline_23ade9680a67359bdc067667aad94ff5.jpg";
        image.optimalHeight = 299;
        image.optimalWidth = 480;
        image.original_save_url = "offline@!@http://hl-img.peco.uodoo.com/hubble/app/sm/ddc78ebbc17a5145b2357a6a65fa16be.jpg";
        image.title = "";
        image.type = "JPG";
        image.url = "offline@!@http://hl-img.peco.uodoo.com/hubble/app/sm/ddc78ebbc17a5145b2357a6a65fa16be.jpg;,,JPG;3,208x";
        imageList.add(image);
        foo.thumbnails = imageList;

        String jsonString = JSON.toJSONString(foo);
        System.out.println(jsonString);

        Foo foo1 = JSON.parseObject(jsonString, Foo.class);//fool的thumbnails反序列化不出来

        assertEquals(1, foo1.thumbnails.size());
        assertNotNull(foo1.thumbnails.get(0));
        assertSame(foo1.getThumbnail(), foo1.thumbnails.get(0));
    }

    public static class Foo {
        public List<IflowItemImage> thumbnails;
        public List<IflowItemImage> images;

        @JSONField(serialzeFeatures = SerializerFeature.DisableCircularReferenceDetect)
        public IflowItemImage getThumbnail() {
            return thumbnails != null && thumbnails.size() > 0 ? thumbnails.get(0) : null;
        }
    }

    public static class IflowItemImage {

        public String id;

        public String title;

        public String url;

        public String type;

        public int optimalWidth;

        public int optimalHeight;

        public String original_save_url;

        public String phash;
        // FIXME: 17/6/9 客户端则使用的字段，预读功能中下载图片的本地url
        public String localUrl;
    }
}
