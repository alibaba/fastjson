package com.alibaba.json.bvtVO;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

/**
 * 交易消息体
 * */
public class DataTransaction2 implements Serializable {

    private static final long serialVersionUID = 1L;

    private Head head = new Head();

    private Body body = new Body();

    public DataTransaction2() {

    }

    /**
     * Head
     **/
    class Head {

        private String appid;

        private String transcode;

        private String seqno;

        private User user = new User();

        private Ret ret = new Ret();

        public Head() {

        }

        class User {
            private String id;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }
        }

        /**
         * 处理结果
         * */
        class Ret {
            private String code;
            private String msg;

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public String getMsg() {
                return msg;
            }

            public void setMsg(String msg) {
                this.msg = msg;
            }
        }

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getTranscode() {
            return transcode;
        }

        public void setTranscode(String transcode) {
            this.transcode = transcode;
        }

        public String getSeqno() {
            return seqno;
        }

        public void setSeqno(String seqno) {
            this.seqno = seqno;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public Ret getRet() {
            return ret;
        }

        public void setRet(Ret ret) {
            this.ret = ret;
        }

        public void setRetCode(String code) {
            this.ret.code = code;
        }

        public void setRetMsg(String msg) {
            this.ret.msg = msg;
        }
    }

    /**
     * Body
     * */
    @SuppressWarnings("rawtypes")
    class Body {

        private Param param = new Param();

        private DataSet dataset = new DataSet();

        public Body() {

        }

        /**
         * 参数
         * */
        class Param {
            private Limit limit = new Limit();

            private Map<String, String> form = new HashMap<String, String>();

            class Limit {
                private String start;
                private String size;
                private String total;

                public String getStart() {
                    return start;
                }

                public void setStart(String start) {
                    this.start = start;
                }

                public String getSize() {
                    return size;
                }

                public void setSize(String size) {
                    this.size = size;
                }

                public String getTotal() {
                    return total;
                }

                public void setTotal(String total) {
                    this.total = total;
                }
            }

            public Limit getLimit() {
                return limit;
            }

            public void setLimit(Limit limit) {
                this.limit = limit;
            }

            public Map<String, String> getForm() {
                return form;
            }

            public void setForm(Map<String, String> form) {
                this.form = form;
            }
        }

        /**
         * 数据集
         * */
        class DataSet {
            private String total;

            private List rows = new ArrayList();

            public String getTotal() {
                return total;
            }

            public void setTotal(String total) {
                this.total = total;
            }

            public List getRows() {
                return rows;
            }

            public void setRows(List rows) {
                this.rows = rows;
            }
        }

        public Param getParam() {
            return param;
        }

        public void setParam(Param param) {
            this.param = param;
        }

        public DataSet getDataset() {
            return dataset;
        }

        public void setDataset(DataSet dataset) {
            this.dataset = dataset;
        }

        public void setDataset(String total, List rows) {
            DataSet ds = new DataSet();
            ds.setTotal(total);
            ds.setRows(rows);
            this.dataset = ds;
        }
    }

    public Head getHead() {
        return head;
    }

    public Body getBody() {
        return body;
    }

    public void setHead(Head head) {
        this.head = head;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    /**
     * 设置返回的消息信息
     * */
    public void setRetMsgCode(String code, String msg) {
        this.head.setRetCode(code);
        this.head.setRetMsg(msg);
    }

    public void setRetMsgCode(String code) {
        this.setRetMsgCode(code, null);
    }

    /**
     * 设置返回的结果集
     **/
    @SuppressWarnings("rawtypes")
    public void setDataSet(String total, List rows) {
        this.body.setDataset(total, rows);
    }

    public static DataTransaction2 fromJSON(String jsonString) {
        return JSON.parseObject(jsonString, DataTransaction2.class);
    }

    public String toJSON() {
        return JSON.toJSONString(this);
    }

    public static void main(String args[]) {

        String jsonString = "{'head' : {'appid':'epas','transcode' : '000000','seqno' : '111111111',        'user' : {          'id' : '00000'},        'ret' : {           'code' : '1',           'msg' : 'txt'}  },  'body' : {      param : {           form:{              name : '111',               sex : '1',              address : 'street1',                array : [ {                 id : '1',                   name : 'tom1'               }, {                    id : '2',                   name : 'tom2'               } ]},           limit : {               start : 1,              size : 25,              total : 100}        },      dataset : {         total : 1000,           rows : [ {              id : 'id',              name : 'name'           }, {                id : 'id',              name : 'name'           } ]     }   }}";
        DataTransaction2 dt = DataTransaction2.fromJSON(jsonString);
        System.out.println(dt.toJSON());
        DataTransaction2 dt1 = JSON.parseObject(dt.toJSON(), DataTransaction2.class);
        System.out.println(dt1.toJSON());
        
        Assert.assertEquals(dt.toJSON(), dt1.toJSON());
        
        System.out.println("=================");
        System.out.println(dt.toJSON());
        dt.setRetMsgCode("-1", "错误");
        dt.setDataSet("1000", new ArrayList<Map<String, Object>>());
        System.out.println(dt.toJSON());
        
        String text = dt.toJSON();
        System.out.println(text);
        
        DataTransaction2 dt2 = JSON.parseObject(text, DataTransaction2.class);
        System.out.println(JSON.toJSONString(dt2));

        Assert.assertEquals(dt.toJSON(), dt2.toJSON());
    }
}
