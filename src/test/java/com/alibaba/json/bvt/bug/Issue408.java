package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSONReader;
import com.alibaba.fastjson.parser.Feature;
import junit.framework.TestCase;
import org.junit.Assert;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class Issue408 extends TestCase {

    private InputStream inputStream;

    @Override
    public void setUp() throws Exception {
        String resource = "json/Issue408.json";
        inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);

        com.alibaba.fastjson.parser.ParserConfig.global.addAccept("com.alibaba.json.bvt.bug.Issue408.");
    }


    @Override
    public void tearDown() throws Exception {
        inputStream.close();
    }

    public void test_for_issue() throws Exception {

        JSONReader jsonReader = new JSONReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
        jsonReader.config(Feature.AllowArbitraryCommas, true);
        jsonReader.config(Feature.IgnoreNotMatch, true);
        jsonReader.config(Feature.SortFeidFastMatch, false);
        jsonReader.config(Feature.DisableCircularReferenceDetect, true);
        jsonReader.config(Feature.AutoCloseSource, true);

        VOList deserialized = null;
        try {
            deserialized = (VOList)jsonReader.readObject();
        }finally {
            jsonReader.close();
        }

        for (int i = 0; i < 17; i++) {
            Assert.assertEquals(deserialized.getVolist()[i].getLongid0(), Long.valueOf(1234567890123L));
            Assert.assertEquals(deserialized.getVolist()[i].getLongid1(), Long.valueOf(1234567890123L));
            Assert.assertEquals(deserialized.getVolist()[i].getLongid2(), Long.valueOf(1234567890123L));
            Assert.assertEquals(deserialized.getVolist()[i].getLongid3(), Long.valueOf(1234567890123L));
            Assert.assertEquals(deserialized.getVolist()[i].getLongid4(), Long.valueOf(1234567890123L));
            Assert.assertEquals(deserialized.getVolist()[i].getLongid5(), Long.valueOf(1234567890123L));
            Assert.assertEquals(deserialized.getVolist()[i].getLongid6(), Long.valueOf(1234567890123L));
            Assert.assertEquals(deserialized.getVolist()[i].getLongid7(), Long.valueOf(1234567890123L));
            Assert.assertEquals(deserialized.getVolist()[i].getLongid8(), Long.valueOf(1234567890123L));
            Assert.assertEquals(deserialized.getVolist()[i].getLongid9(), Long.valueOf(1234567890123L));
            Assert.assertEquals(deserialized.getVolist()[i].getLongid10(), Long.valueOf(1234567890123L));
            Assert.assertEquals(deserialized.getVolist()[i].getLongid11(), Long.valueOf(1234567890123L));
            Assert.assertEquals(deserialized.getVolist()[i].getLongid12(), Long.valueOf(1234567890123L));
            Assert.assertEquals(deserialized.getVolist()[i].getLongid13(), Long.valueOf(1234567890123L));
            Assert.assertEquals(deserialized.getVolist()[i].getLongid14(), Long.valueOf(1234567890123L));
            Assert.assertEquals(deserialized.getVolist()[i].getLongid15(), Long.valueOf(1234567890123L));
            Assert.assertEquals(deserialized.getVolist()[i].getLongid16(), Long.valueOf(1234567890123L));
            Assert.assertEquals(deserialized.getVolist()[i].getLongid17(), Long.valueOf(1234567890123L));
            Assert.assertEquals(deserialized.getVolist()[i].getLongid18(), Long.valueOf(1234567890123L));
            Assert.assertEquals(deserialized.getVolist()[i].getLongid19(), Long.valueOf(1234567890123L));
        }
    }

    public static class VOList {
        private VO[] volist;
        private Long longid0;
        private Long longid1;

        public VO[] getVolist() {
            return volist;
        }

        public void setVolist(VO[] volist) {
            this.volist = volist;
        }

        public Long getLongid1() {
            return longid1;
        }

        public void setLongid1(Long longid1) {
            this.longid1 = longid1;
        }

        public Long getLongid0() {
            return longid0;
        }

        public void setLongid0(Long longid0) {
            this.longid0 = longid0;
        }
    }

    public static class VO {
        private Long longid0;
        private Long longid1;
        private Long longid2;
        private Long longid3;
        private Long longid4;
        private Long longid5;
        private Long longid6;
        private Long longid7;
        private Long longid8;
        private Long longid9;
        private Long longid10;
        private Long longid11;
        private Long longid12;
        private Long longid13;
        private Long longid14;
        private Long longid15;
        private Long longid16;
        private Long longid17;
        private Long longid18;
        private Long longid19;

        public Long getLongid0() {
            return longid0;
        }

        public void setLongid0(Long longid0) {
            this.longid0 = longid0;
        }

        public Long getLongid1() {
            return longid1;
        }

        public void setLongid1(Long longid1) {
            this.longid1 = longid1;
        }

        public Long getLongid2() {
            return longid2;
        }

        public void setLongid2(Long longid2) {
            this.longid2 = longid2;
        }

        public Long getLongid3() {
            return longid3;
        }

        public void setLongid3(Long longid3) {
            this.longid3 = longid3;
        }

        public Long getLongid4() {
            return longid4;
        }

        public void setLongid4(Long longid4) {
            this.longid4 = longid4;
        }

        public Long getLongid5() {
            return longid5;
        }

        public void setLongid5(Long longid5) {
            this.longid5 = longid5;
        }

        public Long getLongid6() {
            return longid6;
        }

        public void setLongid6(Long longid6) {
            this.longid6 = longid6;
        }

        public Long getLongid7() {
            return longid7;
        }

        public void setLongid7(Long longid7) {
            this.longid7 = longid7;
        }

        public Long getLongid8() {
            return longid8;
        }

        public void setLongid8(Long longid8) {
            this.longid8 = longid8;
        }

        public Long getLongid9() {
            return longid9;
        }

        public void setLongid9(Long longid9) {
            this.longid9 = longid9;
        }

        public Long getLongid10() {
            return longid10;
        }

        public void setLongid10(Long longid10) {
            this.longid10 = longid10;
        }

        public Long getLongid11() {
            return longid11;
        }

        public void setLongid11(Long longid11) {
            this.longid11 = longid11;
        }

        public Long getLongid12() {
            return longid12;
        }

        public void setLongid12(Long longid12) {
            this.longid12 = longid12;
        }

        public Long getLongid13() {
            return longid13;
        }

        public void setLongid13(Long longid13) {
            this.longid13 = longid13;
        }

        public Long getLongid14() {
            return longid14;
        }

        public void setLongid14(Long longid14) {
            this.longid14 = longid14;
        }

        public Long getLongid15() {
            return longid15;
        }

        public void setLongid15(Long longid15) {
            this.longid15 = longid15;
        }

        public Long getLongid16() {
            return longid16;
        }

        public void setLongid16(Long longid16) {
            this.longid16 = longid16;
        }

        public Long getLongid17() {
            return longid17;
        }

        public void setLongid17(Long longid17) {
            this.longid17 = longid17;
        }

        public Long getLongid18() {
            return longid18;
        }

        public void setLongid18(Long longid18) {
            this.longid18 = longid18;
        }

        public Long getLongid19() {
            return longid19;
        }

        public void setLongid19(Long longid19) {
            this.longid19 = longid19;
        }
    }
}
