package com.alibaba.json.bvt.android;

import android.net.Uri;
import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

/**
 * Created by wenshao on 23/02/2017.
 */
public class UriTest extends TestCase {
    public void test_android_uri() throws Exception {
        Uri uri = Uri.parse("https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/superman/img/logo/bd_logo1_31bdc765.png");
        String str = JSON.toJSONString(uri);

        assertEquals("\"https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/superman/img/logo/bd_logo1_31bdc765.png\"", str);

        Uri uri2 = JSON.parseObject(str, Uri.class);

    }

    public static class Model {

    }
}
