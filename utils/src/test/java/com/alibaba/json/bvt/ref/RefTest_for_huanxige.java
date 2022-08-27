package com.alibaba.json.bvt.ref;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

import java.io.Serializable;

/**
 * Created by wenshao on 08/02/2017.
 */
public class RefTest_for_huanxige extends TestCase {
    public void test_for_ref() throws Exception {
//字符串通过其它对象序列化而来，当中涉及循环引用，因此存在$ref
        String jsonStr="{\"displayName\":\"灰度发布\",\"id\":221," +
                "\"name\":\"灰度\",\"processInsId\":48,\"processInstance\":{\"$ref\":\"$" +
                ".lastSubProcessInstence.parentProcess\"},\"status\":1,\"success\":true," +
                "\"tail\":true,\"type\":\"gray\"}";
        ProcessNodeInstanceDto a = JSON.parseObject(jsonStr, ProcessNodeInstanceDto.class);//status为空！！！
        assertNotNull(a.status);
        assertEquals(1, a.status.intValue());
    }

    public static class ProcessNodeInstanceDto implements Serializable {
        private Long id;
        private Long processInsId;
        private String name;
        private String displayName;
        private Integer status;
        private String type;
        private Boolean success;
        private Boolean tail;

        public Long getId() {
            return id;
        }
        public void setId(Long id) {
            this.id = id;
        }
        public Long getProcessInsId() {
            return processInsId;
        }
        public void setProcessInsId(Long processInsId) {
            this.processInsId = processInsId;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public String getDisplayName() {
            return displayName;
        }
        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }
        public Integer getStatus() {
            return status;
        }
        public void setStatus(Integer status) {
            this.status = status;
        }
        public String getType() {
            return type;
        }
        public void setType(String type) {
            this.type = type;
        }
        public Boolean getSuccess() {
            return success;
        }
        public void setSuccess(Boolean success) {
            this.success = success;
        }
        public Boolean getTail() {
            return tail;
        }
        public void setTail(Boolean tail) {
            this.tail = tail;
        }
    }
}
