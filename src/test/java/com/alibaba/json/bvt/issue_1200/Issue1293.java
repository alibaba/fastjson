package com.alibaba.json.bvt.issue_1200;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;


/**
 * Created by kimmking on 27/06/2017.
 */
public class Issue1293 extends TestCase {

    public void test_for_issue() {
        String data = "{\"idType\":\"123123\",\"userType\":\"134\",\"count\":\"123123\"}";
        {
            Test test = JSON.parseObject(data, Test.class);

            assertNull(test.idType);
            assertNull(test.userType);
        }

        Test test = JSON.parseObject(data, Test.class);
        assertNull(test.idType);
        assertNull(test.userType);

    }

    static class Test{
        private long count;
        private IdType idType;
        private UserType userType;

        public long getCount() {
            return count;
        }

        public void setCount(long count) {
            this.count = count;
        }

        public IdType getIdType() {
            return idType;
        }

        public void setIdType(IdType idType) {
            this.idType = idType;
        }

        public UserType getUserType() {
            return userType;
        }

        public void setUserType(UserType userType) {
            this.userType = userType;
        }
    }

    static enum IdType{
        A,B
    }
    static enum UserType{
        C,D
    }

}
