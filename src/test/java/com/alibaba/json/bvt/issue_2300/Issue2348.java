package com.alibaba.json.bvt.issue_2300;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import junit.framework.TestCase;

import java.io.Serializable;
import java.util.List;

public class Issue2348 extends TestCase {
    public void test_for_issue() throws Exception {
        String json = "{\n" +
                "\"ID\": null,\n" +
                "\"XM\": \"陈XX\",\n" +
                "\"XB\": \"1\",\n" +
                "\"XB_\": \"男\",\n" +
                "\"ZJH\": \"522401198310176625\",\n" +
                "\"JSH\": \"0101\",\n" +
                "\"GJ\": \"156\",\n" +
                "\"GJ_\": \"中国\",\n" +
                "\"MZ\": \"01\",\n" +
                "\"MZ_\": \"汉族\",\n" +
                "\"WHCD\": \"48\",\n" +
                "\"WHCD_\": \"相当中专或中技毕业\",\n" +
                "\"ZY\": null,\n" +
                "\"ZY_\": null,\n" +
                "\"CSRQ\": \"1532448000000\",\n" +
                "\"CBZ\": null,\n" +
                "\"LFFH\": \"370100111201807250001\",\n" +
                "\"NL\": \"0\",\n" +
                "\"RSRQ\": \"1537167900000\",\n" +
                "\"AY\": \"010180\",\n" +
                "\"AY_\": \"资助活动案\",\n" +
                "\"ZZ\": \"AAAA\",\n" +
                "\"BAHJ\": \"11\",\n" +
                "\"BAHJ_\": \"事留\",\n" +
                "\"JYQX\": null,\n" +
                "\"ZSZT\": \"11\",\n" +
                "\"ZSZT_\": null,\n" +
                "\"PWH\": \"16\",\n" +
                "\"WXDJ\": \"3\",\n" +
                "\"WXDJ_\": \"二级\",\n" +
                "\"JKZK\": null,\n" +
                "\"JKZK_\": null,\n" +
                "\"FZJJ\": \"阿德\",\n" +
                "\"ZDRY\": \"0\",\n" +
                "\"ZDRY_\": \"非重点\",\n" +
                "\"Photo\": \"\",\n" +
                "\"TZZ\": \"\",\n" +
                "\"TZZ2\": \"\",\n" +
                "\"GYQX\": \"2018/8/30 0:00:00\",\n" +
                "\"ZZD\": \"QQQQ\",\n" +
                "\"RSAQ\": \"阿德\",\n" +
                "\"SG\": 22,\n" +
                "\"TZ\": 22,\n" +
                "\"HYZK\": null,\n" +
                "\"HYZK_\": null,\n" +
                "\"BHLX\": \"1\",\n" +
                "\"BHLX_\": null,\n" +
                "\"RFID\": \"23\",\n" +
                "\"RFID_\": \"理发\",\n" +
                "\"ZBZT\": null,\n" +
                "\"JDXJ\": null,\n" +
                "\"WCNR\": null,\n" +
                "\"BYZDE\": \"3\",\n" +
                "\"BYZDE_\": null,\n" +
                "\"GL\": null,\n" +
                "\"GZDW\": \"无单位\",\n" +
                "\"ZJLX\": \"居民身份证\",\n" +
                "\"CARDID\": \"D0CB8F1B\",\n" +
                "\"JBR\": null,\n" +
                "\"SKSJ\": null,\n" +
                "\"SKYY\": null,\n" +
                "\"YE\": 7427.87,\n" +
                "\"BADW\": \"市看\",\n" +
                "\"RSXZ\": \"事留\",\n" +
                "\"ZB\": null,\n" +
                "\"TYPE\": \"1\",\n" +
                "\"CSSJ\": null,\n" +
                "\"CSYY\": null,\n" +
                "\"YXGW\": \"1\"\n" +
                "}";

        PersonnelModel p = JSON.parseObject(json, PersonnelModel.class);
        assertEquals("23", p.getRfid());
        assertEquals("1", p.getBhlx());
        assertEquals(null, p.getJdxj());
    }

    public static class RoomPersonnel {
        private String code;
        private List<PersonnelModel> data;
        private int count;
        static RoomPersonnel roompersonnel;

        public static RoomPersonnel getRoomPersonnel(){
            if(roompersonnel==null){
                roompersonnel=new  RoomPersonnel();
            }
            return roompersonnel;
        }

        public void setCode(String code) {
            this.code = code;
        }
        public String getCode() {
            return code;
        }


        public void setData(List<PersonnelModel> data) {
            this.data = data;
        }
        public List<PersonnelModel> getData() {
            return data;
        }


        public void setCount(int count) {
            this.count = count;
        }
        public int getCount() {
            return count;
        }
    }

    public static class PersonnelModel implements Serializable {

        private String xm;
        private String xb;
        private String xb_;
        private String zjh;
        private String jsh;
        private String gj;
        private String gj_;
        private String mz;
        private String mz_;
        private String whcd;
        private String whcd_;
        private String zy;
        private String zy_;
        private String csrq;
        private String cbz;
        private String lffh;
        private String nl;
        private String rsrq;
        private String ay;
        private String ay_;
        private String zz;
        private String bahj;
        private String bahj_;
        private String jyqx;
        private String zszt;
        private String zszt_;
        private String pwh;
        private String wxdj;
        private String wxdj_;
        private String jkzk;
        private String fzjj;
        private String zdry;
        private String zdry_;
        private String photo;
        private String tzz;
        private String tzz2;
        private String gyqx;
        private String zzd;
        private String rsaq;
        private String sg;
        private String tz;
        private String hyzk;
        private String hyzk_;
        private String bhlx;
        private String rfid;
        private String jkzk_;
        private String gzdw;
        private String zjlx;
        private String zbzt;
        private String jdxj;
        private String wcnr;
        private String byzde;
        private String byzde_;
        private String badw;
        private String type;
        private String rsxz;
        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getBadw() {
            return badw;
        }

        public void setBadw(String badw) {
            this.badw = badw;
        }

        public String getByzde() {
            return byzde;
        }

        public void setByzde(String byzde) {
            this.byzde = byzde;
        }

        public String getByzde_() {
            return byzde_;
        }

        public void setByzde_(String byzde_) {
            this.byzde_ = byzde_;
        }

        public String getJdxj() {
            return jdxj;
        }

        public void setJdxj(String jdxj) {
            this.jdxj = jdxj;
        }

        public String getWcnr() {
            return wcnr;
        }

        public void setWcnr(String wcnr) {
            this.wcnr = wcnr;
        }

        public String getGzdw() {
            return gzdw;
        }

        public String getZbzt() {
            return zbzt;
        }

        public void setZbzt(String zbzt) {
            this.zbzt = zbzt;
        }

        public void setGzdw(String gzdw) {
            this.gzdw = gzdw;
        }

        public String getZjlx() {
            return zjlx;
        }

        public void setZjlx(String zjlx) {
            this.zjlx = zjlx;
        }

        public String getJkzk_() {
            return jkzk_;
        }

        public void setJkzk_(String jkzk_) {
            this.jkzk_ = jkzk_;
        }

        public String getHyzk() {
            return hyzk;
        }

        public void setHyzk(String hyzk) {
            this.hyzk = hyzk;
        }

        public String getHyzk_() {
            return hyzk_;
        }

        public void setHyzk_(String hyzk_) {
            this.hyzk_ = hyzk_;
        }

        public String getBhlx() {
            return bhlx;
        }

        public void setBhlx(String bhlx) {
            this.bhlx = bhlx;
        }

        public String getRfid() {
            return rfid;
        }

        public void setRfid(String rfid) {
            this.rfid = rfid;
        }

        public void setXm(String xm) {
            this.xm = xm;
        }

        public String getGyqx() {
            return gyqx;
        }

        public void setGyqx(String gyqx) {
            this.gyqx = gyqx;
        }

        public String getZzd() {
            return zzd;
        }

        public void setZzd(String zzd) {
            this.zzd = zzd;
        }

        public String getRsaq() {
            return rsaq;
        }

        public void setRsaq(String rsaq) {
            this.rsaq = rsaq;
        }

        public String getSg() {
            return sg;
        }

        public void setSg(String sg) {
            this.sg = sg;
        }

        public String getTz() {
            return tz;
        }

        public void setTz(String tz) {
            this.tz = tz;
        }

        public String getXm() {
            return xm;
        }

        public void setXb(String xb) {
            this.xb = xb;
        }

        public String getXb() {
            return xb;
        }

        public void setXb_(String xb_) {
            this.xb_ = xb_;
        }

        public String getXb_() {
            return xb_;
        }

        public void setZjh(String zjh) {
            this.zjh = zjh;
        }

        public String getZjh() {
            return zjh;
        }

        public void setJsh(String jsh) {
            this.jsh = jsh;
        }

        public String getJsh() {
            return jsh;
        }

        public void setGj(String gj) {
            this.gj = gj;
        }

        public String getGj() {
            return gj;
        }

        public void setGj_(String gj_) {
            this.gj_ = gj_;
        }

        public String getGj_() {
            return gj_;
        }

        public void setMz(String mz) {
            this.mz = mz;
        }

        public String getMz() {
            return mz;
        }

        public void setMz_(String mz_) {
            this.mz_ = mz_;
        }

        public String getMz_() {
            return mz_;
        }

        public void setWhcd(String whcd) {
            this.whcd = whcd;
        }

        public String getWhcd() {
            return whcd;
        }

        public void setWhcd_(String whcd_) {
            this.whcd_ = whcd_;
        }

        public String getWhcd_() {
            return whcd_;
        }

        public void setZy(String zy) {
            this.zy = zy;
        }

        public String getZy() {
            return zy;
        }

        public void setZy_(String zy_) {
            this.zy_ = zy_;
        }

        public String getZy_() {
            return zy_;
        }

        public void setCsrq(String csrq) {
            this.csrq = csrq;
        }

        public String getCsrq() {
            return csrq;
        }

        public void setCbz(String cbz) {
            this.cbz = cbz;
        }

        public String getCbz() {
            return cbz;
        }

        public void setLffh(String lffh) {
            this.lffh = lffh;
        }

        public String getLffh() {
            return lffh;
        }

        public void setNl(String nl) {
            this.nl = nl;
        }

        public String getNl() {
            return nl;
        }

        public void setRsrq(String rsrq) {
            this.rsrq = rsrq;
        }

        public String getRsrq() {
            return rsrq;
        }

        public void setAy(String ay) {
            this.ay = ay;
        }

        public String getAy() {
            return ay;
        }

        public void setAy_(String ay_) {
            this.ay_ = ay_;
        }

        public String getAy_() {
            return ay_;
        }

        public void setZz(String zz) {
            this.zz = zz;
        }

        public String getZz() {
            return zz;
        }

        public void setBahj(String bahj) {
            this.bahj = bahj;
        }

        public String getBahj() {
            return bahj;
        }

        public void setBahj_(String bahj_) {
            this.bahj_ = bahj_;
        }

        public String getBahj_() {
            return bahj_;
        }

        public void setJyqx(String jyqx) {
            this.jyqx = jyqx;
        }

        public String getJyqx() {
            return jyqx;
        }

        public void setZszt(String zszt) {
            this.zszt = zszt;
        }

        public String getZszt() {
            return zszt;
        }

        public void setZszt_(String zszt_) {
            this.zszt_ = zszt_;
        }

        public String getZszt_() {
            return zszt_;
        }

        public void setPwh(String pwh) {
            this.pwh = pwh;
        }

        public String getPwh() {
            return pwh;
        }

        public void setWxdj(String wxdj) {
            this.wxdj = wxdj;
        }

        public String getWxdj() {
            return wxdj;
        }

        public void setWxdj_(String wxdj_) {
            this.wxdj_ = wxdj_;
        }

        public String getWxdj_() {
            return wxdj_;
        }

        public void setJkzk(String jkzk) {
            this.jkzk = jkzk;
        }

        public String getJkzk() {
            return jkzk;
        }

        public void setFzjj(String fzjj) {
            this.fzjj = fzjj;
        }

        public String getFzjj() {
            return fzjj;
        }

        public void setZdry(String zdry) {
            this.zdry = zdry;
        }

        public String getZdry() {
            return zdry;
        }

        public void setZdry_(String zdry_) {
            this.zdry_ = zdry_;
        }

        public String getZdry_() {
            return zdry_;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getPhoto() {
            return photo;
        }

        public void setTzz(String tzz) {
            this.tzz = tzz;
        }

        public String getTzz() {
            return tzz;
        }

        public void setTzz2(String tzz2) {
            this.tzz2 = tzz2;
        }

        public String getTzz2() {
            return tzz2;
        }


        public void setRsxz(String rsxz) {
            this.rsxz = rsxz;
        }

        public String getRsxz() {
            return rsxz;
        }

    }
}
