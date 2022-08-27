package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wenshao on 11/02/2017.
 */
public class Issue1017 extends TestCase {
    public void test_for_issue() throws Exception {
        String json = "{\"pictureList\":[\"http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000\",\"http://common.cnblogs.com/images/icon_weibo_24.png\"]}";

        User user = JSON.parseObject(json, User.class);
        assertNotNull(user.pictureList);
        assertEquals(2, user.pictureList.size());
        assertEquals("http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000", user.pictureList.get(0));
        assertEquals("http://common.cnblogs.com/images/icon_weibo_24.png", user.pictureList.get(1));
    }

    public static class User implements Serializable {
        private List<String> pictureList;
        public List<String> getPictureList() {
            return pictureList;
        }
        public User setPictureList(List<String> pictureList) {
            this.pictureList = pictureList;
            return this;
        }
    }
}
