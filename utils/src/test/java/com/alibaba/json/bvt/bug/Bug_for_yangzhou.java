package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Bug_for_yangzhou extends TestCase {
    public void test_for_issue() throws Exception {
        String test = "{\"distinct\":false,\"oredCriteria\":[{\"allCriteria\":[{\"betweenValue\":false,\"condition\":\"area_id =\",\"listValue\":false,\"noValue\":false,\"singleValue\":true,\"value\":917477670000000000},{\"betweenValue\":false,\"condition\":\"cabinet_id =\",\"listValue\":false,\"noValue\":false,\"singleValue\":true,\"value\":500036},{\"betweenValue\":false,\"condition\":\"status =\",\"listValue\":false,\"noValue\":false,\"singleValue\":true,\"value\":0}],\"criteria\":[{\"$ref\":\"$.oredCriteria[0].allCriteria[0]\"},{\"$ref\":\"$.oredCriteria[0].allCriteria[1]\"},{\"$ref\":\"$.oredCriteria[0].allCriteria[2]\"}],\"valid\":true}],\"page\":true,\"pageIndex\":0,\"pageSize\":1,\"pageStart\":1}";

        System.out.println(test);
        CabinetAuthCodeParam cabinetAuthCodeParam = JSONObject.toJavaObject(JSON.parseObject(test), CabinetAuthCodeParam.class);
        System.out.println(JSON.toJSONString(cabinetAuthCodeParam));
        final String jsonString = JSON.toJSONString(cabinetAuthCodeParam);
        assertEquals("{\"distinct\":false,\"oredCriteria\":[{\"allCriteria\":[{\"listValue\":false,\"noValue\":false,\"condition\":\"area_id =\",\"betweenValue\":false,\"singleValue\":true,\"value\":917477670000000000},{\"listValue\":false,\"noValue\":false,\"condition\":\"cabinet_id =\",\"betweenValue\":false,\"singleValue\":true,\"value\":500036},{\"listValue\":false,\"noValue\":false,\"condition\":\"status =\",\"betweenValue\":false,\"singleValue\":true,\"value\":0}],\"criteria\":[{\"$ref\":\"$.oredCriteria[0].allCriteria[0]\"},{\"$ref\":\"$.oredCriteria[0].allCriteria[1]\"},{\"$ref\":\"$.oredCriteria[0].allCriteria[2]\"}],\"valid\":true}],\"page\":true,\"pageIndex\":0,\"pageSize\":1,\"pageStart\":1}", jsonString);
//        CabinetAuthCodeRecordParam cabinetAuthCodeRecordParam = JSONObject.toJavaObject(JSON.parseObject(jsonString), CabinetAuthCodeRecordParam.class);
//        System.out.println(JSON.toJSONString(cabinetAuthCodeRecordParam));
    }

    public static class CabinetAuthCodeParam {
        protected String orderByClause;

        protected boolean distinct;

        protected boolean page;

        protected int pageIndex;

        protected int pageSize;

        protected int pageStart;

        protected List<Criteria> oredCriteria;

        public CabinetAuthCodeParam() {
            oredCriteria = new ArrayList<Criteria>();
        }

        public CabinetAuthCodeParam appendOrderByClause(OrderCondition orderCondition, SortType sortType) {
            if (null != orderByClause) {
                orderByClause = orderByClause + ", " + orderCondition.getColumnName() + " " + sortType.getValue();
            } else {
                orderByClause = orderCondition.getColumnName() + " " + sortType.getValue();
            }
            return this;
        }

        public String getOrderByClause() {
            return orderByClause;
        }

        public void setDistinct(boolean distinct) {
            this.distinct = distinct;
        }

        public boolean isDistinct() {
            return distinct;
        }

        public void setPage(boolean page) {
            this.page = page;
        }

        public boolean isPage() {
            return page;
        }

        public int getPageIndex() {
            return pageIndex;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize < 1 ? 10 : pageSize;
            this.pageIndex = pageStart < 1 ? 0 : (pageStart - 1) * this.pageSize;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageStart(int pageStart) {
            this.pageStart = pageStart < 1 ? 1 : pageStart;
            this.pageIndex = (this.pageStart - 1) * this.pageSize;
        }

        public int getPageStart() {
            return pageStart;
        }

        public List<Criteria> getOredCriteria() {
            return oredCriteria;
        }

        public void or(Criteria criteria) {
            oredCriteria.add(criteria);
        }

        public Criteria or() {
            Criteria criteria = createCriteriaInternal();
            oredCriteria.add(criteria);
            return criteria;
        }

        public Criteria createCriteria() {
            Criteria criteria = createCriteriaInternal();
            if (oredCriteria.size() == 0) {
                oredCriteria.add(criteria);
            }
            return criteria;
        }

        protected Criteria createCriteriaInternal() {
            Criteria criteria = new Criteria();
            return criteria;
        }

        public void clear() {
            oredCriteria.clear();
            orderByClause = null;
            distinct = false;
        }

        protected abstract static class GeneratedCriteria {
            protected List<Criterion> criteria;

            protected GeneratedCriteria() {
                super();
                criteria = new ArrayList<Criterion>();
            }

            public boolean isValid() {
                return criteria.size() > 0;
            }

            public List<Criterion> getAllCriteria() {
                return criteria;
            }

            public List<Criterion> getCriteria() {
                return criteria;
            }

            protected void addCriterion(String condition) {
                if (condition == null) {
                    throw new RuntimeException("Value for condition cannot be null");
                }
                criteria.add(new Criterion(condition));
            }

            protected void addCriterion(String condition, Object value, String property) {
                if (value == null) {
                    throw new RuntimeException("Value for " + property + " cannot be null");
                }
                criteria.add(new Criterion(condition, value));
            }

            protected void addCriterion(String condition, Object value1, Object value2, String property) {
                if (value1 == null || value2 == null) {
                    throw new RuntimeException("Between values for " + property + " cannot be null");
                }
                criteria.add(new Criterion(condition, value1, value2));
            }

            public Criteria andIdIsNull() {
                addCriterion("id is null");
                return (Criteria) this;
            }

            public Criteria andIdIsNotNull() {
                addCriterion("id is not null");
                return (Criteria) this;
            }

            public Criteria andIdEqualTo(Long value) {
                addCriterion("id =", value, "id");
                return (Criteria) this;
            }

            public Criteria andIdNotEqualTo(Long value) {
                addCriterion("id <>", value, "id");
                return (Criteria) this;
            }

            public Criteria andIdGreaterThan(Long value) {
                addCriterion("id >", value, "id");
                return (Criteria) this;
            }

            public Criteria andIdGreaterThanOrEqualTo(Long value) {
                addCriterion("id >=", value, "id");
                return (Criteria) this;
            }

            public Criteria andIdLessThan(Long value) {
                addCriterion("id <", value, "id");
                return (Criteria) this;
            }

            public Criteria andIdLessThanOrEqualTo(Long value) {
                addCriterion("id <=", value, "id");
                return (Criteria) this;
            }

            public Criteria andIdIn(List<Long> values) {
                addCriterion("id in", values, "id");
                return (Criteria) this;
            }

            public Criteria andIdNotIn(List<Long> values) {
                addCriterion("id not in", values, "id");
                return (Criteria) this;
            }

            public Criteria andIdBetween(Long value1, Long value2) {
                addCriterion("id between", value1, value2, "id");
                return (Criteria) this;
            }

            public Criteria andIdNotBetween(Long value1, Long value2) {
                addCriterion("id not between", value1, value2, "id");
                return (Criteria) this;
            }

            public Criteria andGmtCreateIsNull() {
                addCriterion("gmt_create is null");
                return (Criteria) this;
            }

            public Criteria andGmtCreateIsNotNull() {
                addCriterion("gmt_create is not null");
                return (Criteria) this;
            }

            public Criteria andGmtCreateEqualTo(Date value) {
                addCriterion("gmt_create =", value, "gmtCreate");
                return (Criteria) this;
            }

            public Criteria andGmtCreateNotEqualTo(Date value) {
                addCriterion("gmt_create <>", value, "gmtCreate");
                return (Criteria) this;
            }

            public Criteria andGmtCreateGreaterThan(Date value) {
                addCriterion("gmt_create >", value, "gmtCreate");
                return (Criteria) this;
            }

            public Criteria andGmtCreateGreaterThanOrEqualTo(Date value) {
                addCriterion("gmt_create >=", value, "gmtCreate");
                return (Criteria) this;
            }

            public Criteria andGmtCreateLessThan(Date value) {
                addCriterion("gmt_create <", value, "gmtCreate");
                return (Criteria) this;
            }

            public Criteria andGmtCreateLessThanOrEqualTo(Date value) {
                addCriterion("gmt_create <=", value, "gmtCreate");
                return (Criteria) this;
            }

            public Criteria andGmtCreateIn(List<Date> values) {
                addCriterion("gmt_create in", values, "gmtCreate");
                return (Criteria) this;
            }

            public Criteria andGmtCreateNotIn(List<Date> values) {
                addCriterion("gmt_create not in", values, "gmtCreate");
                return (Criteria) this;
            }

            public Criteria andGmtCreateBetween(Date value1, Date value2) {
                addCriterion("gmt_create between", value1, value2, "gmtCreate");
                return (Criteria) this;
            }

            public Criteria andGmtCreateNotBetween(Date value1, Date value2) {
                addCriterion("gmt_create not between", value1, value2, "gmtCreate");
                return (Criteria) this;
            }

            public Criteria andGmtModifiedIsNull() {
                addCriterion("gmt_modified is null");
                return (Criteria) this;
            }

            public Criteria andGmtModifiedIsNotNull() {
                addCriterion("gmt_modified is not null");
                return (Criteria) this;
            }

            public Criteria andGmtModifiedEqualTo(Date value) {
                addCriterion("gmt_modified =", value, "gmtModified");
                return (Criteria) this;
            }

            public Criteria andGmtModifiedNotEqualTo(Date value) {
                addCriterion("gmt_modified <>", value, "gmtModified");
                return (Criteria) this;
            }

            public Criteria andGmtModifiedGreaterThan(Date value) {
                addCriterion("gmt_modified >", value, "gmtModified");
                return (Criteria) this;
            }

            public Criteria andGmtModifiedGreaterThanOrEqualTo(Date value) {
                addCriterion("gmt_modified >=", value, "gmtModified");
                return (Criteria) this;
            }

            public Criteria andGmtModifiedLessThan(Date value) {
                addCriterion("gmt_modified <", value, "gmtModified");
                return (Criteria) this;
            }

            public Criteria andGmtModifiedLessThanOrEqualTo(Date value) {
                addCriterion("gmt_modified <=", value, "gmtModified");
                return (Criteria) this;
            }

            public Criteria andGmtModifiedIn(List<Date> values) {
                addCriterion("gmt_modified in", values, "gmtModified");
                return (Criteria) this;
            }

            public Criteria andGmtModifiedNotIn(List<Date> values) {
                addCriterion("gmt_modified not in", values, "gmtModified");
                return (Criteria) this;
            }

            public Criteria andGmtModifiedBetween(Date value1, Date value2) {
                addCriterion("gmt_modified between", value1, value2, "gmtModified");
                return (Criteria) this;
            }

            public Criteria andGmtModifiedNotBetween(Date value1, Date value2) {
                addCriterion("gmt_modified not between", value1, value2, "gmtModified");
                return (Criteria) this;
            }

            public Criteria andAreaIdIsNull() {
                addCriterion("area_id is null");
                return (Criteria) this;
            }

            public Criteria andAreaIdIsNotNull() {
                addCriterion("area_id is not null");
                return (Criteria) this;
            }

            public Criteria andAreaIdEqualTo(Long value) {
                addCriterion("area_id =", value, "areaId");
                return (Criteria) this;
            }

            public Criteria andAreaIdNotEqualTo(Long value) {
                addCriterion("area_id <>", value, "areaId");
                return (Criteria) this;
            }

            public Criteria andAreaIdGreaterThan(Long value) {
                addCriterion("area_id >", value, "areaId");
                return (Criteria) this;
            }

            public Criteria andAreaIdGreaterThanOrEqualTo(Long value) {
                addCriterion("area_id >=", value, "areaId");
                return (Criteria) this;
            }

            public Criteria andAreaIdLessThan(Long value) {
                addCriterion("area_id <", value, "areaId");
                return (Criteria) this;
            }

            public Criteria andAreaIdLessThanOrEqualTo(Long value) {
                addCriterion("area_id <=", value, "areaId");
                return (Criteria) this;
            }

            public Criteria andAreaIdIn(List<Long> values) {
                addCriterion("area_id in", values, "areaId");
                return (Criteria) this;
            }

            public Criteria andAreaIdNotIn(List<Long> values) {
                addCriterion("area_id not in", values, "areaId");
                return (Criteria) this;
            }

            public Criteria andAreaIdBetween(Long value1, Long value2) {
                addCriterion("area_id between", value1, value2, "areaId");
                return (Criteria) this;
            }

            public Criteria andAreaIdNotBetween(Long value1, Long value2) {
                addCriterion("area_id not between", value1, value2, "areaId");
                return (Criteria) this;
            }

            public Criteria andAuthCodeIsNull() {
                addCriterion("auth_code is null");
                return (Criteria) this;
            }

            public Criteria andAuthCodeIsNotNull() {
                addCriterion("auth_code is not null");
                return (Criteria) this;
            }

            public Criteria andAuthCodeEqualTo(String value) {
                addCriterion("auth_code =", value, "authCode");
                return (Criteria) this;
            }

            public Criteria andAuthCodeNotEqualTo(String value) {
                addCriterion("auth_code <>", value, "authCode");
                return (Criteria) this;
            }

            public Criteria andAuthCodeGreaterThan(String value) {
                addCriterion("auth_code >", value, "authCode");
                return (Criteria) this;
            }

            public Criteria andAuthCodeGreaterThanOrEqualTo(String value) {
                addCriterion("auth_code >=", value, "authCode");
                return (Criteria) this;
            }

            public Criteria andAuthCodeLessThan(String value) {
                addCriterion("auth_code <", value, "authCode");
                return (Criteria) this;
            }

            public Criteria andAuthCodeLessThanOrEqualTo(String value) {
                addCriterion("auth_code <=", value, "authCode");
                return (Criteria) this;
            }

            public Criteria andAuthCodeLike(String value) {
                addCriterion("auth_code like", value, "authCode");
                return (Criteria) this;
            }

            public Criteria andAuthCodeNotLike(String value) {
                addCriterion("auth_code not like", value, "authCode");
                return (Criteria) this;
            }

            public Criteria andAuthCodeIn(List<String> values) {
                addCriterion("auth_code in", values, "authCode");
                return (Criteria) this;
            }

            public Criteria andAuthCodeNotIn(List<String> values) {
                addCriterion("auth_code not in", values, "authCode");
                return (Criteria) this;
            }

            public Criteria andAuthCodeBetween(String value1, String value2) {
                addCriterion("auth_code between", value1, value2, "authCode");
                return (Criteria) this;
            }

            public Criteria andAuthCodeNotBetween(String value1, String value2) {
                addCriterion("auth_code not between", value1, value2, "authCode");
                return (Criteria) this;
            }

            public Criteria andCabinetIdIsNull() {
                addCriterion("cabinet_id is null");
                return (Criteria) this;
            }

            public Criteria andCabinetIdIsNotNull() {
                addCriterion("cabinet_id is not null");
                return (Criteria) this;
            }

            public Criteria andCabinetIdEqualTo(Long value) {
                addCriterion("cabinet_id =", value, "cabinetId");
                return (Criteria) this;
            }

            public Criteria andCabinetIdNotEqualTo(Long value) {
                addCriterion("cabinet_id <>", value, "cabinetId");
                return (Criteria) this;
            }

            public Criteria andCabinetIdGreaterThan(Long value) {
                addCriterion("cabinet_id >", value, "cabinetId");
                return (Criteria) this;
            }

            public Criteria andCabinetIdGreaterThanOrEqualTo(Long value) {
                addCriterion("cabinet_id >=", value, "cabinetId");
                return (Criteria) this;
            }

            public Criteria andCabinetIdLessThan(Long value) {
                addCriterion("cabinet_id <", value, "cabinetId");
                return (Criteria) this;
            }

            public Criteria andCabinetIdLessThanOrEqualTo(Long value) {
                addCriterion("cabinet_id <=", value, "cabinetId");
                return (Criteria) this;
            }

            public Criteria andCabinetIdIn(List<Long> values) {
                addCriterion("cabinet_id in", values, "cabinetId");
                return (Criteria) this;
            }

            public Criteria andCabinetIdNotIn(List<Long> values) {
                addCriterion("cabinet_id not in", values, "cabinetId");
                return (Criteria) this;
            }

            public Criteria andCabinetIdBetween(Long value1, Long value2) {
                addCriterion("cabinet_id between", value1, value2, "cabinetId");
                return (Criteria) this;
            }

            public Criteria andCabinetIdNotBetween(Long value1, Long value2) {
                addCriterion("cabinet_id not between", value1, value2, "cabinetId");
                return (Criteria) this;
            }

            public Criteria andCabinetNoIsNull() {
                addCriterion("cabinet_no is null");
                return (Criteria) this;
            }

            public Criteria andCabinetNoIsNotNull() {
                addCriterion("cabinet_no is not null");
                return (Criteria) this;
            }

            public Criteria andCabinetNoEqualTo(String value) {
                addCriterion("cabinet_no =", value, "cabinetNo");
                return (Criteria) this;
            }

            public Criteria andCabinetNoNotEqualTo(String value) {
                addCriterion("cabinet_no <>", value, "cabinetNo");
                return (Criteria) this;
            }

            public Criteria andCabinetNoGreaterThan(String value) {
                addCriterion("cabinet_no >", value, "cabinetNo");
                return (Criteria) this;
            }

            public Criteria andCabinetNoGreaterThanOrEqualTo(String value) {
                addCriterion("cabinet_no >=", value, "cabinetNo");
                return (Criteria) this;
            }

            public Criteria andCabinetNoLessThan(String value) {
                addCriterion("cabinet_no <", value, "cabinetNo");
                return (Criteria) this;
            }

            public Criteria andCabinetNoLessThanOrEqualTo(String value) {
                addCriterion("cabinet_no <=", value, "cabinetNo");
                return (Criteria) this;
            }

            public Criteria andCabinetNoLike(String value) {
                addCriterion("cabinet_no like", value, "cabinetNo");
                return (Criteria) this;
            }

            public Criteria andCabinetNoNotLike(String value) {
                addCriterion("cabinet_no not like", value, "cabinetNo");
                return (Criteria) this;
            }

            public Criteria andCabinetNoIn(List<String> values) {
                addCriterion("cabinet_no in", values, "cabinetNo");
                return (Criteria) this;
            }

            public Criteria andCabinetNoNotIn(List<String> values) {
                addCriterion("cabinet_no not in", values, "cabinetNo");
                return (Criteria) this;
            }

            public Criteria andCabinetNoBetween(String value1, String value2) {
                addCriterion("cabinet_no between", value1, value2, "cabinetNo");
                return (Criteria) this;
            }

            public Criteria andCabinetNoNotBetween(String value1, String value2) {
                addCriterion("cabinet_no not between", value1, value2, "cabinetNo");
                return (Criteria) this;
            }

            public Criteria andStatusIsNull() {
                addCriterion("status is null");
                return (Criteria) this;
            }

            public Criteria andStatusIsNotNull() {
                addCriterion("status is not null");
                return (Criteria) this;
            }

            public Criteria andStatusEqualTo(Short value) {
                addCriterion("status =", value, "status");
                return (Criteria) this;
            }

            public Criteria andStatusNotEqualTo(Short value) {
                addCriterion("status <>", value, "status");
                return (Criteria) this;
            }

            public Criteria andStatusGreaterThan(Short value) {
                addCriterion("status >", value, "status");
                return (Criteria) this;
            }

            public Criteria andStatusGreaterThanOrEqualTo(Short value) {
                addCriterion("status >=", value, "status");
                return (Criteria) this;
            }

            public Criteria andStatusLessThan(Short value) {
                addCriterion("status <", value, "status");
                return (Criteria) this;
            }

            public Criteria andStatusLessThanOrEqualTo(Short value) {
                addCriterion("status <=", value, "status");
                return (Criteria) this;
            }

            public Criteria andStatusIn(List<Short> values) {
                addCriterion("status in", values, "status");
                return (Criteria) this;
            }

            public Criteria andStatusNotIn(List<Short> values) {
                addCriterion("status not in", values, "status");
                return (Criteria) this;
            }

            public Criteria andStatusBetween(Short value1, Short value2) {
                addCriterion("status between", value1, value2, "status");
                return (Criteria) this;
            }

            public Criteria andStatusNotBetween(Short value1, Short value2) {
                addCriterion("status not between", value1, value2, "status");
                return (Criteria) this;
            }

            public Criteria andAssignTimeIsNull() {
                addCriterion("assign_time is null");
                return (Criteria) this;
            }

            public Criteria andAssignTimeIsNotNull() {
                addCriterion("assign_time is not null");
                return (Criteria) this;
            }

            public Criteria andAssignTimeEqualTo(Date value) {
                addCriterion("assign_time =", value, "assignTime");
                return (Criteria) this;
            }

            public Criteria andAssignTimeNotEqualTo(Date value) {
                addCriterion("assign_time <>", value, "assignTime");
                return (Criteria) this;
            }

            public Criteria andAssignTimeGreaterThan(Date value) {
                addCriterion("assign_time >", value, "assignTime");
                return (Criteria) this;
            }

            public Criteria andAssignTimeGreaterThanOrEqualTo(Date value) {
                addCriterion("assign_time >=", value, "assignTime");
                return (Criteria) this;
            }

            public Criteria andAssignTimeLessThan(Date value) {
                addCriterion("assign_time <", value, "assignTime");
                return (Criteria) this;
            }

            public Criteria andAssignTimeLessThanOrEqualTo(Date value) {
                addCriterion("assign_time <=", value, "assignTime");
                return (Criteria) this;
            }

            public Criteria andAssignTimeIn(List<Date> values) {
                addCriterion("assign_time in", values, "assignTime");
                return (Criteria) this;
            }

            public Criteria andAssignTimeNotIn(List<Date> values) {
                addCriterion("assign_time not in", values, "assignTime");
                return (Criteria) this;
            }

            public Criteria andAssignTimeBetween(Date value1, Date value2) {
                addCriterion("assign_time between", value1, value2, "assignTime");
                return (Criteria) this;
            }

            public Criteria andAssignTimeNotBetween(Date value1, Date value2) {
                addCriterion("assign_time not between", value1, value2, "assignTime");
                return (Criteria) this;
            }

            public Criteria andUseTimeIsNull() {
                addCriterion("use_time is null");
                return (Criteria) this;
            }

            public Criteria andUseTimeIsNotNull() {
                addCriterion("use_time is not null");
                return (Criteria) this;
            }

            public Criteria andUseTimeEqualTo(Date value) {
                addCriterion("use_time =", value, "useTime");
                return (Criteria) this;
            }

            public Criteria andUseTimeNotEqualTo(Date value) {
                addCriterion("use_time <>", value, "useTime");
                return (Criteria) this;
            }

            public Criteria andUseTimeGreaterThan(Date value) {
                addCriterion("use_time >", value, "useTime");
                return (Criteria) this;
            }

            public Criteria andUseTimeGreaterThanOrEqualTo(Date value) {
                addCriterion("use_time >=", value, "useTime");
                return (Criteria) this;
            }

            public Criteria andUseTimeLessThan(Date value) {
                addCriterion("use_time <", value, "useTime");
                return (Criteria) this;
            }

            public Criteria andUseTimeLessThanOrEqualTo(Date value) {
                addCriterion("use_time <=", value, "useTime");
                return (Criteria) this;
            }

            public Criteria andUseTimeIn(List<Date> values) {
                addCriterion("use_time in", values, "useTime");
                return (Criteria) this;
            }

            public Criteria andUseTimeNotIn(List<Date> values) {
                addCriterion("use_time not in", values, "useTime");
                return (Criteria) this;
            }

            public Criteria andUseTimeBetween(Date value1, Date value2) {
                addCriterion("use_time between", value1, value2, "useTime");
                return (Criteria) this;
            }

            public Criteria andUseTimeNotBetween(Date value1, Date value2) {
                addCriterion("use_time not between", value1, value2, "useTime");
                return (Criteria) this;
            }

            public Criteria andBizTypeIsNull() {
                addCriterion("biz_type is null");
                return (Criteria) this;
            }

            public Criteria andBizTypeIsNotNull() {
                addCriterion("biz_type is not null");
                return (Criteria) this;
            }

            public Criteria andBizTypeEqualTo(Short value) {
                addCriterion("biz_type =", value, "bizType");
                return (Criteria) this;
            }

            public Criteria andBizTypeNotEqualTo(Short value) {
                addCriterion("biz_type <>", value, "bizType");
                return (Criteria) this;
            }

            public Criteria andBizTypeGreaterThan(Short value) {
                addCriterion("biz_type >", value, "bizType");
                return (Criteria) this;
            }

            public Criteria andBizTypeGreaterThanOrEqualTo(Short value) {
                addCriterion("biz_type >=", value, "bizType");
                return (Criteria) this;
            }

            public Criteria andBizTypeLessThan(Short value) {
                addCriterion("biz_type <", value, "bizType");
                return (Criteria) this;
            }

            public Criteria andBizTypeLessThanOrEqualTo(Short value) {
                addCriterion("biz_type <=", value, "bizType");
                return (Criteria) this;
            }

            public Criteria andBizTypeIn(List<Short> values) {
                addCriterion("biz_type in", values, "bizType");
                return (Criteria) this;
            }

            public Criteria andBizTypeNotIn(List<Short> values) {
                addCriterion("biz_type not in", values, "bizType");
                return (Criteria) this;
            }

            public Criteria andBizTypeBetween(Short value1, Short value2) {
                addCriterion("biz_type between", value1, value2, "bizType");
                return (Criteria) this;
            }

            public Criteria andBizTypeNotBetween(Short value1, Short value2) {
                addCriterion("biz_type not between", value1, value2, "bizType");
                return (Criteria) this;
            }

            public Criteria andBizOutIdIsNull() {
                addCriterion("biz_out_id is null");
                return (Criteria) this;
            }

            public Criteria andBizOutIdIsNotNull() {
                addCriterion("biz_out_id is not null");
                return (Criteria) this;
            }

            public Criteria andBizOutIdEqualTo(Long value) {
                addCriterion("biz_out_id =", value, "bizOutId");
                return (Criteria) this;
            }

            public Criteria andBizOutIdNotEqualTo(Long value) {
                addCriterion("biz_out_id <>", value, "bizOutId");
                return (Criteria) this;
            }

            public Criteria andBizOutIdGreaterThan(Long value) {
                addCriterion("biz_out_id >", value, "bizOutId");
                return (Criteria) this;
            }

            public Criteria andBizOutIdGreaterThanOrEqualTo(Long value) {
                addCriterion("biz_out_id >=", value, "bizOutId");
                return (Criteria) this;
            }

            public Criteria andBizOutIdLessThan(Long value) {
                addCriterion("biz_out_id <", value, "bizOutId");
                return (Criteria) this;
            }

            public Criteria andBizOutIdLessThanOrEqualTo(Long value) {
                addCriterion("biz_out_id <=", value, "bizOutId");
                return (Criteria) this;
            }

            public Criteria andBizOutIdIn(List<Long> values) {
                addCriterion("biz_out_id in", values, "bizOutId");
                return (Criteria) this;
            }

            public Criteria andBizOutIdNotIn(List<Long> values) {
                addCriterion("biz_out_id not in", values, "bizOutId");
                return (Criteria) this;
            }

            public Criteria andBizOutIdBetween(Long value1, Long value2) {
                addCriterion("biz_out_id between", value1, value2, "bizOutId");
                return (Criteria) this;
            }

            public Criteria andBizOutIdNotBetween(Long value1, Long value2) {
                addCriterion("biz_out_id not between", value1, value2, "bizOutId");
                return (Criteria) this;
            }

            public Criteria andFeatureIsNull() {
                addCriterion("feature is null");
                return (Criteria) this;
            }

            public Criteria andFeatureIsNotNull() {
                addCriterion("feature is not null");
                return (Criteria) this;
            }

            public Criteria andFeatureEqualTo(String value) {
                addCriterion("feature =", value, "feature");
                return (Criteria) this;
            }

            public Criteria andFeatureNotEqualTo(String value) {
                addCriterion("feature <>", value, "feature");
                return (Criteria) this;
            }

            public Criteria andFeatureGreaterThan(String value) {
                addCriterion("feature >", value, "feature");
                return (Criteria) this;
            }

            public Criteria andFeatureGreaterThanOrEqualTo(String value) {
                addCriterion("feature >=", value, "feature");
                return (Criteria) this;
            }

            public Criteria andFeatureLessThan(String value) {
                addCriterion("feature <", value, "feature");
                return (Criteria) this;
            }

            public Criteria andFeatureLessThanOrEqualTo(String value) {
                addCriterion("feature <=", value, "feature");
                return (Criteria) this;
            }

            public Criteria andFeatureLike(String value) {
                addCriterion("feature like", value, "feature");
                return (Criteria) this;
            }

            public Criteria andFeatureNotLike(String value) {
                addCriterion("feature not like", value, "feature");
                return (Criteria) this;
            }

            public Criteria andFeatureIn(List<String> values) {
                addCriterion("feature in", values, "feature");
                return (Criteria) this;
            }

            public Criteria andFeatureNotIn(List<String> values) {
                addCriterion("feature not in", values, "feature");
                return (Criteria) this;
            }

            public Criteria andFeatureBetween(String value1, String value2) {
                addCriterion("feature between", value1, value2, "feature");
                return (Criteria) this;
            }

            public Criteria andFeatureNotBetween(String value1, String value2) {
                addCriterion("feature not between", value1, value2, "feature");
                return (Criteria) this;
            }
        }

        public static class Criteria extends GeneratedCriteria {

            protected Criteria() {
                super();
            }
        }

        public static class Criterion {
            private String condition;

            private Object value;

            private Object secondValue;

            private boolean noValue;

            private boolean singleValue;

            private boolean betweenValue;

            private boolean listValue;

            private String typeHandler;

            public String getCondition() {
                return condition;
            }

            public Object getValue() {
                return value;
            }

            public Object getSecondValue() {
                return secondValue;
            }

            public boolean isNoValue() {
                return noValue;
            }

            public boolean isSingleValue() {
                return singleValue;
            }

            public boolean isBetweenValue() {
                return betweenValue;
            }

            public boolean isListValue() {
                return listValue;
            }

            public String getTypeHandler() {
                return typeHandler;
            }

            protected Criterion(String condition) {
                super();
                this.condition = condition;
                this.typeHandler = null;
                this.noValue = true;
            }

            protected Criterion(String condition, Object value, String typeHandler) {
                super();
                this.condition = condition;
                this.value = value;
                this.typeHandler = typeHandler;
                if (value instanceof List<?>) {
                    this.listValue = true;
                } else {
                    this.singleValue = true;
                }
            }

            protected Criterion(String condition, Object value) {
                this(condition, value, null);
            }

            protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
                super();
                this.condition = condition;
                this.value = value;
                this.secondValue = secondValue;
                this.typeHandler = typeHandler;
                this.betweenValue = true;
            }

            protected Criterion(String condition, Object value, Object secondValue) {
                this(condition, value, secondValue, null);
            }
        }

        public enum OrderCondition {
            ID("id"),
            GMTCREATE("gmt_create"),
            GMTMODIFIED("gmt_modified"),
            AREAID("area_id"),
            AUTHCODE("auth_code"),
            CABINETID("cabinet_id"),
            CABINETNO("cabinet_no"),
            STATUS("status"),
            ASSIGNTIME("assign_time"),
            USETIME("use_time"),
            BIZTYPE("biz_type"),
            BIZOUTID("biz_out_id"),
            FEATURE("feature");

            private String columnName;

            OrderCondition(String columnName) {
                this.columnName = columnName;
            }

            public String getColumnName() {
                return columnName;
            }

            public static OrderCondition getEnumByName(String name) {
                OrderCondition[] orderConditions = OrderCondition.values();
                for (OrderCondition orderCondition : orderConditions) {
                    if (orderCondition.name().equalsIgnoreCase(name)) {
                        return orderCondition;
                    }
                }
                throw new RuntimeException("OrderCondition of " + name + " enum not exist");
            }

            @Override
            public String toString() {
                return columnName;
            }
        }

        public enum SortType {
            ASC("asc"),
            DESC("desc");

            private String value;

            SortType(String value) {
                this.value = value;
            }

            public String getValue() {
                return value;
            }

            public static SortType getEnumByName(String name) {
                SortType[] sortTypes = SortType.values();
                for (SortType sortType : sortTypes) {
                    if (sortType.name().equalsIgnoreCase(name)) {
                        return sortType;
                    }
                }
                throw new RuntimeException("SortType of " + name + " enum not exist");
            }

            @Override
            public String toString() {
                return value;
            }
        }
    }
}
