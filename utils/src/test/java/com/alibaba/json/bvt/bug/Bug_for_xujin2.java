package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.ValueFilter;
import junit.framework.TestCase;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by wenshao on 10/02/2017.
 */
public class Bug_for_xujin2 extends TestCase {
    public void test_for_bug() throws Exception {
        ContactTemplateParam param = new ContactTemplateParam();
        param.setAuditStatus(AuditStatusType.AUDIT_FAILURE);

        String json = JSON.toJSONString(param, new SerializeFilter[] { new IntEnumFilter("auditStatus") });
        assertEquals("{\"auditStatus\":0}", json);
    }

    public static class IntEnumFilter implements ValueFilter {
        private Set<String> needMaskFileds = new HashSet();

        public IntEnumFilter() {
        }

        public IntEnumFilter(String... fileds) {
            if(fileds != null) {
                String[] arr$ = fileds;
                int len$ = fileds.length;

                for(int i$ = 0; i$ < len$; ++i$) {
                    String filed = arr$[i$];
                    this.needMaskFileds.add(filed);
                }
            }

        }

        public Object process(Object object, String name, Object value) {
            return value == null?value:(this.needMaskFileds.contains(name) && value instanceof IntEnum ?Integer.valueOf(((IntEnum)value).getCode()):value);
        }
    }

    public static class ContactTemplateParam implements Serializable {

        private static final long serialVersionUID = 1L;



        public ContactTemplateParam() {
            // TODO Auto-generated constructor stub
        }

        /** 审核状态 **/
        private AuditStatusType auditStatus;



        public AuditStatusType getAuditStatus() {
            return auditStatus;
        }

        public void setAuditStatus(AuditStatusType auditStatus) {
            this.auditStatus = auditStatus;
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this);
        }
    }

    public static enum AuditStatusType implements IntEnum<AuditStatusType> {
        AUDIT_FAILURE(0, "审核失败", "FAILED"),
        AUDIT_SUCCESS(1, "成功", "SUCCEED"),
        AUDIT_NO_SUBMIT(2, "未实名认证", "NONAUDIT"),
        AUDIT_SUBMIT(3, "审核中", "AUDITING");

        private int code;
        private String desc;
        private String enCode;

        private AuditStatusType(int code) {
            this.code = code;
        }

        private AuditStatusType(int code, String desc, String enCode) {
            this.code = code;
            this.desc = desc;
            this.enCode = enCode;
        }

        public static AuditStatusType valuesOf(String enCode) {
            AuditStatusType[] arr$ = values();
            int len$ = arr$.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                AuditStatusType temp = arr$[i$];
                if(temp.getEnCode().equals(enCode)) {
                    return temp;
                }
            }

            return null;
        }

        public String getDesc() {
            return this.desc;
        }

        public String getEnCode() {
            return this.enCode;
        }

        public int getCode() {
            return this.code;
        }
    }

    public interface IntEnum<E extends Enum<E>> {
        int getCode();
    }
}
