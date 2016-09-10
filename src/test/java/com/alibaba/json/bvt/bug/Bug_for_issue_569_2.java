package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import junit.framework.TestCase;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by wenshao on 16/9/10.
 */
public class Bug_for_issue_569_2 extends TestCase {
    public void test_for_issue() throws Exception {
        String json = "{\"code\":\"1001\",\"dataResult\":[{\"id\":12345}]}";
        Type type = new TypeReference<ResponseDO<List<User>>>() {}.getType();
        ResponseDO<List<User>> resp = JSON.parseObject(json, type);

        assertEquals("1001", resp.code);

        List<User> users = resp.dataResult;
        assertNotNull(users);
        assertEquals(1, users.size());
        assertEquals(12345, users.get(0).id);
    }

    public static class ResponseDO<T> {
        public String code;
        public T dataResult;
    }

    public static class User {
        public int id;
    }
}
