package com.alibaba.json.bvtVO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;

/**
 * 交易消息体
 */
@SuppressWarnings("rawtypes")
public class DataTransaction implements Serializable {

    private static final long serialVersionUID = 1L;

    private Head              head             = new Head();

    private Body              body             = new Body();

    public DataTransaction(){

    }

    /**
     * Head
     **/
    public static class Head {

        private String appid;

        private String transcode;

        private String seqno;

        private User   user = new User();

        private Ret    ret  = new Ret();

        /**
         * 用户信息
         */
        public static class User {

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
         */
        public static class Ret {

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
     */
    public static class Body {

        private Param   param = new Param();

        private DataSet dataset;

        public Body(){

        }

        /**
         * 参数
         */
        public static class Param {

            private Limit               limit = new Limit();

            private Map<String, String> form  = new HashMap<String, String>();

            /**
             * 分页信息
             */
            public static class Limit {

                private String start;
                private String size;
                private String total;
                private String orderBy;

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

                public String getOrderBy() {
                    return orderBy;
                }

                public void setOrderBy(String orderBy) {
                    this.orderBy = orderBy;
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
         */
        public static class DataSet {

            private String total;

            private List   rows = new ArrayList();

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
            this.setDataset(ds);
        }
    }

    public Head getHead() {
        return head;
    }

    public Body getBody() {
        return body;
    }

    public void setRetMsgCode(String code, String msg) {
        this.head.setRetCode(code);
        this.head.setRetMsg(msg);
    }

    public void setRetMsgCode(String code) {
        this.setRetMsgCode(code, null);
    }

    public void setDataSet(String total, List rows) {
        this.body.setDataset(total, rows);
    }

    public static DataTransaction fromJSON(String jsonString) {
        return JSON.parseObject(jsonString, DataTransaction.class);
    }

    public String toJSON() {
        return JSON.toJSONString(this);
    }

    public static void main(String args[]) {
        DataTransaction dt = new DataTransaction();

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        Map<String, Object> m = new HashMap<String, Object>();
        m.put("name", "tom");
        m.put("sex", "m");
        list.add(m);

        dt.setDataSet("1000", list);
        dt.setRetMsgCode("1", "ok");
        dt.getHead().setAppid("back");
        dt.getHead().setSeqno("201010");
        dt.getHead().getUser().setId("root");

        Map<String, String> m2 = new HashMap<String, String>();
        m2.put("name1", "tom");
        m2.put("name2", "tom");
        m2.put("name3", "tom");

        dt.getBody().getParam().setForm(m2);

        System.out.println(dt.toJSON());

        DataTransaction dt2 = DataTransaction.fromJSON(dt.toJSON());
        System.out.println(dt2.toJSON());
    }
}
