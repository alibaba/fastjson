package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

import java.io.Serializable;

/**
 * Created by wenshao on 09/02/2017.
 */
public class Bug_for_xujin extends TestCase {
    public void test_for_xujin() throws Exception {
        String jsonText="{\"module\":{\"auditStatus\":\"PENDING_VERIFICATION\",\"contactId\":\"asdfasdf\",\n\"errorMsg\":\"中国\"},\"success\":true}\n";
        System.out.println(JSON.VERSION);
        ResultDTO resultDTO = (ResultDTO) JSON.parseObject(jsonText, ResultDTO.class);
    }

    public static class ResultDTO<T extends Serializable> implements Serializable {
        private static final long serialVersionUID = 3682481175041925854L;
        private static final String DEFAULT_ERR_CODE = "xin.unknown.error";
        private String errorMsg;
        private String errorCode;
        private boolean success;
        private T module;

        public ResultDTO(String errorCode, String errorMsg, T obj) {
            this.errorCode = errorCode;
            this.errorMsg = errorMsg;
            this.success = false;
            this.module = obj;
        }

        public ResultDTO() {
            buildSuccessResult();
        }

        public ResultDTO(T obj) {
            this.success = true;
            this.module = obj;
        }

        public static <T extends Serializable> ResultDTO<T> buildSuccessResult() {
            return new ResultDTO((Serializable)null);
        }

        public static <T extends Serializable> ResultDTO<T> buildSuccessResult(T obj) {
            return new ResultDTO(obj);
        }

        public static <T extends Serializable> ResultDTO<T> buildFailedResult(String errCode, String errMsg, T obj) {
            return new ResultDTO(errCode, errMsg, obj);
        }

        public static <T extends Serializable> ResultDTO<T> buildFailedResult(String errCode, String errMsg) {
            return new ResultDTO(errCode, errMsg, (Serializable)null);
        }

        public static <T extends Serializable> ResultDTO<T> buildFailedResult(String errMsg) {
            return new ResultDTO("xin.unknown.error", errMsg, (Serializable)null);
        }

        public String getErrorMsg() {
            return this.errorMsg;
        }

        public void setErrorMsg(String errorMsg) {
            this.errorMsg = errorMsg;
        }

        public String getErrorCode() {
            return this.errorCode;
        }

        public void setErrorCode(String errorCode) {
            this.errorCode = errorCode;
        }

        public boolean isSuccess() {
            return this.success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public T getModule() {
            return this.module;
        }

        public void setModule(T module) {
            this.module = module;
        }

        public String toJsonString() {
            return JSON.toJSONString(this);
        }
    }
}
