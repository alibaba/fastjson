package com.alibaba.json.bvt.issue_3200;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONCreator;
import com.alibaba.fastjson.annotation.JSONField;
import junit.framework.TestCase;

public class Issue3266 extends TestCase {
    public void test_for_issue() throws Exception {
        VO vo = new VO();
        vo.type = Color.Black;

        String str = JSON.toJSONString(vo);
        assertEquals("{\"type\":1003}", str);

        VO vo2 = JSON.parseObject(str, VO.class);

    }

    public static class VO {
        public Color type;
    }

    public enum Color {
        Red(1001),
        White(1002),
        Black(1003),
        Blue(1004);

        private final int code;

        private Color(int code) {
            this.code = code;
        }

        @JSONField
        public int getCode() {
            return code;
        }

        @JSONCreator
        public static Color from(int code) {
            for (Color v : values()) {
                if (v.code == code) {
                    return v;
                }
            }

            throw new IllegalArgumentException("code " + code);
        }
    }
}
