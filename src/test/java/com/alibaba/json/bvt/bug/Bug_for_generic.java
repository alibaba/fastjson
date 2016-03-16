package com.alibaba.json.bvt.bug;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class Bug_for_generic extends TestCase {

    public void test() throws Exception {
        String json = "{\"@type\":\"com.alibaba.json.bvt.bug.Bug_for_generic$NotifyDetail\",\"args\":[\"61354557\",\"依依\",\"六\"],\"destId\":60721687,\"detailId\":3155063,\"display\":false,\"foundTime\":{\"@type\":\"java.sql.Timestamp\",\"val\":1344530416000},\"hotId\":0,\"srcId\":1000,\"templateId\":482}";
        JSON.parseObject(json, NotifyDetail.class);
        System.out.println("NotifyDetail对象没问题");
        String json2 = "{\"@type\":\"com.alibaba.json.bvt.bug.Bug_for_generic$Pagination\",\"fromIndex\":0,\"list\":[{\"@type\":\"NotifyDetail\",\"args\":[\"61354557\",\"依依\",\"六\"],\"destId\":60721687,\"detailId\":3155063,\"display\":false,\"foundTime\":{\"@type\":\"java.sql.Timestamp\",\"val\":1344530416000},\"hotId\":0,\"srcId\":1000,\"templateId\":482},{\"@type\":\"NotifyDetail\",\"args\":[\"14527269\",\"懒洋洋\",\"///最佳拍档,非常\",\"24472950\"],\"destId\":60721687,\"detailId\":3151609,\"display\":false,\"foundTime\":{\"@type\":\"java.sql.Timestamp\",\"val\":1344354485000},\"hotId\":0,\"srcId\":1000,\"templateId\":40},{\"@type\":\"NotifyDetail\",\"args\":[\"51090218\",\"天之涯\",\"天会黑，人会变。三分\"],\"destId\":60721687,\"detailId\":3149221,\"display\":false,\"foundTime\":{\"@type\":\"java.sql.Timestamp\",\"val\":1344247529000},\"hotId\":0,\"srcId\":1000,\"templateId\":459},{\"@type\":\"NotifyDetail\",\"args\":[\"51687981\",\"摹然回首梦已成年\",\"星星在哪里都很亮的,\"],\"destId\":60721687,\"detailId\":3149173,\"display\":false,\"foundTime\":{\"@type\":\"java.sql.Timestamp\",\"val\":1344247414000},\"hotId\":0,\"srcId\":1000,\"templateId\":459},{\"@type\":\"NotifyDetail\",\"args\":[\"41486427\",\"寒江蓑笠\",\"双休了\"],\"destId\":60721687,\"detailId\":3148148,\"display\":false,\"foundTime\":{\"@type\":\"java.sql.Timestamp\",\"val\":1344244730000},\"hotId\":0,\"srcId\":1000,\"templateId\":459}],\"maxLength\":5,\"nextPage\":2,\"pageIndex\":1,\"prevPage\":1,\"toIndex\":5,\"totalPage\":3,\"totalResult\":13}";
        JSON.parseObject(json2, Pagination.class);
    }

    public static class Pagination<T> implements Serializable {

        /**
     * 
     */
        private static final long serialVersionUID = 5038839734218582220L;

        private int               totalResult      = 0;

        private int               totalPage        = 1;

        private int               pageIndex        = 1;

        private int               maxLength        = 5;

        private int               fromIndex        = 0;

        private int               toIndex          = 0;

        private List<T>           list             = new ArrayList<T>();

        public Pagination(){

        }

        public void setTotalResult(int totalResult) {
            this.totalResult = totalResult;
        }

        public void setTotalPage(int totalPage) {
            this.totalPage = totalPage;
        }

        public void setPageIndex(int pageIndex) {
            this.pageIndex = pageIndex;
        }

        public void setMaxLength(int maxLength) {
            this.maxLength = maxLength;
        }

        public void setFromIndex(int fromIndex) {
            this.fromIndex = fromIndex;
        }

        public void setToIndex(int toIndex) {
            this.toIndex = toIndex;
        }

        public int getFromIndex() {
            return this.fromIndex;
        }

        public int getToIndex() {
            return this.toIndex;
        }

        public int getNextPage() {
            if (this.pageIndex < this.totalPage) {
                return this.pageIndex + 1;
            } else {
                return this.pageIndex;
            }
        }

        public int getPrevPage() {
            if (this.pageIndex > 1) {
                return this.pageIndex - 1;
            }
            return this.pageIndex;
        }

        /**
         * @return the currentPage
         */
        public int getPageIndex() {
            return pageIndex;
        }

        /**
         * @return the list
         */
        public List<T> getList() {
            if (list == null) {
                return new ArrayList<T>();
            }
            return new ArrayList<T>(list);
        }

        /**
         * @return the totalPage
         */
        public int getTotalPage() {
            return totalPage;
        }

        /**
         * @return the totalRecord
         */
        public int getTotalResult() {
            return totalResult;
        }

        public int getMaxLength() {
            return maxLength;
        }

        /**
         * @param totalResult
         * @param pageIndex
         * @param maxLength
         */
        public Pagination(int totalResult, int pageIndex, int maxLength){
            if (maxLength > 0) {
                this.maxLength = maxLength;
            }
            if (totalResult > 0) {
                this.totalResult = totalResult;
            }
            if (pageIndex > 0) {
                this.pageIndex = pageIndex;
            }
            this.totalPage = this.totalResult / this.maxLength;
            if (this.totalResult % this.maxLength != 0) {
                this.totalPage = this.totalPage + 1;
            }
            if (this.totalPage == 0) {
                this.totalPage = 1;
            }
            if (this.pageIndex > this.totalPage) {
                this.pageIndex = this.totalPage;
            }
            if (this.pageIndex <= 0) {
                this.pageIndex = 1;
            }
            this.fromIndex = (this.pageIndex - 1) * maxLength;
            this.toIndex = this.fromIndex + maxLength;
            if (this.toIndex < 0) {
                this.toIndex = fromIndex;
            }
            if (this.toIndex > this.totalResult) {
                this.toIndex = this.totalResult;
            }

        }

        /**
         * @param datas the datas to set
         */
        public void setList(List<T> list) {
            this.list = list;
        }

        /*
         * (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            StringBuffer sb = new StringBuffer();
            sb.append("Pagination:\r\n");
            sb.append("\tpageIndex\t" + this.pageIndex + "\r\n");
            sb.append("\ttotalPage\t" + this.totalPage + "\r\n");
            sb.append("\tmaxLength\t" + this.maxLength + "\r\n");
            sb.append("\ttotalResult\t" + this.totalResult + "\r\n");
            for (T t : list) {
                sb.append(t + "\r\n");
            }
            return sb.toString();
        }

    }

    public static class NotifyDetail implements Serializable {

        /**
         * 
         */
        private static final long serialVersionUID = 8760630447394929224L;

        private int               detailId;

        private int               hotId;

        private int               templateId;

        private int               srcId;

        private int               destId;

        private boolean           display;

        private java.sql.Date     foundTime;

        private List<String>      args             = new ArrayList<String>();

        public int getDetailId() {
            return detailId;
        }

        public void setDetailId(int detailId) {
            this.detailId = detailId;
        }

        public int getHotId() {
            return hotId;
        }

        public void setHotId(int hotId) {
            this.hotId = hotId;
        }

        public int getTemplateId() {
            return templateId;
        }

        public List<String> getArgs() {
            return args;
        }

        public void setArgs(List<String> args) {
            this.args = args;
        }

        public void setTemplateId(int templateId) {
            this.templateId = templateId;
        }

        public int getSrcId() {
            return srcId;
        }

        public void setSrcId(int srcId) {
            this.srcId = srcId;
        }

        public int getDestId() {
            return destId;
        }

        public void setDestId(int destId) {
            this.destId = destId;
        }

        public boolean isDisplay() {
            return display;
        }

        public void setDisplay(boolean display) {
            this.display = display;
        }

        public java.sql.Date getFoundTime() {
            return foundTime;
        }

        public void setFoundTime(java.sql.Date foundTime) {
            this.foundTime = foundTime;
        }

        /*
         * (non-Javadoc)
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {
            int hasCode = 0;
            if (this.detailId != 0) {
                hasCode += this.detailId;
            }
            return hasCode;
        }

        /*
         * (non-Javadoc)
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (!(obj instanceof NotifyDetail)) {
                return false;
            }
            return this.hashCode() == obj.hashCode();
        }

    }

}
