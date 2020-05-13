package com.alibaba.json.bvt.parser.bug;

import java.util.List;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class Bug_for_yihaodian extends TestCase {

    public void test_for_long_list() throws Exception {
        String str = "{\"backOperatorId\":14281,\"batchNum\":0,\"canPurchase\":1,\"categoryId\":955063}";
        Te ob = JSON.parseObject(str, Te.class);
    }

    public static class Te {

        /** 产品ID */
        private Long         id;
        /** 要删除产品的ID */
        private String       deletedProductId;
        /** 产品编码 */
        private String       productCode;
        /** 产品名 */
        private String       productCname;
        /** 产品名前面的品牌名 */
        private String       productBrandName;
        /** 产品名英文 */
        private String       productEname;
        /** 产品销售类别 */
        private Integer      productSaleType;
        /** 产品品牌Id */
        private Long         brandId;
        /** 产品品牌 */
        private String       brandName;

        /** 市场价 */
        private Double       productListPrice;
        /** 分类Id */
        private Long         categoryId;
        /** 旧分类Id */
        private Long         oldCategoryId;
        /** 旧扩展类别 **/
        private Long         oldExtendCategoryId;
        /** 厂商ID,默认为1 */
        private Long         mfid;
        /** productCanBeChange */
        private Integer      productCanBeChange;
        /** productChangeDay */
        private Integer      productChangeDay;
        /** productCanBeReturn */
        private Integer      productCanBeReturn;
        /** productReturnDay */
        private Integer      productReturnDay;
        /** 能否维修 */
        private Integer      productCanBeRepair;
        /** 能否维修 */
        private Integer      productCanBeRepairDay;
        /** 安装信息 */
        private Integer      productNeedInstallation;
        /** 条形码 */
        private String       ean13;
        /** sku */
        private String       sku;
        /** 长 */
        private Double       length;
        /** 宽 */
        private Double       width;
        /** 高 */
        private Double       height;
        /** 净重 */
        private Double       weight;
        /** keepTemperature */
        private String       keepTemperature;
        /** keepHumidity */
        private String       keepHumidity;
        /** 产品简称 */
        private String       productSname;
        /** keepSpecCondition */
        private String       keepSpecCondition;
        /** productQualityAssuranceDay */
        private Integer      productQualityAssuranceDay;
        /** 是否已删除 */
        private Integer      isDeleted;
        /** 单位 */
        private String       unit;
        /** 进价 */
        private Double       inPrice;
        /** volume */
        private Double       volume;
        /** countryOfOrgn */
        private Long         countryOfOrgn;
        /** 主图ID */
        private Long         defaultPictureId;
        /** 主图URL */
        private String       defaultPictureUrl;
        /** color */
        private String       color;
        /** currencyId */
        private Long         currencyId;
        /** 毛重 */
        private Double       grossWeight;
        /** format */
        private String       format;
        /** 易碎品 0 不是 1是 */
        private String       isFragile;
        /** 向上0 不是 1是 */
        private String       putOnDirection;
        /** 贵重品0 不是 1是 */
        private String       isValuables;
        /** 液体0 不是 1是 */
        private String       isLiquid;
        /** 防交叉污染0 不是 1是 */
        private String       isCrossContamination;
        /** 16进制的颜色代码,如#FF00AA */
        private String       colorNumber;
        /** 尺码 */
        private String       productSize;
        /** 替换后的尺码 */
        private String       replaceProductSize;
        /** 销售技巧 */
        private String       saleSkill;
        /** 本商品作为赠品时的处理方法 */
        private String       dispositionInstruct;
        /** 产地 */
        private String       placeOfOrigin;
        /** 产品页面标题 */
        private String       productSeoTitle;
        /** 产品页面属性关键字 */
        private String       productSeoKeyword;
        /** 产品页面属性描述 */
        private String       productSeoDescription;
        /** 后台产品配件说明 */
        private String       accessoryDescription;
        /** 是否需要单独开票 */
        private Integer      needInvoice;
        /** 清仓原因 */
        private String       clearCause;
        /** 默认商品条码ID */
        private Long         defaultBarcodeId;
        /** 广告词 */
        private String       adWord;
        /** 是否是3c产品（0:非3C,1:3C产品） */
        private Integer      isCcc;
        /** N件购 */
        private Integer      shoppingCount;
        /** 是否为赠品 */
        private Integer      productIsGift;
        /** 是否可以退换货 0：不可以 1：可以 */
        private Integer      canReturnAndChange;
        /** 是否需要检测 0：不需要 1：需要 */
        private Integer      needExamine;
        /** 1:新增未审核;2:编辑待审核;3:审核未通过;4:审核通过;5:文描审核失败;6:图片审核失败 */
        private Integer      verifyFlg;
        /** 审核者 */
        private Long         verifyBy;
        /** 审核日时 */

        /** 商品登记者 */
        private Long         registerBy;
        /** 商品登记日时 */

        /** 商品登记者联系电话 */
        private String       registerPhone;
        /** 审核备注 */
        private String       verifyRemark;
        /** 批量数 */
        private Integer      batchNum;
        /** 是否只限本地配送0: 不限制 1:限制 (粉状/液体/膏状) */
        private Integer      localLimit;
        /** 一包的数量 */
        private Integer      stdPackQty;
        /** 正式表产品ID */
        private Long         fromalProductId;
        /** 是否强制发票 */
        private Integer      isMustInvoice;
        /** 审核失败原因 */
        private Integer      verifyFailureType;
        /** 产品类型 0：普通产品 1：主系列产品 2：子系列产品 3：捆绑产品 4：礼品卡 5: 虚拟商品 6:增值服务 */
        private Integer      productType;
        /** 是否能被采购 */
        private Integer      canPurchase;
        /** 标准包装箱sku */
        private String       stdPackageSku;
        /** 是否需要启用保质期控制 0:不启用 1：启用 */
        private Integer      userExpireControl;
        /** 批次规则ID */
        private Long         batchRuleId;
        /** 产品名称副标题 */
        private String       nameSubtitle;
        private String       specialType;
        /** 给经销商的批发价 */
        private Double       batchPrice;
        /** 是否需要批次控制 0：不需要 1：需要 */
        private Integer      needBatchControl;
        /** 销售税率 */
        private Double       salesTax;
        /** 外部产品编码 */
        private String       outerId;
        /** 商家ID */
        private Long         merchantId;
        /** 商家名称 */
        private String       merchantName;
        /** 商家产品主类别（用于报表统计） */
        private Long         masterCategoryId;

        private Integer      concernLevel;
        /** 关注理由 */
        private String       concernReason;
        /** 是否可售 */
        private Integer      canSale;
        /** 是否显示 */
        private Integer      canShow;
        /** 产品销售税率 */
        private Long         prodcutTaxRate;
        /** 是否支持VIP0:不支持1:支持 */
        private Integer      canVipDiscount;
        /** 分类名称 */
        private String       categoryName;
        /** 销售价格 */
        private Double       salePrice;
        /** 库存 */
        private Long         stockNum;
        /** 商家类别名称 */
        private String       merchantCategoryName;
        /** 商家详情 */
        private String       productDescription;
        /** 是否可调拨 0：不可以 1：可以 */
        private Integer      isTransfer;
        /** 是否需要审核0：新增未提交；1：需要审核；2：编辑未提交 */
        private Integer      isSubmit;
        /** 审核失败类型 */
        private Integer      verifyFailueType;
        /** 产品拼音 */
        private String       productSpell;
        /** 产品名称前缀 */
        private String       productNamePrefix;
        /** 审核失败原因 */
        private String       failueReason;
        /** orgPicUrl */
        private String       orgPicUrl;
        /** 扩展分类名称 */
        private String       subCategoryName;
        /** 扩展分类ID */
        private Long         subCategoryId;
        /** 7天内日均销量 */
        private Integer      dailySale;
        /** 查看是否有主图 */
        private Integer      picCount;
        /** 强制下架原因 */
        private Integer      underCarriageReason;
        /** 强制下架原因-中文信息 */
        private String       underCarriageReasonStr;
        /** 异常信息 */
        private String       errorMessage;
        /** 库存预警数量 */
        private Integer      alertStockCount;

        private String       deliveryInfo;
        /** 主图链接 */
        private String       picUrl;

        /** 是否能分期0：不能 1：能 */
        private Integer      canFenqi;

        private String       season;
        /** 是否是二次审核 */
        private Integer      isDupAudit;

        private Integer      viewFromTag;

        /** 产品售价 */
        private Double       productNonMemberPrice;
        /** 产品图片 */

        /** 是否更新操作 */
        private Integer      isUpdate;
        /** merchantRpcVo */

        /** 系列产品的颜色图片 */

        /** 系列产品的尺码 */
        private List<String> productSizeSet;

        /** 是否主产品 */
        private Boolean      isMainProduct;
        /** 从图片空间中返回图片ID和URL */
        private String       productPicIdAndURL;

        private Integer      isTemp;
        /** 市场价和售价的比例 */
        private Double       priceRate;

        private Integer      picSpecialType;
        private Integer      exemptStatus;

        private String       violationReasonIds;

        private String       violationReasons;

        private Long         remainTime;

        private Integer      submitOrder;

        private Integer      productSource;

        private Integer      isKa;
        /** KA商家创建时间 */
        private Integer      kaMCreateTime;
        /** 配送延长期 */
        private Integer      deliveryDay;
        /** 产品状态 */
        private Integer      isEdit;
        /** 操作人 */
        private Long         backOperatorId;
        /** 正式库pm_info_id */
        private Long         formalPmInfoId;
        /** 类别拼接字符串 */
        private String       categoryStr;
        /** 类别id拼接字符串 */
        private String       categoryIdStr;
        /** 扩展类别拼接字符串 */
        private String       extendCategoryStr;
        /** 扩展类别id拼接字符串 */
        private String       extendCategoryIdStr;
        /** 商家类别ID */
        private List<Long>   masterCategoryIdList;

        private Long         defaultWarehouseId;

        public Long getBackOperatorId() {
            return backOperatorId;
        }

        public void setBackOperatorId(Long backOperatorId) {
            this.backOperatorId = backOperatorId;
        }

        public Integer getIsDupAudit() {
            return isDupAudit;
        }

        public void setIsDupAudit(Integer isDupAudit) {
            this.isDupAudit = isDupAudit;
        }

        public Long getId() {
            return id;
        }

        public String getUnderCarriageReasonStr() {
            return underCarriageReasonStr;
        }

        public void setUnderCarriageReasonStr(String underCarriageReasonStr) {
            this.underCarriageReasonStr = underCarriageReasonStr;
        }

        /**
         * 产品ID
         * 
         * @param id 产品ID
         */

        public void setId(Long id) {
            this.id = id;
        }

        /**
         * 产品编码
         * 
         * @return productCode
         */

        public String getProductCode() {
            return productCode;
        }

        /**
         * 产品编码
         * 
         * @param productCode 产品编码
         */

        public void setProductCode(String productCode) {
            this.productCode = productCode;
        }

        /**
         * 产品名
         * 
         * @return productCname
         */

        public String getProductCname() {
            return productCname;
        }

        /**
         * 产品名
         * 
         * @param productCname 产品名
         */

        public void setProductCname(String productCname) {
            this.productCname = productCname;
        }

        /**
         * 产品名英文
         * 
         * @return productEname
         */

        public String getProductEname() {
            return productEname;
        }

        /**
         * 产品名英文
         * 
         * @param productEname 产品名英文
         */

        public void setProductEname(String productEname) {
            this.productEname = productEname;
        }


        /**
         * 产品销售类别
         * 
         * @param productSaleType 产品销售类别
         */

        public void setProductSaleType(Integer productSaleType) {
            this.productSaleType = productSaleType;
        }

        /**
         * 产品品牌Id
         * 
         * @return brandId
         */

        public Long getBrandId() {
            return brandId;
        }

        /**
         * 产品品牌Id
         * 
         * @param brandId 产品品牌Id
         */

        public void setBrandId(Long brandId) {
            this.brandId = brandId;
        }

        /**
         * 产品品牌
         * 
         * @return brandName
         */

        public String getBrandName() {
            return brandName;
        }

        /**
         * 产品品牌
         * 
         * @param brandName 产品品牌
         */

        public void setBrandName(String brandName) {
            this.brandName = brandName;
        }

        /**
         * 产品创建时间
         * 
         * @return createTime
         */

        /**
         * 产品创建时间
         * 
         * @param createTime 产品创建时间
         */

        /**
         * 市场价
         * 
         * @return productListPrice
         */

        public Double getProductListPrice() {
            return productListPrice;
        }

        /**
         * 市场价
         * 
         * @param productListPrice 市场价
         */

        public void setProductListPrice(Double productListPrice) {
            this.productListPrice = productListPrice;
        }

        /**
         * 分类Id
         * 
         * @return categoryId
         */

        public Long getCategoryId() {
            return categoryId;
        }

        /**
         * 分类Id
         * 
         * @param categoryId 分类Id
         */

        public void setCategoryId(Long categoryId) {
            this.categoryId = categoryId;
        }

        /**
         * 厂商ID默认为1
         * 
         * @return mfid
         */

        public Long getMfid() {
            return mfid;
        }

        /**
         * 厂商ID默认为1
         * 
         * @param mfid 厂商ID默认为1
         */

        public void setMfid(Long mfid) {
            this.mfid = mfid;
        }

        /**
         * productCanBeChange
         * 
         * @return productCanBeChange
         */

        public Integer getProductCanBeChange() {
            return productCanBeChange;
        }

        /**
         * productCanBeChange
         * 
         * @param productCanBeChange productCanBeChange
         */

        public void setProductCanBeChange(Integer productCanBeChange) {
            this.productCanBeChange = productCanBeChange;
        }

        /**
         * productChangeDay
         * 
         * @return productChangeDay
         */

        public Integer getProductChangeDay() {
            return productChangeDay;
        }

        /**
         * productChangeDay
         * 
         * @param productChangeDay productChangeDay
         */

        public void setProductChangeDay(Integer productChangeDay) {
            this.productChangeDay = productChangeDay;
        }

        /**
         * productCanBeReturn
         * 
         * @return productCanBeReturn
         */

        public Integer getProductCanBeReturn() {
            return productCanBeReturn;
        }

        /**
         * productCanBeReturn
         * 
         * @param productCanBeReturn productCanBeReturn
         */

        public void setProductCanBeReturn(Integer productCanBeReturn) {
            this.productCanBeReturn = productCanBeReturn;
        }

        /**
         * productReturnDay
         * 
         * @return productReturnDay
         */

        public Integer getProductReturnDay() {
            return productReturnDay;
        }

        /**
         * productReturnDay
         * 
         * @param productReturnDay productReturnDay
         */

        public void setProductReturnDay(Integer productReturnDay) {
            this.productReturnDay = productReturnDay;
        }

        /**
         * 能否维修
         * 
         * @return productCanBeRepair
         */

        public Integer getProductCanBeRepair() {
            return productCanBeRepair;
        }

        /**
         * 能否维修
         * 
         * @param productCanBeRepair 能否维修
         */

        public void setProductCanBeRepair(Integer productCanBeRepair) {
            this.productCanBeRepair = productCanBeRepair;
        }

        /**
         * 能否维修
         * 
         * @return productCanBeRepairDay
         */

        public Integer getProductCanBeRepairDay() {
            return productCanBeRepairDay;
        }

        /**
         * 能否维修
         * 
         * @param productCanBeRepairDay 能否维修
         */

        public void setProductCanBeRepairDay(Integer productCanBeRepairDay) {
            this.productCanBeRepairDay = productCanBeRepairDay;
        }

        /**
         * 安装信息
         * 
         * @return productNeedInstallation
         */

        public Integer getProductNeedInstallation() {
            return productNeedInstallation;
        }

        /**
         * 安装信息
         * 
         * @param productNeedInstallation 安装信息
         */

        public void setProductNeedInstallation(Integer productNeedInstallation) {
            this.productNeedInstallation = productNeedInstallation;
        }

        /**
         * 条形码
         * 
         * @return ean13
         */

        public String getEan13() {
            return ean13;
        }

        /**
         * 条形码
         * 
         * @param ean13 条形码
         */

        public void setEan13(String ean13) {
            this.ean13 = ean13;
        }

        /**
         * sku
         * 
         * @return sku
         */

        public String getSku() {
            return sku;
        }

        /**
         * sku
         * 
         * @param sku sku
         */

        public void setSku(String sku) {
            this.sku = sku;
        }

        /**
         * 长
         * 
         * @return length
         */

        public Double getLength() {
            return length;
        }

        /**
         * 长
         * 
         * @param length 长
         */

        public void setLength(Double length) {
            this.length = length;
        }

        /**
         * 宽
         * 
         * @return width
         */

        public Double getWidth() {
            return width;
        }

        /**
         * 宽
         * 
         * @param width 宽
         */

        public void setWidth(Double width) {
            this.width = width;
        }

        /**
         * 高
         * 
         * @return height
         */

        public Double getHeight() {
            return height;
        }

        /**
         * 高
         * 
         * @param height 高
         */

        public void setHeight(Double height) {
            this.height = height;
        }

        /**
         * 净重
         * 
         * @return weight
         */

        public Double getWeight() {
            return weight;
        }

        /**
         * 净重
         * 
         * @param weight 净重
         */

        public void setWeight(Double weight) {
            this.weight = weight;
        }

        /**
         * keepTemperature
         * 
         * @return keepTemperature
         */

        public String getKeepTemperature() {
            return keepTemperature;
        }

        /**
         * keepTemperature
         * 
         * @param keepTemperature keepTemperature
         */

        public void setKeepTemperature(String keepTemperature) {
            this.keepTemperature = keepTemperature;
        }

        /**
         * keepHumidity
         * 
         * @return keepHumidity
         */

        public String getKeepHumidity() {
            return keepHumidity;
        }

        /**
         * keepHumidity
         * 
         * @param keepHumidity keepHumidity
         */

        public void setKeepHumidity(String keepHumidity) {
            this.keepHumidity = keepHumidity;
        }

        /**
         * keepSpecCondition
         * 
         * @return keepSpecCondition
         */

        public String getKeepSpecCondition() {
            return keepSpecCondition;
        }

        /**
         * keepSpecCondition
         * 
         * @param keepSpecCondition keepSpecCondition
         */

        public void setKeepSpecCondition(String keepSpecCondition) {
            this.keepSpecCondition = keepSpecCondition;
        }

        /**
         * productQualityAssuranceDay
         * 
         * @return productQualityAssuranceDay
         */

        public Integer getProductQualityAssuranceDay() {
            return productQualityAssuranceDay;
        }

        /**
         * productQualityAssuranceDay
         * 
         * @param productQualityAssuranceDay productQualityAssuranceDay
         */

        public void setProductQualityAssuranceDay(Integer productQualityAssuranceDay) {
            this.productQualityAssuranceDay = productQualityAssuranceDay;
        }

        /**
         * 是否已删除
         * 
         * @return isDeleted
         */

        public Integer getIsDeleted() {
            return isDeleted;
        }

        /**
         * 是否已删除
         * 
         * @param isDeleted 是否已删除
         */

        public void setIsDeleted(Integer isDeleted) {
            this.isDeleted = isDeleted;
        }

        /**
         * 单位
         * 
         * @return unit
         */

        public String getUnit() {
            return unit;
        }

        /**
         * 单位
         * 
         * @param unit 单位
         */

        public void setUnit(String unit) {
            this.unit = unit;
        }

        /**
         * 进价
         * 
         * @return inPrice
         */

        public Double getInPrice() {
            return inPrice;
        }

        /**
         * 进价
         * 
         * @param inPrice 进价
         */

        public void setInPrice(Double inPrice) {
            this.inPrice = inPrice;
        }

        /**
         * volume
         * 
         * @return volume
         */

        public Double getVolume() {
            return volume;
        }

        /**
         * volume
         * 
         * @param volume volume
         */

        public void setVolume(Double volume) {
            this.volume = volume;
        }

        /**
         * countryOfOrgn
         * 
         * @return countryOfOrgn
         */

        public Long getCountryOfOrgn() {
            return countryOfOrgn;
        }

        /**
         * countryOfOrgn
         * 
         * @param countryOfOrgn countryOfOrgn
         */

        public void setCountryOfOrgn(Long countryOfOrgn) {
            this.countryOfOrgn = countryOfOrgn;
        }

        /**
         * 主图ID
         * 
         * @return defaultPictureId
         */

        public Long getDefaultPictureId() {
            return defaultPictureId;
        }

        /**
         * 主图ID
         * 
         * @param defaultPictureId 主图ID
         */

        public void setDefaultPictureId(Long defaultPictureId) {
            this.defaultPictureId = defaultPictureId;
        }

        /**
         * 主图URL
         * 
         * @return defaultPictureUrl
         */

        public String getDefaultPictureUrl() {

            return defaultPictureUrl;
        }

        /**
         * 主图URL
         * 
         * @param defaultPictureUrl 主图URL
         */

        public void setDefaultPictureUrl(String defaultPictureUrl) {
            this.defaultPictureUrl = defaultPictureUrl;
        }

        /**
         * color
         * 
         * @return color
         */

        public String getColor() {
            return color;
        }

        /**
         * color
         * 
         * @param color color
         */

        public void setColor(String color) {
            this.color = color;
        }

        /**
         * currencyId
         * 
         * @return currencyId
         */

        public Long getCurrencyId() {
            return currencyId;
        }

        /**
         * currencyId
         * 
         * @param currencyId currencyId
         */

        public void setCurrencyId(Long currencyId) {
            this.currencyId = currencyId;
        }

        /**
         * 毛重
         * 
         * @return grossWeight
         */

        public Double getGrossWeight() {
            return grossWeight;
        }

        /**
         * 毛重
         * 
         * @param grossWeight 毛重
         */

        public void setGrossWeight(Double grossWeight) {
            this.grossWeight = grossWeight;
        }

        /**
         * format
         * 
         * @return format
         */

        public String getFormat() {
            return format;
        }

        /**
         * format
         * 
         * @param format format
         */

        public void setFormat(String format) {
            this.format = format;
        }

        /**
         * 易碎品0不是1是
         * 
         * @return isFragile
         */

        public String getIsFragile() {
            return isFragile;
        }

        /**
         * 易碎品0不是1是
         * 
         * @param isFragile 易碎品0不是1是
         */

        public void setIsFragile(String isFragile) {
            this.isFragile = isFragile;
        }

        /**
         * 向上0不是1是
         * 
         * @return putOnDirection
         */

        public String getPutOnDirection() {
            return putOnDirection;
        }

        /**
         * 向上0不是1是
         * 
         * @param putOnDirection 向上0不是1是
         */

        public void setPutOnDirection(String putOnDirection) {
            this.putOnDirection = putOnDirection;
        }

        /**
         * 贵重品0不是1是
         * 
         * @return isValuables
         */

        public String getIsValuables() {
            return isValuables;
        }

        /**
         * 贵重品0不是1是
         * 
         * @param isValuables 贵重品0不是1是
         */

        public void setIsValuables(String isValuables) {
            this.isValuables = isValuables;
        }

        /**
         * 液体0不是1是
         * 
         * @return isLiquid
         */

        public String getIsLiquid() {
            return isLiquid;
        }

        /**
         * 液体0不是1是
         * 
         * @param isLiquid 液体0不是1是
         */

        public void setIsLiquid(String isLiquid) {
            this.isLiquid = isLiquid;
        }

        /**
         * 防交叉污染0不是1是
         * 
         * @return isCrossContamination
         */

        public String getIsCrossContamination() {
            return isCrossContamination;
        }

        /**
         * 防交叉污染0不是1是
         * 
         * @param isCrossContamination 防交叉污染0不是1是
         */

        public void setIsCrossContamination(String isCrossContamination) {
            this.isCrossContamination = isCrossContamination;
        }

        /**
         * 16进制的颜色代码如#FF00AA
         * 
         * @return colorNumber
         */

        public String getColorNumber() {
            return colorNumber;
        }

        /**
         * 16进制的颜色代码如#FF00AA
         * 
         * @param colorNumber 16进制的颜色代码如#FF00AA
         */

        public void setColorNumber(String colorNumber) {
            this.colorNumber = colorNumber;
        }

        /**
         * 尺码
         * 
         * @return productSize
         */

        public String getProductSize() {
            return productSize;
        }

        /**
         * 尺码
         * 
         * @param productSize 尺码
         */

        public void setProductSize(String productSize) {
            this.productSize = productSize;
        }

        /**
         * 销售技巧
         * 
         * @return saleSkill
         */

        public String getSaleSkill() {
            return saleSkill;
        }

        /**
         * 销售技巧
         * 
         * @param saleSkill 销售技巧
         */

        public void setSaleSkill(String saleSkill) {
            this.saleSkill = saleSkill;
        }

        /**
         * 本商品作为赠品时的处理方法
         * 
         * @return dispositionInstruct
         */

        public String getDispositionInstruct() {
            return dispositionInstruct;
        }

        /**
         * 本商品作为赠品时的处理方法
         * 
         * @param dispositionInstruct 本商品作为赠品时的处理方法
         */

        public void setDispositionInstruct(String dispositionInstruct) {
            this.dispositionInstruct = dispositionInstruct;
        }

        /**
         * 产地
         * 
         * @return placeOfOrigin
         */

        public String getPlaceOfOrigin() {
            return placeOfOrigin;
        }

        /**
         * 产地
         * 
         * @param placeOfOrigin 产地
         */

        public void setPlaceOfOrigin(String placeOfOrigin) {
            this.placeOfOrigin = placeOfOrigin;
        }

        /**
         * 产品页面标题
         * 
         * @return productSeoTitle
         */

        public String getProductSeoTitle() {
            return productSeoTitle;
        }

        /**
         * 产品页面标题
         * 
         * @param productSeoTitle 产品页面标题
         */

        public void setProductSeoTitle(String productSeoTitle) {
            this.productSeoTitle = productSeoTitle;
        }

        /**
         * 产品页面属性关键字
         * 
         * @return productSeoKeyword
         */

        public String getProductSeoKeyword() {
            return productSeoKeyword;
        }

        /**
         * 产品页面属性关键字
         * 
         * @param productSeoKeyword 产品页面属性关键字
         */

        public void setProductSeoKeyword(String productSeoKeyword) {
            this.productSeoKeyword = productSeoKeyword;
        }

        /**
         * 产品页面属性描述
         * 
         * @return productSeoDescription
         */

        public String getProductSeoDescription() {
            return productSeoDescription;
        }

        /**
         * 产品页面属性描述
         * 
         * @param productSeoDescription 产品页面属性描述
         */

        public void setProductSeoDescription(String productSeoDescription) {
            this.productSeoDescription = productSeoDescription;
        }

        /**
         * 后台产品配件说明
         * 
         * @return accessoryDescription
         */

        public String getAccessoryDescription() {
            return accessoryDescription;
        }

        /**
         * 后台产品配件说明
         * 
         * @param accessoryDescription 后台产品配件说明
         */

        public void setAccessoryDescription(String accessoryDescription) {
            this.accessoryDescription = accessoryDescription;
        }

        /**
         * 是否需要单独开票
         * 
         * @return needInvoice
         */

        public Integer getNeedInvoice() {
            return needInvoice;
        }

        /**
         * 是否需要单独开票
         * 
         * @param needInvoice 是否需要单独开票
         */

        public void setNeedInvoice(Integer needInvoice) {
            this.needInvoice = needInvoice;
        }

        /**
         * 清仓原因
         * 
         * @return clearCause
         */

        public String getClearCause() {
            return clearCause;
        }

        /**
         * 清仓原因
         * 
         * @param clearCause 清仓原因
         */

        public void setClearCause(String clearCause) {
            this.clearCause = clearCause;
        }

        /**
         * 默认商品条码ID
         * 
         * @return defaultBarcodeId
         */

        public Long getDefaultBarcodeId() {
            return defaultBarcodeId;
        }

        /**
         * 默认商品条码ID
         * 
         * @param defaultBarcodeId 默认商品条码ID
         */

        public void setDefaultBarcodeId(Long defaultBarcodeId) {
            this.defaultBarcodeId = defaultBarcodeId;
        }

        /**
         * 广告词
         * 
         * @return adWord
         */

        public String getAdWord() {
            return adWord;
        }

        /**
         * 广告词
         * 
         * @param adWord 广告词
         */

        public void setAdWord(String adWord) {
            this.adWord = adWord;
        }

        /**
         * 是否是3c产品（0:非3C1:3C产品）
         * 
         * @return isCcc
         */

        public Integer getIsCcc() {
            return isCcc;
        }

        /**
         * 是否是3c产品（0:非3C1:3C产品）
         * 
         * @param isCcc 是否是3c产品（0:非3C1:3C产品）
         */

        public void setIsCcc(Integer isCcc) {
            this.isCcc = isCcc;
        }

        /**
         * N件购
         * 
         * @return shoppingCount
         */

        public Integer getShoppingCount() {
            return shoppingCount;
        }

        /**
         * N件购
         * 
         * @param shoppingCount N件购
         */

        public void setShoppingCount(Integer shoppingCount) {
            this.shoppingCount = shoppingCount;
        }

        /**
         * 是否为赠品
         * 
         * @return productIsGift
         */

        public Integer getProductIsGift() {
            return productIsGift;
        }

        /**
         * 是否为赠品
         * 
         * @param productIsGift 是否为赠品
         */

        public void setProductIsGift(Integer productIsGift) {
            this.productIsGift = productIsGift;
        }

        /**
         * 是否可以退换货0：不可以1：可以
         * 
         * @return canReturnAndChange
         */

        public Integer getCanReturnAndChange() {
            return canReturnAndChange;
        }

        /**
         * 是否可以退换货0：不可以1：可以
         * 
         * @param canReturnAndChange 是否可以退换货0：不可以1：可以
         */

        public void setCanReturnAndChange(Integer canReturnAndChange) {
            this.canReturnAndChange = canReturnAndChange;
        }

        /**
         * 是否需要检测0：不需要1：需要
         * 
         * @return needExamine
         */

        public Integer getNeedExamine() {
            return needExamine;
        }

        /**
         * 是否需要检测0：不需要1：需要
         * 
         * @param needExamine 是否需要检测0：不需要1：需要
         */

        public void setNeedExamine(Integer needExamine) {
            this.needExamine = needExamine;
        }

        /**
         * 1:新增未审核;2:编辑待审核;3:审核未通过;4:审核通过;5:文描审核失败;6:图片审核失败
         * 
         * @return verifyFlg
         */

        public Integer getVerifyFlg() {
            return verifyFlg;
        }

        /**
         * 1:新增未审核;2:编辑待审核;3:审核未通过;4:审核通过;5:文描审核失败;6:图片审核失败
         * 
         * @param verifyFlg 1:新增未审核;2:编辑待审核;3:审核未通过;4:审核通过;5:文描审核失败;6:图片审核失败
         */

        public void setVerifyFlg(Integer verifyFlg) {
            this.verifyFlg = verifyFlg;
        }

        /**
         * 审核者
         * 
         * @return verifyBy
         */

        public Long getVerifyBy() {
            return verifyBy;
        }

        /**
         * 审核者
         * 
         * @param verifyBy 审核者
         */

        public void setVerifyBy(Long verifyBy) {
            this.verifyBy = verifyBy;
        }

        /**
         * 审核日时
         * 
         * @return verifyAt
         */

        /**
         * 审核日时
         * 
         * @param verifyAt 审核日时
         */

        /**
         * 商品登记者
         * 
         * @return registerBy
         */

        public Long getRegisterBy() {
            return registerBy;
        }

        /**
         * 商品登记者
         * 
         * @param registerBy 商品登记者
         */

        public void setRegisterBy(Long registerBy) {
            this.registerBy = registerBy;
        }

        /**
         * 商品登记日时
         * 
         * @return registerAt
         */

        /**
         * 商品登记日时
         * 
         * @param registerAt 商品登记日时
         */

        /**
         * 商品登记者联系电话
         * 
         * @return registerPhone
         */

        public String getRegisterPhone() {
            return registerPhone;
        }

        /**
         * 商品登记者联系电话
         * 
         * @param registerPhone 商品登记者联系电话
         */

        public void setRegisterPhone(String registerPhone) {
            this.registerPhone = registerPhone;
        }

        /**
         * 审核备注
         * 
         * @return verifyRemark
         */

        public String getVerifyRemark() {
            return verifyRemark;
        }

        /**
         * 审核备注
         * 
         * @param verifyRemark 审核备注
         */

        public void setVerifyRemark(String verifyRemark) {
            this.verifyRemark = verifyRemark;
        }

        /**
         * 批量数
         * 
         * @return batchNum
         */

        public Integer getBatchNum() {
            return batchNum;
        }

        /**
         * 批量数
         * 
         * @param batchNum 批量数
         */

        public void setBatchNum(Integer batchNum) {
            this.batchNum = batchNum;
        }

        /**
         * 是否只限本地配送0:不限制1:限制(粉状液体膏状)
         * 
         * @return localLimit
         */

        public Integer getLocalLimit() {
            return localLimit;
        }

        /**
         * 是否只限本地配送0:不限制1:限制(粉状液体膏状)
         * 
         * @param localLimit 是否只限本地配送0:不限制1:限制(粉状液体膏状)
         */

        public void setLocalLimit(Integer localLimit) {
            this.localLimit = localLimit;
        }

        /**
         * 一包的数量
         * 
         * @return stdPackQty
         */

        public Integer getStdPackQty() {
            return stdPackQty;
        }

        /**
         * 一包的数量
         * 
         * @param stdPackQty 一包的数量
         */

        public void setStdPackQty(Integer stdPackQty) {
            this.stdPackQty = stdPackQty;
        }

        /**
         * 正式表产品ID
         * 
         * @return fromalProductId
         */

        public Long getFromalProductId() {
            return fromalProductId;
        }

        /**
         * 正式表产品ID
         * 
         * @param fromalProductId 正式表产品ID
         */

        public void setFromalProductId(Long fromalProductId) {
            this.fromalProductId = fromalProductId;
        }

        /**
         * 是否强制发票
         * 
         * @return isMustInvoice
         */

        public Integer getIsMustInvoice() {
            return isMustInvoice;
        }

        /**
         * 是否强制发票
         * 
         * @param isMustInvoice 是否强制发票
         */

        public void setIsMustInvoice(Integer isMustInvoice) {
            this.isMustInvoice = isMustInvoice;
        }

        /**
         * 审核失败原因
         * 
         * @return verifyFailureType
         */

        public Integer getVerifyFailureType() {
            return verifyFailureType;
        }

        /**
         * 审核失败原因
         * 
         * @param verifyFailureType 审核失败原因
         */

        public void setVerifyFailureType(Integer verifyFailureType) {
            this.verifyFailureType = verifyFailureType;
        }

        /**
         * 产品类型0：普通产品1：主系列产品2：子系列产品3：捆绑产品4：礼品卡5:虚拟商品6:增值服务
         * 
         * @return productType
         */

        public Integer getProductType() {
            return productType;
        }

        /**
         * 产品类型0：普通产品1：主系列产品2：子系列产品3：捆绑产品4：礼品卡5:虚拟商品6:增值服务
         * 
         * @param productType 产品类型0：普通产品1：主系列产品2：子系列产品3：捆绑产品4：礼品卡5:虚拟商品6:增值服务
         */

        public void setProductType(Integer productType) {
            this.productType = productType;
        }

        /**
         * 是否能被采购
         * 
         * @return canPurchase
         */

        public Integer getCanPurchase() {
            return canPurchase;
        }

        /**
         * 是否能被采购
         * 
         * @param canPurchase 是否能被采购
         */

        public void setCanPurchase(Integer canPurchase) {
            this.canPurchase = canPurchase;
        }

        /**
         * 标准包装箱sku
         * 
         * @return stdPackageSku
         */

        public String getStdPackageSku() {
            return stdPackageSku;
        }

        /**
         * 标准包装箱sku
         * 
         * @param stdPackageSku 标准包装箱sku
         */

        public void setStdPackageSku(String stdPackageSku) {
            this.stdPackageSku = stdPackageSku;
        }

        /**
         * 是否需要启用保质期控制0:不启用1：启用
         * 
         * @return userExpireControl
         */

        public Integer getUserExpireControl() {
            return userExpireControl;
        }

        /**
         * 是否需要启用保质期控制0:不启用1：启用
         * 
         * @param userExpireControl 是否需要启用保质期控制0:不启用1：启用
         */

        public void setUserExpireControl(Integer userExpireControl) {
            this.userExpireControl = userExpireControl;
        }

        /**
         * 批次规则ID
         * 
         * @return batchRuleId
         */

        public Long getBatchRuleId() {
            return batchRuleId;
        }

        /**
         * 批次规则ID
         * 
         * @param batchRuleId 批次规则ID
         */

        public void setBatchRuleId(Long batchRuleId) {
            this.batchRuleId = batchRuleId;
        }

        /**
         * 产品名称副标题
         * 
         * @return nameSubtitle
         */

        public String getNameSubtitle() {
            return nameSubtitle;
        }

        /**
         * 产品名称副标题
         * 
         * @param nameSubtitle 产品名称副标题
         */

        public void setNameSubtitle(String nameSubtitle) {
            this.nameSubtitle = nameSubtitle;
        }

        /**
         * 产品特殊类型：1：医药；11：药品；12器械；14-18:处方药；50：电子凭证
         * 
         * @return specialType
         */

        public String getSpecialType() {
            return specialType;
        }

        /**
         * 产品特殊类型：1：医药；11：药品；12器械；14-18:处方药；50：电子凭证
         * 
         * @param specialType 产品特殊类型：1：医药；11：药品；12器械；14-18:处方药；50：电子凭证
         */

        public void setSpecialType(String specialType) {
            this.specialType = specialType;
        }

        /**
         * 给经销商的批发价
         * 
         * @return batchPrice
         */

        public Double getBatchPrice() {
            return batchPrice;
        }

        /**
         * 给经销商的批发价
         * 
         * @param batchPrice 给经销商的批发价
         */

        public void setBatchPrice(Double batchPrice) {
            this.batchPrice = batchPrice;
        }

        /**
         * 是否需要批次控制0：不需要1：需要
         * 
         * @return needBatchControl
         */

        public Integer getNeedBatchControl() {
            return needBatchControl;
        }

        /**
         * 是否需要批次控制0：不需要1：需要
         * 
         * @param needBatchControl 是否需要批次控制0：不需要1：需要
         */

        public void setNeedBatchControl(Integer needBatchControl) {
            this.needBatchControl = needBatchControl;
        }

        /**
         * 销售税率
         * 
         * @return salesTax
         */

        public Double getSalesTax() {
            return salesTax;
        }

        /**
         * 销售税率
         * 
         * @param salesTax 销售税率
         */

        public void setSalesTax(Double salesTax) {
            this.salesTax = salesTax;
        }

        /**
         * 外部产品编码
         * 
         * @return outerId
         */

        public String getOuterId() {
            return outerId;
        }

        /**
         * 外部产品编码
         * 
         * @param outerId 外部产品编码
         */

        public void setOuterId(String outerId) {
            this.outerId = outerId;
        }

        /**
         * 商家ID
         * 
         * @return merchantId
         */

        public Long getMerchantId() {
            return merchantId;
        }

        /**
         * 商家ID
         * 
         * @param merchantId 商家ID
         */

        public void setMerchantId(Long merchantId) {
            this.merchantId = merchantId;
        }

        /**
         * 商家名称
         * 
         * @return merchantName
         */

        public String getMerchantName() {
            return merchantName;
        }

        /**
         * 商家名称
         * 
         * @param merchantName 商家名称
         */

        public void setMerchantName(String merchantName) {
            this.merchantName = merchantName;
        }

        /**
         * 商家产品主类别（用于报表统计）
         * 
         * @return masterCategoryId
         */

        public Long getMasterCategoryId() {
            return masterCategoryId;
        }

        /**
         * 商家产品主类别（用于报表统计）
         * 
         * @param masterCategoryId 商家产品主类别（用于报表统计）
         */

        public void setMasterCategoryId(Long masterCategoryId) {
            this.masterCategoryId = masterCategoryId;
        }

        /**
         * 关注等级设置
         * 
         * @return concernLevel
         */

        public Integer getConcernLevel() {
            return concernLevel;
        }

        /**
         * 关注等级设置
         * 
         * @param concernLevel 关注等级设置
         */

        public void setConcernLevel(Integer concernLevel) {
            this.concernLevel = concernLevel;
        }

        /**
         * 关注理由
         * 
         * @return concernReason
         */

        public String getConcernReason() {
            return concernReason;
        }

        /**
         * 关注理由
         * 
         * @param concernReason 关注理由
         */

        public void setConcernReason(String concernReason) {
            this.concernReason = concernReason;
        }

        /**
         * 是否可售
         * 
         * @return canSale
         */

        public Integer getCanSale() {
            return canSale;
        }

        /**
         * 是否可售
         * 
         * @param canSale 是否可售
         */

        public void setCanSale(Integer canSale) {
            this.canSale = canSale;
        }

        /**
         * 是否显示
         * 
         * @return canShow
         */

        public Integer getCanShow() {
            return canShow;
        }

        /**
         * 是否显示
         * 
         * @param canShow 是否显示
         */

        public void setCanShow(Integer canShow) {
            this.canShow = canShow;
        }

        /**
         * 产品销售税率
         * 
         * @return prodcutTaxRate
         */

        public Long getProdcutTaxRate() {
            return prodcutTaxRate;
        }

        /**
         * 产品销售税率
         * 
         * @param prodcutTaxRate 产品销售税率
         */

        public void setProdcutTaxRate(Long prodcutTaxRate) {
            this.prodcutTaxRate = prodcutTaxRate;
        }

        /**
         * 是否支持VIP0:不支持1:支持
         * 
         * @return canVipDiscount
         */

        public Integer getCanVipDiscount() {
            return canVipDiscount;
        }

        /**
         * 是否支持VIP0:不支持1:支持
         * 
         * @param canVipDiscount 是否支持VIP0:不支持1:支持
         */

        public void setCanVipDiscount(Integer canVipDiscount) {
            this.canVipDiscount = canVipDiscount;
        }

        /**
         * 分类名称
         * 
         * @return categoryName
         */

        public String getCategoryName() {
            return categoryName;
        }

        /**
         * 分类名称
         * 
         * @param categoryName 分类名称
         */

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        /**
         * 销售价格
         * 
         * @return salePrice
         */

        public Double getSalePrice() {
            return salePrice;
        }

        /**
         * 销售价格
         * 
         * @param salePrice 销售价格
         */

        public void setSalePrice(Double salePrice) {
            this.salePrice = salePrice;
        }

        /**
         * 库存
         * 
         * @return stockNum
         */

        public Long getStockNum() {
            return stockNum;
        }

        /**
         * 库存
         * 
         * @param stockNum 库存
         */

        public void setStockNum(Long stockNum) {
            this.stockNum = stockNum;
        }

        /**
         * 商家类别名称
         * 
         * @return merchantCategoryName
         */

        public String getMerchantCategoryName() {
            return merchantCategoryName;
        }

        /**
         * 商家类别名称
         * 
         * @param merchantCategoryName 商家类别名称
         */

        public void setMerchantCategoryName(String merchantCategoryName) {
            this.merchantCategoryName = merchantCategoryName;
        }

        /**
         * 商家详情
         * 
         * @return productDescription
         */

        public String getProductDescription() {
            return productDescription;
        }

        /**
         * 商家详情
         * 
         * @param productDescription 商家详情
         */

        public void setProductDescription(String productDescription) {
            this.productDescription = productDescription;
        }

        /**
         * 是否可调拨0：不可以1：可以
         * 
         * @return isTransfer
         */

        public Integer getIsTransfer() {
            return isTransfer;
        }

        /**
         * 是否可调拨0：不可以1：可以
         * 
         * @param isTransfer 是否可调拨0：不可以1：可以
         */

        public void setIsTransfer(Integer isTransfer) {
            this.isTransfer = isTransfer;
        }

        /**
         * 是否需要审核0：新增未提交；1：需要审核；2：编辑未提交
         * 
         * @return isSubmit
         */

        public Integer getIsSubmit() {
            return isSubmit;
        }

        /**
         * 是否需要审核0：新增未提交；1：需要审核；2：编辑未提交
         * 
         * @param isSubmit 是否需要审核0：新增未提交；1：需要审核；2：编辑未提交
         */

        public void setIsSubmit(Integer isSubmit) {
            this.isSubmit = isSubmit;
        }

        /**
         * 审核失败类型
         * 
         * @return verifyFailueType
         */

        public Integer getVerifyFailueType() {
            return verifyFailueType;
        }

        /**
         * 审核失败类型
         * 
         * @param verifyFailueType 审核失败类型
         */

        public void setVerifyFailueType(Integer verifyFailueType) {
            this.verifyFailueType = verifyFailueType;
        }

        /**
         * 产品拼音
         * 
         * @return productSpell
         */

        public String getProductSpell() {
            return productSpell;
        }

        /**
         * 产品拼音
         * 
         * @param productSpell 产品拼音
         */

        public void setProductSpell(String productSpell) {
            this.productSpell = productSpell;
        }

        /**
         * 产品名称前缀
         * 
         * @return productNamePrefix
         */

        public String getProductNamePrefix() {
            return productNamePrefix;
        }

        /**
         * 产品名称前缀
         * 
         * @param productNamePrefix 产品名称前缀
         */

        public void setProductNamePrefix(String productNamePrefix) {
            this.productNamePrefix = productNamePrefix;
        }

        /**
         * 审核失败原因
         * 
         * @return failueReason
         */

        public String getFailueReason() {
            return failueReason;
        }

        /**
         * 审核失败原因
         * 
         * @param failueReason 审核失败原因
         */

        public void setFailueReason(String failueReason) {
            this.failueReason = failueReason;
        }

        /**
         * orgPicUrl
         * 
         * @return orgPicUrl
         */

        public String getOrgPicUrl() {

            return orgPicUrl;
        }

        /**
         * orgPicUrl
         * 
         * @param orgPicUrl orgPicUrl
         */

        public void setOrgPicUrl(String orgPicUrl) {
            this.orgPicUrl = orgPicUrl;
        }

        /**
         * 扩展分类名称
         * 
         * @return subCategoryName
         */

        public String getSubCategoryName() {
            return subCategoryName;
        }

        /**
         * 扩展分类名称
         * 
         * @param subCategoryName 扩展分类名称
         */

        public void setSubCategoryName(String subCategoryName) {
            this.subCategoryName = subCategoryName;
        }

        /**
         * 扩展分类ID
         * 
         * @return subCategoryId
         */

        public Long getSubCategoryId() {
            return subCategoryId;
        }

        /**
         * 扩展分类ID
         * 
         * @param subCategoryId 扩展分类ID
         */

        public void setSubCategoryId(Long subCategoryId) {
            this.subCategoryId = subCategoryId;
        }

        /**
         * 7天内日均销量
         * 
         * @return dailySale
         */

        public Integer getDailySale() {
            return dailySale;
        }

        /**
         * 7天内日均销量
         * 
         * @param dailySale 7天内日均销量
         */

        public void setDailySale(Integer dailySale) {
            this.dailySale = dailySale;
        }

        /**
         * 查看是否有主图
         * 
         * @return picCount
         */

        public Integer getPicCount() {
            return picCount;
        }

        /**
         * 查看是否有主图
         * 
         * @param picCount 查看是否有主图
         */

        public void setPicCount(Integer picCount) {
            this.picCount = picCount;
        }

        /**
         * 强制下架原因
         * 
         * @return underCarriageReason
         */

        public Integer getUnderCarriageReason() {
            return underCarriageReason;
        }

        /**
         * 强制下架原因
         * 
         * @param underCarriageReason 强制下架原因
         */

        public void setUnderCarriageReason(Integer underCarriageReason) {
            this.underCarriageReason = underCarriageReason;
        }

        /**
         * 异常信息
         * 
         * @return errorMessage
         */

        public String getErrorMessage() {
            return errorMessage;
        }

        /**
         * 异常信息
         * 
         * @param errorMessage 异常信息
         */
        /**
         * public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; } 库存预警数量
         * 
         * @return alertStockCount
         */

        public Integer getAlertStockCount() {
            return alertStockCount;
        }

        /**
         * 库存预警数量
         * 
         * @param alertStockCount 库存预警数量
         */

        public void setAlertStockCount(Integer alertStockCount) {
            this.alertStockCount = alertStockCount;
        }

        /**
         * 提交时间
         * 
         * @return submitTime
         */
        /**
         * public Date getSubmitTime() { return submitTime; } 提交时间
         * 
         * @param submitTime 提交时间
         */
        /**
         * public void setSubmitTime(Date submitTime) { this.submitTime = submitTime; } holdPmPriceRpcVo
         * 
         * @return holdPmPriceRpcVo
         */

        /**
         * holdPmPriceRpcVo
         * 
         * @param holdPmPrice holdPmPriceRpcVo
         */

        /**
         * pmPriceRpcVo
         * 
         * @return pmPriceRpcVo
         */
        /**
         * public PmPriceRpcVo getPmPrice() { return pmPrice; } pmPriceRpcVo
         * 
         * @param pmPrice pmPriceRpcVo public void setPmPrice(PmPriceRpcVo pmPrice) { this.pmPrice = pmPrice; }
         */
        public Long getFormalPmInfoId() {
            return formalPmInfoId;
        }

        public void setFormalPmInfoId(Long formalPmInfoId) {
            this.formalPmInfoId = formalPmInfoId;
        }

        /**
         * 库存状况（产品预览页用）
         * 
         * @return deliveryInfo
         */

        public String getDeliveryInfo() {
            return deliveryInfo;
        }

        /**
         * 库存状况（产品预览页用）
         * 
         * @param deliveryInfo 库存状况（产品预览页用）
         */

        public void setDeliveryInfo(String deliveryInfo) {
            this.deliveryInfo = deliveryInfo;
        }

        /**
         * 主图链接
         * 
         * @return picUrl
         */

        public String getPicUrl() {
            return picUrl;
        }

        /**
         * 主图链接
         * 
         * @param picUrl 主图链接
         */

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }

        /**
         * 跳到商品详情页的来源0:首次审核页面1：二次审核页面2：审核失败页面
         * 
         * @return viewFromTag
         */

        public Integer getViewFromTag() {
            return viewFromTag;
        }

        /**
         * 跳到商品详情页的来源0:首次审核页面1：二次审核页面2：审核失败页面
         * 
         * @param viewFromTag 跳到商品详情页的来源0:首次审核页面1：二次审核页面2：审核失败页面
         */

        public void setViewFromTag(Integer viewFromTag) {
            this.viewFromTag = viewFromTag;
        }

        public Double getProductNonMemberPrice() {
            return productNonMemberPrice;
        }

        /**
         * 产品售价
         * 
         * @param productNonMemberPrice 产品售价
         */

        public void setProductNonMemberPrice(Double productNonMemberPrice) {
            this.productNonMemberPrice = productNonMemberPrice;
        }

        public Integer getIsUpdate() {
            return isUpdate;
        }

        /**
         * 是否更新操作
         * 
         * @param isUpdate 是否更新操作
         */

        public void setIsUpdate(Integer isUpdate) {
            this.isUpdate = isUpdate;
        }

        public List<String> getProductSizeSet() {
            return productSizeSet;
        }

        public void setProductSizeSet(List<String> productSizeSet) {
            this.productSizeSet = productSizeSet;
        }

        public Boolean getIsMainProduct() {
            return isMainProduct;
        }

        /**
         * 是否主产品
         * 
         * @param isMainProduct 是否主产品
         */

        public void setIsMainProduct(Boolean isMainProduct) {
            this.isMainProduct = isMainProduct;
        }

        /**
         * 从图片空间中返回图片ID和URL
         * 
         * @return productPicIdAndURL
         */

        public String getProductPicIdAndURL() {
            return productPicIdAndURL;
        }

        /**
         * 从图片空间中返回图片ID和URL
         * 
         * @param productPicIdAndURL 从图片空间中返回图片ID和URL
         */

        public void setProductPicIdAndURL(String productPicIdAndURL) {
            this.productPicIdAndURL = productPicIdAndURL;
        }

        public Integer getIsTemp() {
            return isTemp;
        }

        /**
         * isTemp
         * 
         * @param isTemp isTemp
         */

        public void setIsTemp(Integer isTemp) {
            this.isTemp = isTemp;
        }

        public Double getPriceRate() {
            return priceRate;
        }

        public void setPriceRate(Double priceRate) {
            this.priceRate = priceRate;
        }

        public Integer getPicSpecialType() {
            return picSpecialType;
        }

        public void setPicSpecialType(Integer picSpecialType) {
            this.picSpecialType = picSpecialType;
        }

        public Integer getExemptStatus() {
            return exemptStatus;
        }

        public void setExemptStatus(Integer exemptStatus) {
            this.exemptStatus = exemptStatus;
        }

        public String getViolationReasonIds() {
            return violationReasonIds;
        }

        /**
         * 免审商家新增字段:记录违规的原因
         * 
         * @param violationReasonIds 免审商家新增字段:记录违规的原因
         */

        public void setViolationReasonIds(String violationReasonIds) {
            this.violationReasonIds = violationReasonIds;
        }

        /**
         * 免审商家新增字段:记录违规的原因文字信息，逗号分隔
         * 
         * @return violationReasons
         */

        public String getViolationReasons() {
            return violationReasons;
        }

        public void setViolationReasons(String violationReasons) {
            this.violationReasons = violationReasons;
        }

        /**
         * 违规限定修改剩余时间（毫秒数）
         * 
         * @return remainTime
         */

        public Long getRemainTime() {
            return remainTime;
        }

        /**
         * 违规限定修改剩余时间（毫秒数）
         * 
         * @param remainTime 违规限定修改剩余时间（毫秒数）
         */

        public void setRemainTime(Long remainTime) {
            this.remainTime = remainTime;
        }

        public Integer getSubmitOrder() {
            return submitOrder;
        }

        public void setSubmitOrder(Integer submitOrder) {
            this.submitOrder = submitOrder;
        }

        public Integer getProductSource() {
            return productSource;
        }

        public void setProductSource(Integer productSource) {
            this.productSource = productSource;
        }

        public String getProductSname() {
            return productSname;
        }

        public void setProductSname(String productSname) {
            this.productSname = productSname;
        }

        public Integer getCanFenqi() {
            return canFenqi;
        }

        public void setCanFenqi(Integer canFenqi) {
            this.canFenqi = canFenqi;
        }

        public String getSeason() {
            return season;
        }

        public void setSeason(String season) {
            this.season = season;
        }

        public Integer getIsKa() {
            return isKa;
        }

        public void setIsKa(Integer isKa) {
            this.isKa = isKa;
        }

        public Integer getKaMCreateTime() {
            return kaMCreateTime;
        }

        public void setKaMCreateTime(Integer kaMCreateTime) {
            this.kaMCreateTime = kaMCreateTime;
        }

        public Integer getDeliveryDay() {
            return deliveryDay;
        }

        public void setDeliveryDay(Integer deliveryDay) {
            this.deliveryDay = deliveryDay;
        }

        public Integer getIsEdit() {
            return isEdit;
        }

        public void setIsEdit(Integer isEdit) {
            this.isEdit = isEdit;
        }

        public String getProductBrandName() {
            return productBrandName;
        }

        public void setProductBrandName(String productBrandName) {
            this.productBrandName = productBrandName;
        }

        /**
         * 类别拼接字符串
         * 
         * @return categoryStr
         */

        public String getCategoryStr() {
            return categoryStr;
        }

        /**
         * 类别拼接字符串
         * 
         * @param categoryStr 类别拼接字符串
         */

        public void setCategoryStr(String categoryStr) {
            this.categoryStr = categoryStr;
        }

        /**
         * 扩展类别拼接字符串
         * 
         * @return extendCategoryStr
         */

        public String getExtendCategoryStr() {
            return extendCategoryStr;
        }

        /**
         * 扩展类别拼接字符串
         * 
         * @param extendCategoryStr 扩展类别拼接字符串
         */

        public void setExtendCategoryStr(String extendCategoryStr) {
            this.extendCategoryStr = extendCategoryStr;
        }

        public String getCategoryIdStr() {
            return categoryIdStr;
        }

        public void setCategoryIdStr(String categoryIdStr) {
            this.categoryIdStr = categoryIdStr;
        }

        public String getExtendCategoryIdStr() {
            return extendCategoryIdStr;
        }


        public Long getDefaultWarehouseId() {
            return defaultWarehouseId;
        }

        public void setDefaultWarehouseId(Long defaultWarehouseId) {
            this.defaultWarehouseId = defaultWarehouseId;
        }

        public Long getOldCategoryId() {
            return oldCategoryId;
        }

        public void setOldCategoryId(Long oldCategoryId) {
            this.oldCategoryId = oldCategoryId;
        }

        public Long getOldExtendCategoryId() {
            return oldExtendCategoryId;
        }

        public void setOldExtendCategoryId(Long oldExtendCategoryId) {
            this.oldExtendCategoryId = oldExtendCategoryId;
        }

        public String getDeletedProductId() {
            return deletedProductId;
        }

        public void setDeletedProductId(String deletedProductId) {
            this.deletedProductId = deletedProductId;
        }

        public String getReplaceProductSize() {
            return replaceProductSize;
        }

        public void setReplaceProductSize(String replaceProductSize) {
            this.replaceProductSize = replaceProductSize;
        }

        public List<Long> getMasterCategoryIdList() {
            return masterCategoryIdList;
        }

        public void setMasterCategoryIdList(List<Long> masterCategoryIdList) {
            //this.masterCategoryIdList = masterCategoryIdList;
        }

    }

}
