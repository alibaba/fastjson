package com.alibaba.json.bvt.issue_1400;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.serializer.SerializeConfig;
import junit.framework.TestCase;

import java.util.Arrays;
import java.util.List;

public class Issue1496 extends TestCase {
    public void test_for_issue() throws Exception {
        String json = JSON.toJSONString(SetupStatus.FINAL_TRAIL);
        assertEquals("{\"canRefuse\":true,\"code\":3,\"declaringClass\":\"com.alibaba.json.bvt.issue_1400.Issue1496$SetupStatus\",\"first\":false,\"last\":false,\"name\":\"FINAL_TRAIL\",\"nameCn\":\"公益委员会/理事会/理事长审核\"}", json);
    }

    public interface ISetupStatusInfo {
        List<SetupStatus> nextList();

        Boolean isFirst();

        Boolean isLast();
    }

    public interface ISetupStatusProcess {

        /**
         *
         * @return
         */
        SetupStatus refuse();

        /**
         * 状态转移失败返回null
         *
         * @param name
         * @return
         */
        SetupStatus next(String name);

    }

    @JSONType(serializeEnumAsJavaBean = true)
    public enum SetupStatus implements ISetupStatusInfo, ISetupStatusProcess {
        EDIT(0, "EDIT", "编辑中") {
            public List<SetupStatus> nextList() {
                return Arrays.asList(FIRST_TRAIL);
            }

            @Override
            public Boolean isFirst() {
                return true;
            }

            @Override
            public SetupStatus refuse() {
                return EDIT;
            }

        }, FIRST_TRAIL(1, "FIRST_TRAIL", "初审") {
            public List<SetupStatus> nextList() {
                return Arrays.asList(EXPERT, FINAL_TRAIL);
            }

            @Override
            public SetupStatus refuse() {
                return EDIT;
            }

        }, EXPERT(2, "EXPERT", "专家补充意见", false) {
            public List<SetupStatus> nextList() {
                return Arrays.asList(FINAL_TRAIL);
            }

        }, FINAL_TRAIL(3, "FINAL_TRAIL", "公益委员会/理事会/理事长审核") {
            public List<SetupStatus> nextList() {
                return Arrays.asList(PASS);
            }

            @Override
            public SetupStatus refuse() {
                return EDIT;
            }
        }, PASS(4, "PASS", "项目通过", false) {
            public List<SetupStatus> nextList() {
                return Arrays.asList(SIGN);
            }
        }, SIGN(5, "SIGN", "协议签署", false) {
            @Override
            public List<SetupStatus> nextList() {
                return Arrays.asList(ACTIVE);
            }
        }, ACTIVE(6, "ACTIVE", "启动") {
            @Override
            public List<SetupStatus> nextList() {
                return null;
            }

            @Override
            public Boolean isLast() {
                return true;
            }
        };
        private int code;
        private String name;
        private String nameCn;
        private boolean canRefuse;

        SetupStatus(int code, String name, String nameCn) {
            this.code = code;
            this.name = name;
            this.nameCn = nameCn;
            this.canRefuse = true;
        }

        SetupStatus(int code, String name, String nameCn, boolean canRefuse) {
            this.code = code;
            this.name = name;
            this.nameCn = nameCn;
            this.canRefuse = canRefuse;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNameCn() {
            return nameCn;
        }

        public void setNameCn(String nameCn) {
            this.nameCn = nameCn;
        }

        public boolean isCanRefuse() {
            return canRefuse;
        }

        public void setCanRefuse(boolean canRefuse) {
            this.canRefuse = canRefuse;
        }

        public static SetupStatus getFromCode(Integer code) {
            if (code == null) {
                return null;
            }
            for (SetupStatus status : values()) {
                if (status.code == code) {
                    return status;
                }
            }
            throw new IllegalArgumentException("unknown SetupStatus enumeration code:" + code);
        }

        public static SetupStatus getFromName(String name) {
            if (name == null) {
                return null;
            }
            for (SetupStatus status : values()) {
                if (status.name.equals(name)) {
                    return status;
                }
            }
            return null;
        }

        public Boolean isFirst() {
            return false;
        }

        public Boolean isLast() {
            return false;
        }

        public SetupStatus refuse() {
            return null;
        }

        public SetupStatus next(String name) {
            SetupStatus status = getFromName(name);
            return name != null && this.nextList().contains(status) ? status : null;
        }

        @Override
        public String toString() {
            return "SetupStatus{" + "code=" + code + ", name='" + name + '\'' + ", nameCn='" + nameCn + '\'' + ", canRefuse=" + canRefuse + '}';
        }
    }
}
