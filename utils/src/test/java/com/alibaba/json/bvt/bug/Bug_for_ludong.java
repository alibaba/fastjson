package com.alibaba.json.bvt.bug;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class Bug_for_ludong extends TestCase {

    public void test_for_ludong() throws Exception {
        String msg = "{\"changedItems\":[{\"attribute\":\"new\",\"benefitCustomer\":\"chance130320584431\",\"benefitCustomerContactor\":5809917,\"benefitCustomerId\":2001385618,\"bizStatus\":\"audit_pass\",\"creator\":\"dowjons\",\"defaultBiz\":true,\"discountRate\":100,\"domain\":\"nirvana\",\"executeAmount\":3688,\"gmtCreate\":1367856000000,\"gmtModified\":1368374400000,\"gmtSign\":1367856000000,\"id\":321600616,\"isDeleted\":\"n\",\"itemNum\":\"W1305070000053_1\",\"lastOperType\":\"finance_pass_rollback\",\"memberId\":\"3592950865\",\"modifier\":\"haiquan.zhanghq\",\"num\":12,\"oppId\":103722314,\"orderId\":315749401,\"parentId\":0,\"paymentAmount\":0,\"paymentStatus\":\"payment_none\",\"policyId\":63149,\"price\":3688,\"productCode\":\"pc060\",\"purchaseType\":\"bought\",\"quotedPrice\":3688,\"salesId\":\"tiandan\",\"salesOrgFullid\":\"/10/1/30/101/160/1001/1051/\",\"serviceSupplyCompany\":\"B50\",\"signSalesId\":\"tiandan\",\"signSalesOrgFullId\":\"/10/1/30/101/160/1001/1051/\",\"traceChange\":true,\"ultimatePrice\":3688,\"unServiceDay\":0,\"unit\":\"M\",\"unvoucherAmount\":3688,\"voucherStatus\":\"voucher_none\"}],\"context\":{\"payAmount\":3688,\"payDate\":1368442850437,\"paymentStatus\":\"payment_success\"},\"generateTime\":1368442868624,\"msgType\":\"PAYMENT\",\"orderNumber\":\"W1305070000053\"}";

        OrderInternalDto dto = JSON.parseObject(msg, OrderInternalDto.class);
    }

    public static class OrderInternalDto implements Serializable {

        private static final long   serialVersionUID = 3228508302993121205L;

        /* 对象生成的时间 */

        private Date                generateTime;

        /** 订单号 */

        private String              orderNumber;

        /** 对象的业务状态 */

        private MSGTYPE             msgType;

        /** 订单的瞬时状态 */

        // private List<OrdOrderItemDto> instantItems;

        /** 发生状态变化的订单行列,比如到款只是对一个订单行发生到账行为 */

        private List<OrdOrderItem>  changedItems;

        /** 上下文参数 */

        private Map<String, Object> context;

        public OrderInternalDto(){

            this.generateTime = new Date();

            context = new HashMap<String, Object>();

        }

        public void setContext(Map<String, Object> context) {

            if (context == null) return;

            this.context = context;

        }

        @Override
        public String toString() {

            return JSON.toJSONString(this);

        }

    }

    public static class OrdOrderItem implements Cloneable, Serializable {

        public static String         ORDER_ID                   = "orderId";

        private Object               orderId;

        public static String         PARENT_ID                  = "parentId";

        private Integer              parentId;

        public static String         SERIAL_NUM                 = "serialNum";

        private String               serialNum;

        public static String         ITEM_NUM                   = "itemNum";

        private String               itemNum;

        public static String         PURCHASE_TYPE              = "purchaseType";

        private String               purchaseType;

        public static String         ATTRIBUTE                  = "attribute";

        private String               attribute;

        public static String         MEMBER_ID                  = "memberId";

        private String               memberId;

        public static String         PRODUCT_CODE               = "productCode";

        private String               productCode;

        public static String         NUM                        = "num";

        private Integer              num;

        public static String         UNIT                       = "unit";

        private String               unit;

        public static String         PRICE                      = "price";

        private java.math.BigDecimal price;

        public static String         DISCOUNT_RATE              = "discountRate";

        private java.math.BigDecimal discountRate;

        public static String         QUOTED_PRICE               = "quotedPrice";

        private java.math.BigDecimal quotedPrice;

        public static String         ULTIMATE_PRICE             = "ultimatePrice";

        private java.math.BigDecimal ultimatePrice;

        public static String         EXECUTE_AMOUNT             = "executeAmount";

        private java.math.BigDecimal executeAmount;

        public static String         GMT_TARGET_BEGIN           = "gmtTargetBegin";

        private java.util.Date       gmtTargetBegin;

        public static String         GMT_TARGET_END             = "gmtTargetEnd";

        private java.util.Date       gmtTargetEnd;

        public static String         GMT_ACTUAL_BEGIN           = "gmtActualBegin";

        private java.util.Date       gmtActualBegin;

        public static String         GMT_ACTUAL_END             = "gmtActualEnd";

        private java.util.Date       gmtActualEnd;

        public static String         SERVICE_SUPPLY_COMPANY     = "serviceSupplyCompany";

        private String               serviceSupplyCompany;

        public static String         BENEFIT_CUSTOMER           = "benefitCustomer";

        private String               benefitCustomer;

        public static String         BENEFIT_CUSTOMER_ID        = "benefitCustomerId";

        private Integer              benefitCustomerId;

        public static String         BENEFIT_CUSTOMER_CONTACTOR = "benefitCustomerContactor";

        private Integer              benefitCustomerContactor;

        public static String         BIZ_STATUS                 = "bizStatus";

        private String               bizStatus;

        public static String         VOUCHER_STATUS             = "voucherStatus";

        private String               voucherStatus;

        public static String         PAYMENT_STATUS             = "paymentStatus";

        private String               paymentStatus;

        public static String         PAYMENT_AMOUNT             = "paymentAmount";

        private java.math.BigDecimal paymentAmount;

        public static String         POLICY_ID                  = "policyId";

        private Integer              policyId;

        public static String         MEMO                       = "memo";

        private String               memo;

        public static String         SUPPORTER                  = "supporter";

        private String               supporter;

        public static String         SUPPORTER_ORG_ID           = "supporterOrgId";

        private Integer              supporterOrgId;

        public static String         SUPPORTER_ORG_FULLID       = "supporterOrgFullid";

        private String               supporterOrgFullid;

        public static String         SALES_ORG_FULLID           = "salesOrgFullid";

        private String               salesOrgFullid;

        public static String         SIGN_SALES_ORG_FULLID      = "signSalesOrgFullId";

        private String               signSalesOrgFullId;

        public static String         OPP_ID                     = "oppId";

        private Integer              oppId;

        public static String         DOMAIN                     = "domain";

        private String               domain;

        public static String         UN_SERVICE_DAY             = "unServiceDay";

        private java.math.BigDecimal unServiceDay;

        public static String         PROCESS_ID                 = "processId";

        private Long                 processId;

        public static String         LAST_OPER_TYPE             = "lastOperType";

        private String               lastOperType;

        public static String         UNVOUCHER_AMOUNT           = "unvoucherAmount";

        private java.math.BigDecimal unvoucherAmount;

        public static String         GMT_VOUCHER_RECEIVE        = "gmtVoucherReceive";

        private java.util.Date       gmtVoucherReceive;

        public static String         GMT_PAYMENT_REMIT          = "gmtPaymentRemit";

        private java.util.Date       gmtPaymentRemit;

        public static String         SERVICE_JUMP_DAYS          = "serviceJumpDays";

        private Integer              serviceJumpDays;

        public static String         SIGN_SALES_ID              = "signSalesId";

        private String               signSalesId;

        public static String         SALES_ORG_ID               = "salesOrgId";

        private Integer              salesOrgId;

        public static String         SIGN_SALES_ORG_ID          = "signSalesOrgId";

        private Integer              signSalesOrgId;

        public Integer getSignSalesOrgId() {

            return signSalesOrgId;

        }

        public void setSignSalesOrgId(Integer signSalesOrgId) {

            this.signSalesOrgId = signSalesOrgId;

        }

        public Integer getSalesOrgId() {

            return salesOrgId;

        }

        public void setSalesOrgId(Integer salesOrgId) {

            this.salesOrgId = salesOrgId;

        }

        public static String SIGN_SELLER_COMPANY = "signSellerCompany";

        private String       signSellerCompany;

        public static String BARGAIN_ID          = "bargainId";

        private Integer      bargainId;

        public Integer getBargainId() {

            return bargainId;

        }

        public void setBargainId(Integer bargainId) {

            this.bargainId = bargainId;

        }

        public String getSignSellerCompany() {

            return signSellerCompany;

        }

        public void setSignSellerCompany(String signSellerCompany) {

            this.signSellerCompany = signSellerCompany;

        }

        // 增加了新签和续签销售的id

        public static String SALES_ID = "salesId";

        private String       salesId;

        public String getSalesId() {

            return salesId;

        }

        public void setSalesId(String salesId) {

            this.salesId = salesId;

        }

        public String getRenewSalesId() {

            return renewSalesId;

        }

        public void setRenewSalesId(String renewSalesId) {

            this.renewSalesId = renewSalesId;

        }

        public static String   RENEW_SALES_ID = "renewSalesId";

        private String         renewSalesId;

        public static String   GMT_SIGN       = "gmtSign";

        private java.util.Date gmtSign;

        public Object getOrderId() {

            return this.orderId;

        }

        public void setOrderId(Object orderId) {

            this.orderId = orderId;

        }

        public Integer getParentId() {

            return this.parentId;

        }

        public void setParentId(Integer parentId) {

            this.parentId = parentId;

        }

        public String getSerialNum() {

            return this.serialNum;

        }

        public void setSerialNum(String serialNum) {

            this.serialNum = serialNum;

        }

        public String getItemNum() {

            return this.itemNum;

        }

        public void setItemNum(String itemNum) {

            this.itemNum = itemNum;

        }

        public String getPurchaseType() {

            return this.purchaseType;

        }

        public void setPurchaseType(String purchaseType) {

            this.purchaseType = purchaseType;

        }

        public String getAttribute() {

            return this.attribute;

        }

        public void setAttribute(String attribute) {

            this.attribute = attribute;

        }

        public String getMemberId() {

            return this.memberId;

        }

        public void setMemberId(String memberId) {

            this.memberId = memberId;

        }

        public String getProductCode() {

            return this.productCode;

        }

        public void setProductCode(String productCode) {

            this.productCode = productCode;

        }

        public Integer getNum() {

            return this.num;

        }

        public void setNum(Integer num) {

            this.num = num;

        }

        public String getUnit() {

            return this.unit;

        }

        public void setUnit(String unit) {

            this.unit = unit;

        }

        public java.math.BigDecimal getPrice() {

            return this.price;

        }

        public void setPrice(java.math.BigDecimal price) {

            this.price = price;

        }

        public java.math.BigDecimal getDiscountRate() {

            return this.discountRate;

        }

        public void setDiscountRate(java.math.BigDecimal discountRate) {

            this.discountRate = discountRate;

        }

        public java.math.BigDecimal getQuotedPrice() {

            return this.quotedPrice;

        }

        public void setQuotedPrice(java.math.BigDecimal quotedPrice) {

            this.quotedPrice = quotedPrice;

        }

        public java.math.BigDecimal getUltimatePrice() {

            return this.ultimatePrice;

        }

        public void setUltimatePrice(java.math.BigDecimal ultimatePrice) {

            this.ultimatePrice = ultimatePrice;

        }

        public java.math.BigDecimal getExecuteAmount() {

            return this.executeAmount;

        }

        public void setExecuteAmount(java.math.BigDecimal executeAmount) {

            this.executeAmount = executeAmount;

        }

        public java.util.Date getGmtTargetBegin() {

            return this.gmtTargetBegin;

        }

        public void setGmtTargetBegin(java.util.Date gmtTargetBegin) {

            this.gmtTargetBegin = gmtTargetBegin;

        }

        public java.util.Date getGmtTargetEnd() {

            return this.gmtTargetEnd;

        }

        public void setGmtTargetEnd(java.util.Date gmtTargetEnd) {

            this.gmtTargetEnd = gmtTargetEnd;

        }

        public java.util.Date getGmtActualBegin() {

            return this.gmtActualBegin;

        }

        public void setGmtActualBegin(java.util.Date gmtActualBegin) {

            this.gmtActualBegin = gmtActualBegin;

        }

        public java.util.Date getGmtActualEnd() {

            return this.gmtActualEnd;

        }

        public void setGmtActualEnd(java.util.Date gmtActualEnd) {

            this.gmtActualEnd = gmtActualEnd;

        }

        public String getServiceSupplyCompany() {

            return this.serviceSupplyCompany;

        }

        public void setServiceSupplyCompany(String serviceSupplyCompany) {

            this.serviceSupplyCompany = serviceSupplyCompany;

        }

        public String getBenefitCustomer() {

            return this.benefitCustomer;

        }

        public void setBenefitCustomer(String benefitCustomer) {

            this.benefitCustomer = benefitCustomer;

        }

        public Integer getBenefitCustomerId() {

            return this.benefitCustomerId;

        }

        public void setBenefitCustomerId(Integer benefitCustomerId) {

            this.benefitCustomerId = benefitCustomerId;

        }

        public Integer getBenefitCustomerContactor() {

            return this.benefitCustomerContactor;

        }

        public void setBenefitCustomerContactor(Integer benefitCustomerContactor) {

            this.benefitCustomerContactor = benefitCustomerContactor;

        }

        public String getBizStatus() {

            return this.bizStatus;

        }

        public void setBizStatus(String bizStatus) {

            this.bizStatus = bizStatus;

        }

        public String getVoucherStatus() {

            return this.voucherStatus;

        }

        public void setVoucherStatus(String voucherStatus) {

            this.voucherStatus = voucherStatus;

        }

        public String getPaymentStatus() {

            return this.paymentStatus;

        }

        public void setPaymentStatus(String paymentStatus) {

            this.paymentStatus = paymentStatus;

        }

        public java.math.BigDecimal getPaymentAmount() {

            return this.paymentAmount;

        }

        public void setPaymentAmount(java.math.BigDecimal paymentAmount) {

            this.paymentAmount = paymentAmount;

        }

        public Integer getPolicyId() {

            return this.policyId;

        }

        public void setPolicyId(Integer policyId) {

            this.policyId = policyId;

        }

        public String getMemo() {

            return this.memo;

        }

        public void setMemo(String memo) {

            this.memo = memo;

        }

        public String getSupporter() {

            return this.supporter;

        }

        public void setSupporter(String supporter) {

            this.supporter = supporter;

        }

        public Integer getSupporterOrgId() {

            return this.supporterOrgId;

        }

        public void setSupporterOrgId(Integer supporterOrgId) {

            this.supporterOrgId = supporterOrgId;

        }

        public String getSupporterOrgFullid() {

            return this.supporterOrgFullid;

        }

        public void setSupporterOrgFullid(String supporterOrgFullid) {

            this.supporterOrgFullid = supporterOrgFullid;

        }

        public Integer getOppId() {

            return this.oppId;

        }

        public void setOppId(Integer oppId) {

            this.oppId = oppId;

        }

        public String getDomain() {

            return this.domain;

        }

        public void setDomain(String domain) {

            this.domain = domain;

        }

        public java.math.BigDecimal getUnServiceDay() {

            return this.unServiceDay;

        }

        public void setUnServiceDay(java.math.BigDecimal unServiceDay) {

            this.unServiceDay = unServiceDay;

        }

        public Long getProcessId() {

            return this.processId;

        }

        public void setProcessId(Long processId) {

            this.processId = processId;

        }

        public String getLastOperType() {

            return this.lastOperType;

        }

        public void setLastOperType(String lastOperType) {

            this.lastOperType = lastOperType;

        }

        public java.math.BigDecimal getUnvoucherAmount() {

            return this.unvoucherAmount;

        }

        public void setUnvoucherAmount(java.math.BigDecimal unvoucherAmount) {

            this.unvoucherAmount = unvoucherAmount;

        }

        public java.util.Date getGmtVoucherReceive() {

            return this.gmtVoucherReceive;

        }

        public void setGmtVoucherReceive(java.util.Date gmtVoucherReceive) {

            this.gmtVoucherReceive = gmtVoucherReceive;

        }

        public java.util.Date getGmtPaymentRemit() {

            return this.gmtPaymentRemit;

        }

        public void setGmtPaymentRemit(java.util.Date gmtPaymentRemit) {

            this.gmtPaymentRemit = gmtPaymentRemit;

        }

        public Integer getServiceJumpDays() {

            return this.serviceJumpDays;

        }

        public void setServiceJumpDays(Integer serviceJumpDays) {

            this.serviceJumpDays = serviceJumpDays;

        }

        @Override
        public Object clone() throws CloneNotSupportedException {

            return super.clone();

        }

        public String getSignSalesId() {

            return signSalesId;

        }

        public void setSignSalesId(String signSalesId) {

            this.signSalesId = signSalesId;

        }

        public String getSalesOrgFullid() {

            return salesOrgFullid;

        }

        public void setSalesOrgFullid(String salesOrgFullid) {

            this.salesOrgFullid = salesOrgFullid;

        }

        public String getSignSalesOrgFullId() {

            return signSalesOrgFullId;

        }

        public void setSignSalesOrgFullId(String signSalesOrgFullId) {

            this.signSalesOrgFullId = signSalesOrgFullId;

        }

        public java.util.Date getGmtSign() {

            return gmtSign;

        }

        public void setGmtSign(java.util.Date gmtSign) {

            this.gmtSign = gmtSign;

        }

    }

    public static class MSGTYPE {

        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }
}
