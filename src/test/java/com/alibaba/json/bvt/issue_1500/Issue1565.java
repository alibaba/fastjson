package com.alibaba.json.bvt.issue_1500;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.serializer.SerializeConfig;
import junit.framework.TestCase;
import org.junit.Assert;

import java.io.Serializable;
import java.util.List;

/**
 * Created by SongLing.Dong on 11/7/2017.
 */
public class Issue1565 extends TestCase{

    public void test_testLargeBeanContainsOver256Field(){
        SerializeConfig serializeConfig = new SerializeConfig();
        serializeConfig.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;

//        SmallBean smallBean = new SmallBean();
//        smallBean.setId("S35669xxxxxxxxxxxxxx");
//        smallBean.setNetValueDate(20171105);
//
//        System.out.println(JSON.toJSONString(smallBean, serializeConfig));


        LargeBean expectedBean = new LargeBean();
        expectedBean.setId("S35669");
        expectedBean.setNetValueDate(20171105);
        String expectedStr = "{\"id\":\"S35669\",\"net_value_date\":20171105}";

        String actualStr = JSON.toJSONString(expectedBean, serializeConfig);
        JSONObject actualBean = JSON.parseObject(actualStr);
        Assert.assertEquals(expectedStr, actualStr);
        Assert.assertEquals(expectedBean.getId(), actualBean.getString("id"));
        Assert.assertEquals(expectedBean.getNetValueDate(), actualBean.getInteger("net_value_date"));



    }

    public static class SmallBean implements Serializable{
        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Integer getNetValueDate() {
            return netValueDate;
        }

        public void setNetValueDate(Integer netValueDate) {
            this.netValueDate = netValueDate;
        }

        private Integer netValueDate;
    }

    public static class LargeBean implements Serializable {

        /**
         * 每页数量
         */
        private Integer pageSize;

        /**
         * 获取第一个记录的下标
         */
        private Integer firstResult;

        /**
         * 获取数量
         */
        private Integer fetchSize;

        /**
         * 开始时间
         */
        private String startTime;

        /**
         * 结束时间
         */
        private String endTime;

        /**
         * 是否管理员标识
         */
        private Boolean isAdministrator;

        /**
         * 排序方式 0:升序 1:倒序
         */
        private Byte sortMode;

        /**
         * 排序字段名
         */
        private String sortFieldName;

        /**
         * 排序字段值
         */
        private String sortFieldValue;

        /**
         * 创建时间戳(毫秒)
         */
        private Long createTimestamp;

        /**
         * 上一次页码
         */
        private Integer lastPage;

        /**
         * 查询类型
         */
        private Byte queryType;

        /**
         * 分片键
         */
        private String shard;

        /**
         * 净值日期，格式：yyyyMMdd
         */
        private Integer netValueDate;

        /**
         * 单位净值
         */
        private Float unitNetValue;

        /**
         * 累计净值
         */
        private Float totalNetValue;

        /**
         * 近一个月累计收益率
         */
        private Float nomTotalYield;

        /**
         * 近半年累计收益率
         */
        private Float nhyTotalYield;

        /**
         * 近一年累计收益率
         */
        private Float noyTotalYield;

        /**
         * 本月累计收益率
         */
        private Float tmTotalYield;

        /**
         * 本季度累计收益率
         */
        private Float tqTotalYield;

        /**
         * 本年累计收益率
         */
        private Float tyTotalYield;

        /**
         * 所有累计收益率
         */
        private Float allTotalYield;

        /**
         * 近一个月年化收益率
         */
        private Float nomAnnualizedReturn;

        /**
         * 近半年年化收益率
         */
        private Float nhyAnnualizedReturn;

        /**
         * 近一年年化收益率
         */
        private Float noyAnnualizedReturn;

        /**
         * 本月年化收益率
         */
        private Float tmAnnualizedReturn;

        /**
         * 本季度年化收益率
         */
        private Float tqAnnualizedReturn;

        /**
         * 本年年化收益率
         */
        private Float tyAnnualizedReturn;

        /**
         * 所有年化收益率
         */
        private Float allAnnualizedReturn;

        /**
         * 近一个月最大盈利幅度
         */
        private Float nomMaxProfitMargin;

        /**
         * 近半年最大盈利幅度
         */
        private Float nhyMaxProfitMargin;

        /**
         * 近一年最大盈利幅度
         */
        private Float noyMaxProfitMargin;

        /**
         * 本月最大盈利幅度
         */
        private Float tmMaxProfitMargin;

        /**
         * 本季度最大盈利幅度
         */
        private Float tqMaxProfitMargin;

        /**
         * 本年最大盈利幅度
         */
        private Float tyMaxProfitMargin;

        /**
         * 所有最大盈利幅度
         */
        private Float allMaxProfitMargin;

        /**
         * 近一个月最大单次盈利
         */
        private Float nomMaxSingleProfit;

        /**
         * 近半年最大单次盈利
         */
        private Float nhyMaxSingleProfit;

        /**
         * 近一年最大单次盈利
         */
        private Float noyMaxSingleProfit;

        /**
         * 本月最大单次盈利
         */
        private Float tmMaxSingleProfit;

        /**
         * 本季度最大单次盈利
         */
        private Float tqMaxSingleProfit;

        /**
         * 本年最大单次盈利
         */
        private Float tyMaxSingleProfit;

        /**
         * 所有最大单次盈利
         */
        private Float allMaxSingleProfit;

        /**
         * 近一个月最大连续盈利次数
         */
        private Integer nomMaxConProfitTime;

        /**
         * 近半年最大连续盈利次数
         */
        private Integer nhyMaxConProfitTime;

        /**
         * 近一年最大连续盈利次数
         */
        private Integer noyMaxConProfitTime;

        /**
         * 本月最大连续盈利次数
         */
        private Integer tmMaxConProfitTime;

        /**
         * 本季度最大连续盈利次数
         */
        private Integer tqMaxConProfitTime;

        /**
         * 本年最大连续盈利次数
         */
        private Integer tyMaxConProfitTime;

        /**
         * 所有最大连续盈利次数
         */
        private Integer allMaxConProfitTime;

        /**
         * 所有最大连续盈利次数出现日期
         */
        private Integer allMaxConProfitTimeDate;

        /**
         * 近一个月最大回撤
         */
        private Float nomMaxDrawdown;

        /**
         * 近半年最大回撤
         */
        private Float nhyMaxDrawdown;

        /**
         * 近一年最大回撤
         */
        private Float noyMaxDrawdown;

        /**
         * 本月最大回撤
         */
        private Float tmMaxDrawdown;

        /**
         * 本季度最大回撤
         */
        private Float tqMaxDrawdown;

        /**
         * 本年最大回撤
         */
        private Float tyMaxDrawdown;

        /**
         * 所有最大回撤
         */
        private Float allMaxDrawdown;

        /**
         * 所有最大回撤出现日期
         */
        private Integer allMaxDrawdownDate;

        /**
         * 近一个月最大单次回撤
         */
        private Float nomMaxSingleDrawdown;

        /**
         * 近半年最大单次回撤
         */
        private Float nhyMaxSingleDrawdown;

        /**
         * 近一年最大单次回撤
         */
        private Float noyMaxSingleDrawdown;

        /**
         * 本月最大单次回撤
         */
        private Float tmMaxSingleDrawdown;

        /**
         * 本季度最大单次回撤
         */
        private Float tqMaxSingleDrawdown;

        /**
         * 本年最大单次回撤
         */
        private Float tyMaxSingleDrawdown;

        /**
         * 所有最大单次回撤
         */
        private Float allMaxSingleDrawdown;

        /**
         * 所有最大单次回撤出现日期
         */
        private Integer allMaxSingleDrawdownDate;

        /**
         * 近一个月最大连续回撤次数
         */
        private Integer nomMaxConDrawdownTime;

        /**
         * 近半年最大连续回撤次数
         */
        private Integer nhyMaxConDrawdownTime;

        /**
         * 近一年最大连续回撤次数
         */
        private Integer noyMaxConDrawdownTime;

        /**
         * 本月最大连续回撤次数
         */
        private Integer tmMaxConDrawdownTime;

        /**
         * 本季度最大连续回撤次数
         */
        private Integer tqMaxConDrawdownTime;

        /**
         * 本年最大连续回撤次数
         */
        private Integer tyMaxConDrawdownTime;

        /**
         * 所有最大连续回撤次数
         */
        private Integer allMaxConDrawdownTime;

        /**
         * 近一个月收益率标准差
         */
        private Float nomYieldStdDeviation;

        /**
         * 近半年收益率标准差
         */
        private Float nhyYieldStdDeviation;

        /**
         * 近一年收益率标准差
         */
        private Float noyYieldStdDeviation;

        /**
         * 本月收益率标准差
         */
        private Float tmYieldStdDeviation;

        /**
         * 本季度收益率标准差
         */
        private Float tqYieldStdDeviation;

        /**
         * 本年收益率标准差
         */
        private Float tyYieldStdDeviation;

        /**
         * 所有收益率标准差
         */
        private Float allYieldStdDeviation;

        /**
         * 近一个月下行标准差
         */
        private Float nomDownStdDeviation;

        /**
         * 近半年下行标准差
         */
        private Float nhyDownStdDeviation;

        /**
         * 近一年下行标准差
         */
        private Float noyDownStdDeviation;

        /**
         * 本月下行标准差
         */
        private Float tmDownStdDeviation;

        /**
         * 本季度下行标准差
         */
        private Float tqDownStdDeviation;

        /**
         * 本年下行标准差
         */
        private Float tyDownStdDeviation;

        /**
         * 所有下行标准差
         */
        private Float allDownStdDeviation;

        /**
         * 近一个月胜率
         */
        private Float nomWinRatio;

        /**
         * 近半年胜率
         */
        private Float nhyWinRatio;

        /**
         * 近一年胜率
         */
        private Float noyWinRatio;

        /**
         * 本月胜率
         */
        private Float tmWinRatio;

        /**
         * 本季度胜率
         */
        private Float tqWinRatio;

        /**
         * 本年胜率
         */
        private Float tyWinRatio;

        /**
         * 所有胜率
         */
        private Float allWinRatio;

        /**
         * 近一个月贝塔系数
         */
        private Float nomBeta;

        /**
         * 近半年贝塔系数
         */
        private Float nhyBeta;

        /**
         * 近一年贝塔系数
         */
        private Float noyBeta;

        /**
         * 本月贝塔系数
         */
        private Float tmBeta;

        /**
         * 本季度贝塔系数
         */
        private Float tqBeta;

        /**
         * 本年贝塔系数
         */
        private Float tyBeta;

        /**
         * 所有贝塔系数
         */
        private Float allBeta;

        /**
         * 近一个月阿尔法系数
         */
        private Float nomAlpha;

        /**
         * 近半年阿尔法系数
         */
        private Float nhyAlpha;

        /**
         * 近一年阿尔法系数
         */
        private Float noyAlpha;

        /**
         * 本月阿尔法系数
         */
        private Float tmAlpha;

        /**
         * 本季度阿尔法系数
         */
        private Float tqAlpha;

        /**
         * 本年阿尔法系数
         */
        private Float tyAlpha;

        /**
         * 所有阿尔法系数
         */
        private Float allAlpha;

        /**
         * 近一个月詹森指数
         */
        private Float nomJansen;

        /**
         * 近半年詹森指数
         */
        private Float nhyJansen;

        /**
         * 近一年詹森指数
         */
        private Float noyJansen;

        /**
         * 本月詹森指数
         */
        private Float tmJansen;

        /**
         * 本季度詹森指数
         */
        private Float tqJansen;

        /**
         * 本年詹森指数
         */
        private Float tyJansen;

        /**
         * 所有詹森指数
         */
        private Float allJansen;

        /**
         * 近一个月卡玛比率
         */
        private Float nomKumarRatio;

        /**
         * 近半年卡玛比率
         */
        private Float nhyKumarRatio;

        /**
         * 近一年卡玛比率
         */
        private Float noyKumarRatio;

        /**
         * 本月卡玛比率
         */
        private Float tmKumarRatio;

        /**
         * 本季度卡玛比率
         */
        private Float tqKumarRatio;

        /**
         * 本年卡玛比率
         */
        private Float tyKumarRatio;

        /**
         * 所有卡玛比率
         */
        private Float allKumarRatio;

        /**
         * 近一个月夏普比率
         */
        private Float nomSharpeRatio;

        /**
         * 近半年夏普比率
         */
        private Float nhySharpeRatio;

        /**
         * 近一年夏普比率
         */
        private Float noySharpeRatio;

        /**
         * 本月夏普比率
         */
        private Float tmSharpeRatio;

        /**
         * 本季度夏普比率
         */
        private Float tqSharpeRatio;

        /**
         * 本年夏普比率
         */
        private Float tySharpeRatio;

        /**
         * 所有夏普比率
         */
        private Float allSharpeRatio;

        /**
         * 近一个月索提若比率
         */
        private Float nomSortinoRatio;

        /**
         * 近半年索提若比率
         */
        private Float nhySortinoRatio;

        /**
         * 近一年索提若比率
         */
        private Float noySortinoRatio;

        /**
         * 本月索提若比率
         */
        private Float tmSortinoRatio;

        /**
         * 本季度索提若比率
         */
        private Float tqSortinoRatio;

        /**
         * 本年索提若比率
         */
        private Float tySortinoRatio;

        /**
         * 所有索提若比率
         */
        private Float allSortinoRatio;

        /**
         * 近一个月赫斯特指数
         */
        private Float nomHurstIndex;

        /**
         * 近半年赫斯特指数
         */
        private Float nhyHurstIndex;

        /**
         * 近一年赫斯特指数
         */
        private Float noyHurstIndex;

        /**
         * 本月赫斯特指数
         */
        private Float tmHurstIndex;

        /**
         * 本季度赫斯特指数
         */
        private Float tqHurstIndex;

        /**
         * 本年赫斯特指数
         */
        private Float tyHurstIndex;

        /**
         * 所有赫斯特指数
         */
        private Float allHurstIndex;

        /**
         * 近一个月VaR指标(95%)
         */
        private Float nomVarIndex;

        /**
         * 近半年VaR指标(95%)
         */
        private Float nhyVarIndex;

        /**
         * 近一年VaR指标(95%)
         */
        private Float noyVarIndex;

        /**
         * 本月VaR指标(95%)
         */
        private Float tmVarIndex;

        /**
         * 本季度VaR指标(95%)
         */
        private Float tqVarIndex;

        /**
         * 本年VaR指标(95%)
         */
        private Float tyVarIndex;

        /**
         * 所有VaR指标(95%)
         */
        private Float allVarIndex;

        /**
         * 近一个月VaR指标(99%)
         */
        private Float nomVarIndex99;

        /**
         * 近半年VaR指标(99%)
         */
        private Float nhyVarIndex99;

        /**
         * 近一年VaR指标(99%)
         */
        private Float noyVarIndex99;

        /**
         * 本月VaR指标(99%)
         */
        private Float tmVarIndex99;

        /**
         * 本季度VaR指标(99%)
         */
        private Float tqVarIndex99;

        /**
         * 本年VaR指标(99%)
         */
        private Float tyVarIndex99;

        /**
         * 所有VaR指标(99%)
         */
        private Float allVarIndex99;

        /**
         * 近一个月上行捕获率
         */
        private Float nomUpCaptureRate;

        /**
         * 近半年上行捕获率
         */
        private Float nhyUpCaptureRate;

        /**
         * 近一年上行捕获率
         */
        private Float noyUpCaptureRate;

        /**
         * 本月上行捕获率
         */
        private Float tmUpCaptureRate;

        /**
         * 本季度上行捕获率
         */
        private Float tqUpCaptureRate;

        /**
         * 本年上行捕获率
         */
        private Float tyUpCaptureRate;

        /**
         * 所有上行捕获率
         */
        private Float allUpCaptureRate;

        /**
         * 近一个月下行捕获率
         */
        private Float nomDownCaptureRate;

        /**
         * 近半年下行捕获率
         */
        private Float nhyDownCaptureRate;

        /**
         * 近一年下行捕获率
         */
        private Float noyDownCaptureRate;

        /**
         * 本月下行捕获率
         */
        private Float tmDownCaptureRate;

        /**
         * 本季度下行捕获率
         */
        private Float tqDownCaptureRate;

        /**
         * 本年下行捕获率
         */
        private Float tyDownCaptureRate;

        /**
         * 所有下行捕获率
         */
        private Float allDownCaptureRate;

        /**
         * 近一个月信息比率
         */
        private Float nomInfoRatio;

        /**
         * 近半年信息比率
         */
        private Float nhyInfoRatio;

        /**
         * 近一年信息比率
         */
        private Float noyInfoRatio;

        /**
         * 本月信息比率
         */
        private Float tmInfoRatio;

        /**
         * 本季度信息比率
         */
        private Float tqInfoRatio;

        /**
         * 本年信息比率
         */
        private Float tyInfoRatio;

        /**
         * 所有信息比率
         */
        private Float allInfoRatio;

        /**
         * 近一个月策略波动率
         */
        private Float nomAlgorithmVolatility;

        /**
         * 近半年策略波动率
         */
        private Float nhyAlgorithmVolatility;

        /**
         * 近一年策略波动率
         */
        private Float noyAlgorithmVolatility;

        /**
         * 本月策略波动率
         */
        private Float tmAlgorithmVolatility;

        /**
         * 本季度策略波动率
         */
        private Float tqAlgorithmVolatility;

        /**
         * 本年策略波动率
         */
        private Float tyAlgorithmVolatility;

        /**
         * 所有策略波动率
         */
        private Float allAlgorithmVolatility;

        /**
         * 近一个月M平方
         */
        private Float nomMSquare;

        /**
         * 近半年M平方
         */
        private Float nhyMSquare;

        /**
         * 近一年M平方
         */
        private Float noyMSquare;

        /**
         * 本月M平方
         */
        private Float tmMSquare;

        /**
         * 本季度M平方
         */
        private Float tqMSquare;

        /**
         * 本年M平方
         */
        private Float tyMSquare;

        /**
         * 所有M平方
         */
        private Float allMSquare;

        /**
         * 近一个月特雷诺指数(TR)
         */
        private Float nomTreynorIndex;

        /**
         * 近半年特雷诺指数(TR)
         */
        private Float nhyTreynorIndex;

        /**
         * 近一年特雷诺指数(TR)
         */
        private Float noyTreynorIndex;

        /**
         * 本月特雷诺指数(TR)
         */
        private Float tmTreynorIndex;

        /**
         * 本季度特雷诺指数(TR)
         */
        private Float tqTreynorIndex;

        /**
         * 本年特雷诺指数(TR)
         */
        private Float tyTreynorIndex;

        /**
         * 所有特雷诺指数(TR)
         */
        private Float allTreynorIndex;

        /**
         * 基金产品ID(片键值)
         */
        private String id;

        /**
         * 基金产品名称
         */
        private String name;

        /**
         * 基金产品短名称
         */
        private String shortName;

        /**
         * 基金代码
         */
        private String code;

        /**
         * 备案号
         */
        private String recordNumber;

        /**
         * 基金类型 0:私募基金 1:公募基金 2:私有基金
         */
        private Byte fundType;

        /**
         * 基金品种 0:开放式基金 1:货币型基金 2:理财型基金 3:分级型基金 4:场内交易型基金
         */
        private Byte fundBreed;

        /**
         * 基金状态 0:存续中 1:已清盘
         */
        private Byte fundStatus;

        /**
         * 申购状态，当基金类型=1:公募基金时该字段才存在
         */
        private String buyStatus;

        /**
         * 赎回状态，当基金类型=1:公募基金时该字段才存在
         */
        private String redeemStatus;

        /**
         * 备案日期，格式：yyyy-MM-dd
         */
        private String recordDate;

        /**
         * 成立日期，格式：yyyy-MM-dd
         */
        private String createDate;

        /**
         * 终止日期，格式：yyyy-MM-dd
         */
        private String stopDate;

        /**
         * 基金备案阶段
         */
        private String fundFilingStage;

        /**
         * 基金投资类型
         */
        private String fundInvestmentType;

        /**
         * 币种
         */
        private String currency;

        /**
         * 管理类型
         */
        private String managerType;

        /**
         * 托管人名称
         */
        private String managerName;

        /**
         * 投资目标
         */
        private String investmentTarget;

        /**
         * 主要投资领域，即投资范围
         */
        private String majorInvestAreas;

        /**
         * 基金信息最后修改日期
         */
        private String fundLastModifyDate;

        /**
         * 基金协会特别提示（针对基金）
         */
        private String specialNote;

        /**
         * 注册地址
         */
        private String registeredAddress;

        /**
         * 投资策略
         */
        private String investmentStrategy;

        /**
         * 投资子策略
         */
        private String investmentSubStrategy;

        /**
         * 基金经理ID数组
         */
        private List<String> fundManagerIds;

        /**
         * 投顾公司ID
         */
        private String companyId;

        /**
         * 序号
         */
        private Long orderNum;

        /**
         * 成立规模
         */
        private String createScale;

        /**
         * 最新规模
         */
        private String latestScale;

        /**
         * 产品基准代码
         */
        private String benchmark;

        /**
         * 净值更新频率
         */
        private Byte netValueUpdateRate;

        /**
         * 基金产品外部ID
         */
        private String fundOuterId;

        /**
         * 标签
         */
        private String tags;

        /**
         * 备注
         */
        private String remark;

        /**
         * 策略容量
         */
        private String strategyCapacity;

        /**
         * 创建时间
         */
        private Long createTime;

        /**
         * 创建者ID
         */
        private String creatorId;

        /**
         * 最后修改时间
         */
        private Long lastModifyTime;

        /**
         * 最后修改者ID
         */
        private String lastModifierId;

        /**
         * 基金公司外部ID
         */
        private String companyOuterId;

        /**
         * 基金公司名称
         */
        private String companyName;

        /**
         * 基金经理外部ID数组
         */
        private List<String> managerOuterIds;

        /**
         * 基金产品ID列表
         */
        private List<String> fundIds;

        /**
         * 投顾公司ID列表
         */
        private List<String> companyIds;

        /**
         * 开始年化收益率
         */
        private Float startAnnualizedReturn;

        /**
         * 结束年化收益率
         */
        private Float endAnnualizedReturn;

        /**
         * 时间区间
         */
        private String timeInterval;

        /**
         * 基金经理姓名数组
         */
        private List<String> fundManagerNames;

        /**
         * 基金状态名称 0:存续中 1:已清盘
         */
        private String fundStatusName;

        /**
         * 基金类型名称  0:私募基金 1:公募基金 2:私有基金'
         */
        private String fundTypeName;

        /**
         * 是否关注基金 0:否 1:是
         */
        private Byte isConcern;

        /**
         * 配置权重(%)
         */
        private Float configWeight;

        /**
         * 净值日期字符串 yyyy-MM-dd格式
         */
        private String netValueDateString;

        /**
         * 基金经理ID
         */
        private String managerId;

        /**
         * 用户标签ID
         */
        private String tagId;

        public Integer getPageSize() {
            return pageSize;
        }

        public void setPageSize(Integer pageSize) {
            this.pageSize = pageSize;
        }

        public Integer getFirstResult() {
            return firstResult;
        }

        public void setFirstResult(Integer firstResult) {
            this.firstResult = firstResult;
        }

        public Integer getFetchSize() {
            return fetchSize;
        }

        public void setFetchSize(Integer fetchSize) {
            this.fetchSize = fetchSize;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public Boolean getAdministrator() {
            return isAdministrator;
        }

        public void setAdministrator(Boolean administrator) {
            isAdministrator = administrator;
        }

        public Byte getSortMode() {
            return sortMode;
        }

        public void setSortMode(Byte sortMode) {
            this.sortMode = sortMode;
        }

        public String getSortFieldName() {
            return sortFieldName;
        }

        public void setSortFieldName(String sortFieldName) {
            this.sortFieldName = sortFieldName;
        }

        public String getSortFieldValue() {
            return sortFieldValue;
        }

        public void setSortFieldValue(String sortFieldValue) {
            this.sortFieldValue = sortFieldValue;
        }

        public Long getCreateTimestamp() {
            return createTimestamp;
        }

        public void setCreateTimestamp(Long createTimestamp) {
            this.createTimestamp = createTimestamp;
        }

        public Integer getLastPage() {
            return lastPage;
        }

        public void setLastPage(Integer lastPage) {
            this.lastPage = lastPage;
        }

        public Byte getQueryType() {
            return queryType;
        }

        public void setQueryType(Byte queryType) {
            this.queryType = queryType;
        }

        public String getShard() {
            return shard;
        }

        public void setShard(String shard) {
            this.shard = shard;
        }

        public Integer getNetValueDate() {
            return netValueDate;
        }

        public void setNetValueDate(Integer netValueDate) {
            this.netValueDate = netValueDate;
        }

        public Float getUnitNetValue() {
            return unitNetValue;
        }

        public void setUnitNetValue(Float unitNetValue) {
            this.unitNetValue = unitNetValue;
        }

        public Float getTotalNetValue() {
            return totalNetValue;
        }

        public void setTotalNetValue(Float totalNetValue) {
            this.totalNetValue = totalNetValue;
        }

        public Float getNomTotalYield() {
            return nomTotalYield;
        }

        public void setNomTotalYield(Float nomTotalYield) {
            this.nomTotalYield = nomTotalYield;
        }

        public Float getNhyTotalYield() {
            return nhyTotalYield;
        }

        public void setNhyTotalYield(Float nhyTotalYield) {
            this.nhyTotalYield = nhyTotalYield;
        }

        public Float getNoyTotalYield() {
            return noyTotalYield;
        }

        public void setNoyTotalYield(Float noyTotalYield) {
            this.noyTotalYield = noyTotalYield;
        }

        public Float getTmTotalYield() {
            return tmTotalYield;
        }

        public void setTmTotalYield(Float tmTotalYield) {
            this.tmTotalYield = tmTotalYield;
        }

        public Float getTqTotalYield() {
            return tqTotalYield;
        }

        public void setTqTotalYield(Float tqTotalYield) {
            this.tqTotalYield = tqTotalYield;
        }

        public Float getTyTotalYield() {
            return tyTotalYield;
        }

        public void setTyTotalYield(Float tyTotalYield) {
            this.tyTotalYield = tyTotalYield;
        }

        public Float getAllTotalYield() {
            return allTotalYield;
        }

        public void setAllTotalYield(Float allTotalYield) {
            this.allTotalYield = allTotalYield;
        }

        public Float getNomAnnualizedReturn() {
            return nomAnnualizedReturn;
        }

        public void setNomAnnualizedReturn(Float nomAnnualizedReturn) {
            this.nomAnnualizedReturn = nomAnnualizedReturn;
        }

        public Float getNhyAnnualizedReturn() {
            return nhyAnnualizedReturn;
        }

        public void setNhyAnnualizedReturn(Float nhyAnnualizedReturn) {
            this.nhyAnnualizedReturn = nhyAnnualizedReturn;
        }

        public Float getNoyAnnualizedReturn() {
            return noyAnnualizedReturn;
        }

        public void setNoyAnnualizedReturn(Float noyAnnualizedReturn) {
            this.noyAnnualizedReturn = noyAnnualizedReturn;
        }

        public Float getTmAnnualizedReturn() {
            return tmAnnualizedReturn;
        }

        public void setTmAnnualizedReturn(Float tmAnnualizedReturn) {
            this.tmAnnualizedReturn = tmAnnualizedReturn;
        }

        public Float getTqAnnualizedReturn() {
            return tqAnnualizedReturn;
        }

        public void setTqAnnualizedReturn(Float tqAnnualizedReturn) {
            this.tqAnnualizedReturn = tqAnnualizedReturn;
        }

        public Float getTyAnnualizedReturn() {
            return tyAnnualizedReturn;
        }

        public void setTyAnnualizedReturn(Float tyAnnualizedReturn) {
            this.tyAnnualizedReturn = tyAnnualizedReturn;
        }

        public Float getAllAnnualizedReturn() {
            return allAnnualizedReturn;
        }

        public void setAllAnnualizedReturn(Float allAnnualizedReturn) {
            this.allAnnualizedReturn = allAnnualizedReturn;
        }

        public Float getNomMaxProfitMargin() {
            return nomMaxProfitMargin;
        }

        public void setNomMaxProfitMargin(Float nomMaxProfitMargin) {
            this.nomMaxProfitMargin = nomMaxProfitMargin;
        }

        public Float getNhyMaxProfitMargin() {
            return nhyMaxProfitMargin;
        }

        public void setNhyMaxProfitMargin(Float nhyMaxProfitMargin) {
            this.nhyMaxProfitMargin = nhyMaxProfitMargin;
        }

        public Float getNoyMaxProfitMargin() {
            return noyMaxProfitMargin;
        }

        public void setNoyMaxProfitMargin(Float noyMaxProfitMargin) {
            this.noyMaxProfitMargin = noyMaxProfitMargin;
        }

        public Float getTmMaxProfitMargin() {
            return tmMaxProfitMargin;
        }

        public void setTmMaxProfitMargin(Float tmMaxProfitMargin) {
            this.tmMaxProfitMargin = tmMaxProfitMargin;
        }

        public Float getTqMaxProfitMargin() {
            return tqMaxProfitMargin;
        }

        public void setTqMaxProfitMargin(Float tqMaxProfitMargin) {
            this.tqMaxProfitMargin = tqMaxProfitMargin;
        }

        public Float getTyMaxProfitMargin() {
            return tyMaxProfitMargin;
        }

        public void setTyMaxProfitMargin(Float tyMaxProfitMargin) {
            this.tyMaxProfitMargin = tyMaxProfitMargin;
        }

        public Float getAllMaxProfitMargin() {
            return allMaxProfitMargin;
        }

        public void setAllMaxProfitMargin(Float allMaxProfitMargin) {
            this.allMaxProfitMargin = allMaxProfitMargin;
        }

        public Float getNomMaxSingleProfit() {
            return nomMaxSingleProfit;
        }

        public void setNomMaxSingleProfit(Float nomMaxSingleProfit) {
            this.nomMaxSingleProfit = nomMaxSingleProfit;
        }

        public Float getNhyMaxSingleProfit() {
            return nhyMaxSingleProfit;
        }

        public void setNhyMaxSingleProfit(Float nhyMaxSingleProfit) {
            this.nhyMaxSingleProfit = nhyMaxSingleProfit;
        }

        public Float getNoyMaxSingleProfit() {
            return noyMaxSingleProfit;
        }

        public void setNoyMaxSingleProfit(Float noyMaxSingleProfit) {
            this.noyMaxSingleProfit = noyMaxSingleProfit;
        }

        public Float getTmMaxSingleProfit() {
            return tmMaxSingleProfit;
        }

        public void setTmMaxSingleProfit(Float tmMaxSingleProfit) {
            this.tmMaxSingleProfit = tmMaxSingleProfit;
        }

        public Float getTqMaxSingleProfit() {
            return tqMaxSingleProfit;
        }

        public void setTqMaxSingleProfit(Float tqMaxSingleProfit) {
            this.tqMaxSingleProfit = tqMaxSingleProfit;
        }

        public Float getTyMaxSingleProfit() {
            return tyMaxSingleProfit;
        }

        public void setTyMaxSingleProfit(Float tyMaxSingleProfit) {
            this.tyMaxSingleProfit = tyMaxSingleProfit;
        }

        public Float getAllMaxSingleProfit() {
            return allMaxSingleProfit;
        }

        public void setAllMaxSingleProfit(Float allMaxSingleProfit) {
            this.allMaxSingleProfit = allMaxSingleProfit;
        }

        public Integer getNomMaxConProfitTime() {
            return nomMaxConProfitTime;
        }

        public void setNomMaxConProfitTime(Integer nomMaxConProfitTime) {
            this.nomMaxConProfitTime = nomMaxConProfitTime;
        }

        public Integer getNhyMaxConProfitTime() {
            return nhyMaxConProfitTime;
        }

        public void setNhyMaxConProfitTime(Integer nhyMaxConProfitTime) {
            this.nhyMaxConProfitTime = nhyMaxConProfitTime;
        }

        public Integer getNoyMaxConProfitTime() {
            return noyMaxConProfitTime;
        }

        public void setNoyMaxConProfitTime(Integer noyMaxConProfitTime) {
            this.noyMaxConProfitTime = noyMaxConProfitTime;
        }

        public Integer getTmMaxConProfitTime() {
            return tmMaxConProfitTime;
        }

        public void setTmMaxConProfitTime(Integer tmMaxConProfitTime) {
            this.tmMaxConProfitTime = tmMaxConProfitTime;
        }

        public Integer getTqMaxConProfitTime() {
            return tqMaxConProfitTime;
        }

        public void setTqMaxConProfitTime(Integer tqMaxConProfitTime) {
            this.tqMaxConProfitTime = tqMaxConProfitTime;
        }

        public Integer getTyMaxConProfitTime() {
            return tyMaxConProfitTime;
        }

        public void setTyMaxConProfitTime(Integer tyMaxConProfitTime) {
            this.tyMaxConProfitTime = tyMaxConProfitTime;
        }

        public Integer getAllMaxConProfitTime() {
            return allMaxConProfitTime;
        }

        public void setAllMaxConProfitTime(Integer allMaxConProfitTime) {
            this.allMaxConProfitTime = allMaxConProfitTime;
        }

        public Integer getAllMaxConProfitTimeDate() {
            return allMaxConProfitTimeDate;
        }

        public void setAllMaxConProfitTimeDate(Integer allMaxConProfitTimeDate) {
            this.allMaxConProfitTimeDate = allMaxConProfitTimeDate;
        }

        public Float getNomMaxDrawdown() {
            return nomMaxDrawdown;
        }

        public void setNomMaxDrawdown(Float nomMaxDrawdown) {
            this.nomMaxDrawdown = nomMaxDrawdown;
        }

        public Float getNhyMaxDrawdown() {
            return nhyMaxDrawdown;
        }

        public void setNhyMaxDrawdown(Float nhyMaxDrawdown) {
            this.nhyMaxDrawdown = nhyMaxDrawdown;
        }

        public Float getNoyMaxDrawdown() {
            return noyMaxDrawdown;
        }

        public void setNoyMaxDrawdown(Float noyMaxDrawdown) {
            this.noyMaxDrawdown = noyMaxDrawdown;
        }

        public Float getTmMaxDrawdown() {
            return tmMaxDrawdown;
        }

        public void setTmMaxDrawdown(Float tmMaxDrawdown) {
            this.tmMaxDrawdown = tmMaxDrawdown;
        }

        public Float getTqMaxDrawdown() {
            return tqMaxDrawdown;
        }

        public void setTqMaxDrawdown(Float tqMaxDrawdown) {
            this.tqMaxDrawdown = tqMaxDrawdown;
        }

        public Float getTyMaxDrawdown() {
            return tyMaxDrawdown;
        }

        public void setTyMaxDrawdown(Float tyMaxDrawdown) {
            this.tyMaxDrawdown = tyMaxDrawdown;
        }

        public Float getAllMaxDrawdown() {
            return allMaxDrawdown;
        }

        public void setAllMaxDrawdown(Float allMaxDrawdown) {
            this.allMaxDrawdown = allMaxDrawdown;
        }

        public Integer getAllMaxDrawdownDate() {
            return allMaxDrawdownDate;
        }

        public void setAllMaxDrawdownDate(Integer allMaxDrawdownDate) {
            this.allMaxDrawdownDate = allMaxDrawdownDate;
        }

        public Float getNomMaxSingleDrawdown() {
            return nomMaxSingleDrawdown;
        }

        public void setNomMaxSingleDrawdown(Float nomMaxSingleDrawdown) {
            this.nomMaxSingleDrawdown = nomMaxSingleDrawdown;
        }

        public Float getNhyMaxSingleDrawdown() {
            return nhyMaxSingleDrawdown;
        }

        public void setNhyMaxSingleDrawdown(Float nhyMaxSingleDrawdown) {
            this.nhyMaxSingleDrawdown = nhyMaxSingleDrawdown;
        }

        public Float getNoyMaxSingleDrawdown() {
            return noyMaxSingleDrawdown;
        }

        public void setNoyMaxSingleDrawdown(Float noyMaxSingleDrawdown) {
            this.noyMaxSingleDrawdown = noyMaxSingleDrawdown;
        }

        public Float getTmMaxSingleDrawdown() {
            return tmMaxSingleDrawdown;
        }

        public void setTmMaxSingleDrawdown(Float tmMaxSingleDrawdown) {
            this.tmMaxSingleDrawdown = tmMaxSingleDrawdown;
        }

        public Float getTqMaxSingleDrawdown() {
            return tqMaxSingleDrawdown;
        }

        public void setTqMaxSingleDrawdown(Float tqMaxSingleDrawdown) {
            this.tqMaxSingleDrawdown = tqMaxSingleDrawdown;
        }

        public Float getTyMaxSingleDrawdown() {
            return tyMaxSingleDrawdown;
        }

        public void setTyMaxSingleDrawdown(Float tyMaxSingleDrawdown) {
            this.tyMaxSingleDrawdown = tyMaxSingleDrawdown;
        }

        public Float getAllMaxSingleDrawdown() {
            return allMaxSingleDrawdown;
        }

        public void setAllMaxSingleDrawdown(Float allMaxSingleDrawdown) {
            this.allMaxSingleDrawdown = allMaxSingleDrawdown;
        }

        public Integer getAllMaxSingleDrawdownDate() {
            return allMaxSingleDrawdownDate;
        }

        public void setAllMaxSingleDrawdownDate(Integer allMaxSingleDrawdownDate) {
            this.allMaxSingleDrawdownDate = allMaxSingleDrawdownDate;
        }

        public Integer getNomMaxConDrawdownTime() {
            return nomMaxConDrawdownTime;
        }

        public void setNomMaxConDrawdownTime(Integer nomMaxConDrawdownTime) {
            this.nomMaxConDrawdownTime = nomMaxConDrawdownTime;
        }

        public Integer getNhyMaxConDrawdownTime() {
            return nhyMaxConDrawdownTime;
        }

        public void setNhyMaxConDrawdownTime(Integer nhyMaxConDrawdownTime) {
            this.nhyMaxConDrawdownTime = nhyMaxConDrawdownTime;
        }

        public Integer getNoyMaxConDrawdownTime() {
            return noyMaxConDrawdownTime;
        }

        public void setNoyMaxConDrawdownTime(Integer noyMaxConDrawdownTime) {
            this.noyMaxConDrawdownTime = noyMaxConDrawdownTime;
        }

        public Integer getTmMaxConDrawdownTime() {
            return tmMaxConDrawdownTime;
        }

        public void setTmMaxConDrawdownTime(Integer tmMaxConDrawdownTime) {
            this.tmMaxConDrawdownTime = tmMaxConDrawdownTime;
        }

        public Integer getTqMaxConDrawdownTime() {
            return tqMaxConDrawdownTime;
        }

        public void setTqMaxConDrawdownTime(Integer tqMaxConDrawdownTime) {
            this.tqMaxConDrawdownTime = tqMaxConDrawdownTime;
        }

        public Integer getTyMaxConDrawdownTime() {
            return tyMaxConDrawdownTime;
        }

        public void setTyMaxConDrawdownTime(Integer tyMaxConDrawdownTime) {
            this.tyMaxConDrawdownTime = tyMaxConDrawdownTime;
        }

        public Integer getAllMaxConDrawdownTime() {
            return allMaxConDrawdownTime;
        }

        public void setAllMaxConDrawdownTime(Integer allMaxConDrawdownTime) {
            this.allMaxConDrawdownTime = allMaxConDrawdownTime;
        }

        public Float getNomYieldStdDeviation() {
            return nomYieldStdDeviation;
        }

        public void setNomYieldStdDeviation(Float nomYieldStdDeviation) {
            this.nomYieldStdDeviation = nomYieldStdDeviation;
        }

        public Float getNhyYieldStdDeviation() {
            return nhyYieldStdDeviation;
        }

        public void setNhyYieldStdDeviation(Float nhyYieldStdDeviation) {
            this.nhyYieldStdDeviation = nhyYieldStdDeviation;
        }

        public Float getNoyYieldStdDeviation() {
            return noyYieldStdDeviation;
        }

        public void setNoyYieldStdDeviation(Float noyYieldStdDeviation) {
            this.noyYieldStdDeviation = noyYieldStdDeviation;
        }

        public Float getTmYieldStdDeviation() {
            return tmYieldStdDeviation;
        }

        public void setTmYieldStdDeviation(Float tmYieldStdDeviation) {
            this.tmYieldStdDeviation = tmYieldStdDeviation;
        }

        public Float getTqYieldStdDeviation() {
            return tqYieldStdDeviation;
        }

        public void setTqYieldStdDeviation(Float tqYieldStdDeviation) {
            this.tqYieldStdDeviation = tqYieldStdDeviation;
        }

        public Float getTyYieldStdDeviation() {
            return tyYieldStdDeviation;
        }

        public void setTyYieldStdDeviation(Float tyYieldStdDeviation) {
            this.tyYieldStdDeviation = tyYieldStdDeviation;
        }

        public Float getAllYieldStdDeviation() {
            return allYieldStdDeviation;
        }

        public void setAllYieldStdDeviation(Float allYieldStdDeviation) {
            this.allYieldStdDeviation = allYieldStdDeviation;
        }

        public Float getNomDownStdDeviation() {
            return nomDownStdDeviation;
        }

        public void setNomDownStdDeviation(Float nomDownStdDeviation) {
            this.nomDownStdDeviation = nomDownStdDeviation;
        }

        public Float getNhyDownStdDeviation() {
            return nhyDownStdDeviation;
        }

        public void setNhyDownStdDeviation(Float nhyDownStdDeviation) {
            this.nhyDownStdDeviation = nhyDownStdDeviation;
        }

        public Float getNoyDownStdDeviation() {
            return noyDownStdDeviation;
        }

        public void setNoyDownStdDeviation(Float noyDownStdDeviation) {
            this.noyDownStdDeviation = noyDownStdDeviation;
        }

        public Float getTmDownStdDeviation() {
            return tmDownStdDeviation;
        }

        public void setTmDownStdDeviation(Float tmDownStdDeviation) {
            this.tmDownStdDeviation = tmDownStdDeviation;
        }

        public Float getTqDownStdDeviation() {
            return tqDownStdDeviation;
        }

        public void setTqDownStdDeviation(Float tqDownStdDeviation) {
            this.tqDownStdDeviation = tqDownStdDeviation;
        }

        public Float getTyDownStdDeviation() {
            return tyDownStdDeviation;
        }

        public void setTyDownStdDeviation(Float tyDownStdDeviation) {
            this.tyDownStdDeviation = tyDownStdDeviation;
        }

        public Float getAllDownStdDeviation() {
            return allDownStdDeviation;
        }

        public void setAllDownStdDeviation(Float allDownStdDeviation) {
            this.allDownStdDeviation = allDownStdDeviation;
        }

        public Float getNomWinRatio() {
            return nomWinRatio;
        }

        public void setNomWinRatio(Float nomWinRatio) {
            this.nomWinRatio = nomWinRatio;
        }

        public Float getNhyWinRatio() {
            return nhyWinRatio;
        }

        public void setNhyWinRatio(Float nhyWinRatio) {
            this.nhyWinRatio = nhyWinRatio;
        }

        public Float getNoyWinRatio() {
            return noyWinRatio;
        }

        public void setNoyWinRatio(Float noyWinRatio) {
            this.noyWinRatio = noyWinRatio;
        }

        public Float getTmWinRatio() {
            return tmWinRatio;
        }

        public void setTmWinRatio(Float tmWinRatio) {
            this.tmWinRatio = tmWinRatio;
        }

        public Float getTqWinRatio() {
            return tqWinRatio;
        }

        public void setTqWinRatio(Float tqWinRatio) {
            this.tqWinRatio = tqWinRatio;
        }

        public Float getTyWinRatio() {
            return tyWinRatio;
        }

        public void setTyWinRatio(Float tyWinRatio) {
            this.tyWinRatio = tyWinRatio;
        }

        public Float getAllWinRatio() {
            return allWinRatio;
        }

        public void setAllWinRatio(Float allWinRatio) {
            this.allWinRatio = allWinRatio;
        }

        public Float getNomBeta() {
            return nomBeta;
        }

        public void setNomBeta(Float nomBeta) {
            this.nomBeta = nomBeta;
        }

        public Float getNhyBeta() {
            return nhyBeta;
        }

        public void setNhyBeta(Float nhyBeta) {
            this.nhyBeta = nhyBeta;
        }

        public Float getNoyBeta() {
            return noyBeta;
        }

        public void setNoyBeta(Float noyBeta) {
            this.noyBeta = noyBeta;
        }

        public Float getTmBeta() {
            return tmBeta;
        }

        public void setTmBeta(Float tmBeta) {
            this.tmBeta = tmBeta;
        }

        public Float getTqBeta() {
            return tqBeta;
        }

        public void setTqBeta(Float tqBeta) {
            this.tqBeta = tqBeta;
        }

        public Float getTyBeta() {
            return tyBeta;
        }

        public void setTyBeta(Float tyBeta) {
            this.tyBeta = tyBeta;
        }

        public Float getAllBeta() {
            return allBeta;
        }

        public void setAllBeta(Float allBeta) {
            this.allBeta = allBeta;
        }

        public Float getNomAlpha() {
            return nomAlpha;
        }

        public void setNomAlpha(Float nomAlpha) {
            this.nomAlpha = nomAlpha;
        }

        public Float getNhyAlpha() {
            return nhyAlpha;
        }

        public void setNhyAlpha(Float nhyAlpha) {
            this.nhyAlpha = nhyAlpha;
        }

        public Float getNoyAlpha() {
            return noyAlpha;
        }

        public void setNoyAlpha(Float noyAlpha) {
            this.noyAlpha = noyAlpha;
        }

        public Float getTmAlpha() {
            return tmAlpha;
        }

        public void setTmAlpha(Float tmAlpha) {
            this.tmAlpha = tmAlpha;
        }

        public Float getTqAlpha() {
            return tqAlpha;
        }

        public void setTqAlpha(Float tqAlpha) {
            this.tqAlpha = tqAlpha;
        }

        public Float getTyAlpha() {
            return tyAlpha;
        }

        public void setTyAlpha(Float tyAlpha) {
            this.tyAlpha = tyAlpha;
        }

        public Float getAllAlpha() {
            return allAlpha;
        }

        public void setAllAlpha(Float allAlpha) {
            this.allAlpha = allAlpha;
        }

        public Float getNomJansen() {
            return nomJansen;
        }

        public void setNomJansen(Float nomJansen) {
            this.nomJansen = nomJansen;
        }

        public Float getNhyJansen() {
            return nhyJansen;
        }

        public void setNhyJansen(Float nhyJansen) {
            this.nhyJansen = nhyJansen;
        }

        public Float getNoyJansen() {
            return noyJansen;
        }

        public void setNoyJansen(Float noyJansen) {
            this.noyJansen = noyJansen;
        }

        public Float getTmJansen() {
            return tmJansen;
        }

        public void setTmJansen(Float tmJansen) {
            this.tmJansen = tmJansen;
        }

        public Float getTqJansen() {
            return tqJansen;
        }

        public void setTqJansen(Float tqJansen) {
            this.tqJansen = tqJansen;
        }

        public Float getTyJansen() {
            return tyJansen;
        }

        public void setTyJansen(Float tyJansen) {
            this.tyJansen = tyJansen;
        }

        public Float getAllJansen() {
            return allJansen;
        }

        public void setAllJansen(Float allJansen) {
            this.allJansen = allJansen;
        }

        public Float getNomKumarRatio() {
            return nomKumarRatio;
        }

        public void setNomKumarRatio(Float nomKumarRatio) {
            this.nomKumarRatio = nomKumarRatio;
        }

        public Float getNhyKumarRatio() {
            return nhyKumarRatio;
        }

        public void setNhyKumarRatio(Float nhyKumarRatio) {
            this.nhyKumarRatio = nhyKumarRatio;
        }

        public Float getNoyKumarRatio() {
            return noyKumarRatio;
        }

        public void setNoyKumarRatio(Float noyKumarRatio) {
            this.noyKumarRatio = noyKumarRatio;
        }

        public Float getTmKumarRatio() {
            return tmKumarRatio;
        }

        public void setTmKumarRatio(Float tmKumarRatio) {
            this.tmKumarRatio = tmKumarRatio;
        }

        public Float getTqKumarRatio() {
            return tqKumarRatio;
        }

        public void setTqKumarRatio(Float tqKumarRatio) {
            this.tqKumarRatio = tqKumarRatio;
        }

        public Float getTyKumarRatio() {
            return tyKumarRatio;
        }

        public void setTyKumarRatio(Float tyKumarRatio) {
            this.tyKumarRatio = tyKumarRatio;
        }

        public Float getAllKumarRatio() {
            return allKumarRatio;
        }

        public void setAllKumarRatio(Float allKumarRatio) {
            this.allKumarRatio = allKumarRatio;
        }

        public Float getNomSharpeRatio() {
            return nomSharpeRatio;
        }

        public void setNomSharpeRatio(Float nomSharpeRatio) {
            this.nomSharpeRatio = nomSharpeRatio;
        }

        public Float getNhySharpeRatio() {
            return nhySharpeRatio;
        }

        public void setNhySharpeRatio(Float nhySharpeRatio) {
            this.nhySharpeRatio = nhySharpeRatio;
        }

        public Float getNoySharpeRatio() {
            return noySharpeRatio;
        }

        public void setNoySharpeRatio(Float noySharpeRatio) {
            this.noySharpeRatio = noySharpeRatio;
        }

        public Float getTmSharpeRatio() {
            return tmSharpeRatio;
        }

        public void setTmSharpeRatio(Float tmSharpeRatio) {
            this.tmSharpeRatio = tmSharpeRatio;
        }

        public Float getTqSharpeRatio() {
            return tqSharpeRatio;
        }

        public void setTqSharpeRatio(Float tqSharpeRatio) {
            this.tqSharpeRatio = tqSharpeRatio;
        }

        public Float getTySharpeRatio() {
            return tySharpeRatio;
        }

        public void setTySharpeRatio(Float tySharpeRatio) {
            this.tySharpeRatio = tySharpeRatio;
        }

        public Float getAllSharpeRatio() {
            return allSharpeRatio;
        }

        public void setAllSharpeRatio(Float allSharpeRatio) {
            this.allSharpeRatio = allSharpeRatio;
        }

        public Float getNomSortinoRatio() {
            return nomSortinoRatio;
        }

        public void setNomSortinoRatio(Float nomSortinoRatio) {
            this.nomSortinoRatio = nomSortinoRatio;
        }

        public Float getNhySortinoRatio() {
            return nhySortinoRatio;
        }

        public void setNhySortinoRatio(Float nhySortinoRatio) {
            this.nhySortinoRatio = nhySortinoRatio;
        }

        public Float getNoySortinoRatio() {
            return noySortinoRatio;
        }

        public void setNoySortinoRatio(Float noySortinoRatio) {
            this.noySortinoRatio = noySortinoRatio;
        }

        public Float getTmSortinoRatio() {
            return tmSortinoRatio;
        }

        public void setTmSortinoRatio(Float tmSortinoRatio) {
            this.tmSortinoRatio = tmSortinoRatio;
        }

        public Float getTqSortinoRatio() {
            return tqSortinoRatio;
        }

        public void setTqSortinoRatio(Float tqSortinoRatio) {
            this.tqSortinoRatio = tqSortinoRatio;
        }

        public Float getTySortinoRatio() {
            return tySortinoRatio;
        }

        public void setTySortinoRatio(Float tySortinoRatio) {
            this.tySortinoRatio = tySortinoRatio;
        }

        public Float getAllSortinoRatio() {
            return allSortinoRatio;
        }

        public void setAllSortinoRatio(Float allSortinoRatio) {
            this.allSortinoRatio = allSortinoRatio;
        }

        public Float getNomHurstIndex() {
            return nomHurstIndex;
        }

        public void setNomHurstIndex(Float nomHurstIndex) {
            this.nomHurstIndex = nomHurstIndex;
        }

        public Float getNhyHurstIndex() {
            return nhyHurstIndex;
        }

        public void setNhyHurstIndex(Float nhyHurstIndex) {
            this.nhyHurstIndex = nhyHurstIndex;
        }

        public Float getNoyHurstIndex() {
            return noyHurstIndex;
        }

        public void setNoyHurstIndex(Float noyHurstIndex) {
            this.noyHurstIndex = noyHurstIndex;
        }

        public Float getTmHurstIndex() {
            return tmHurstIndex;
        }

        public void setTmHurstIndex(Float tmHurstIndex) {
            this.tmHurstIndex = tmHurstIndex;
        }

        public Float getTqHurstIndex() {
            return tqHurstIndex;
        }

        public void setTqHurstIndex(Float tqHurstIndex) {
            this.tqHurstIndex = tqHurstIndex;
        }

        public Float getTyHurstIndex() {
            return tyHurstIndex;
        }

        public void setTyHurstIndex(Float tyHurstIndex) {
            this.tyHurstIndex = tyHurstIndex;
        }

        public Float getAllHurstIndex() {
            return allHurstIndex;
        }

        public void setAllHurstIndex(Float allHurstIndex) {
            this.allHurstIndex = allHurstIndex;
        }

        public Float getNomVarIndex() {
            return nomVarIndex;
        }

        public void setNomVarIndex(Float nomVarIndex) {
            this.nomVarIndex = nomVarIndex;
        }

        public Float getNhyVarIndex() {
            return nhyVarIndex;
        }

        public void setNhyVarIndex(Float nhyVarIndex) {
            this.nhyVarIndex = nhyVarIndex;
        }

        public Float getNoyVarIndex() {
            return noyVarIndex;
        }

        public void setNoyVarIndex(Float noyVarIndex) {
            this.noyVarIndex = noyVarIndex;
        }

        public Float getTmVarIndex() {
            return tmVarIndex;
        }

        public void setTmVarIndex(Float tmVarIndex) {
            this.tmVarIndex = tmVarIndex;
        }

        public Float getTqVarIndex() {
            return tqVarIndex;
        }

        public void setTqVarIndex(Float tqVarIndex) {
            this.tqVarIndex = tqVarIndex;
        }

        public Float getTyVarIndex() {
            return tyVarIndex;
        }

        public void setTyVarIndex(Float tyVarIndex) {
            this.tyVarIndex = tyVarIndex;
        }

        public Float getAllVarIndex() {
            return allVarIndex;
        }

        public void setAllVarIndex(Float allVarIndex) {
            this.allVarIndex = allVarIndex;
        }

        public Float getNomVarIndex99() {
            return nomVarIndex99;
        }

        public void setNomVarIndex99(Float nomVarIndex99) {
            this.nomVarIndex99 = nomVarIndex99;
        }

        public Float getNhyVarIndex99() {
            return nhyVarIndex99;
        }

        public void setNhyVarIndex99(Float nhyVarIndex99) {
            this.nhyVarIndex99 = nhyVarIndex99;
        }

        public Float getNoyVarIndex99() {
            return noyVarIndex99;
        }

        public void setNoyVarIndex99(Float noyVarIndex99) {
            this.noyVarIndex99 = noyVarIndex99;
        }

        public Float getTmVarIndex99() {
            return tmVarIndex99;
        }

        public void setTmVarIndex99(Float tmVarIndex99) {
            this.tmVarIndex99 = tmVarIndex99;
        }

        public Float getTqVarIndex99() {
            return tqVarIndex99;
        }

        public void setTqVarIndex99(Float tqVarIndex99) {
            this.tqVarIndex99 = tqVarIndex99;
        }

        public Float getTyVarIndex99() {
            return tyVarIndex99;
        }

        public void setTyVarIndex99(Float tyVarIndex99) {
            this.tyVarIndex99 = tyVarIndex99;
        }

        public Float getAllVarIndex99() {
            return allVarIndex99;
        }

        public void setAllVarIndex99(Float allVarIndex99) {
            this.allVarIndex99 = allVarIndex99;
        }

        public Float getNomUpCaptureRate() {
            return nomUpCaptureRate;
        }

        public void setNomUpCaptureRate(Float nomUpCaptureRate) {
            this.nomUpCaptureRate = nomUpCaptureRate;
        }

        public Float getNhyUpCaptureRate() {
            return nhyUpCaptureRate;
        }

        public void setNhyUpCaptureRate(Float nhyUpCaptureRate) {
            this.nhyUpCaptureRate = nhyUpCaptureRate;
        }

        public Float getNoyUpCaptureRate() {
            return noyUpCaptureRate;
        }

        public void setNoyUpCaptureRate(Float noyUpCaptureRate) {
            this.noyUpCaptureRate = noyUpCaptureRate;
        }

        public Float getTmUpCaptureRate() {
            return tmUpCaptureRate;
        }

        public void setTmUpCaptureRate(Float tmUpCaptureRate) {
            this.tmUpCaptureRate = tmUpCaptureRate;
        }

        public Float getTqUpCaptureRate() {
            return tqUpCaptureRate;
        }

        public void setTqUpCaptureRate(Float tqUpCaptureRate) {
            this.tqUpCaptureRate = tqUpCaptureRate;
        }

        public Float getTyUpCaptureRate() {
            return tyUpCaptureRate;
        }

        public void setTyUpCaptureRate(Float tyUpCaptureRate) {
            this.tyUpCaptureRate = tyUpCaptureRate;
        }

        public Float getAllUpCaptureRate() {
            return allUpCaptureRate;
        }

        public void setAllUpCaptureRate(Float allUpCaptureRate) {
            this.allUpCaptureRate = allUpCaptureRate;
        }

        public Float getNomDownCaptureRate() {
            return nomDownCaptureRate;
        }

        public void setNomDownCaptureRate(Float nomDownCaptureRate) {
            this.nomDownCaptureRate = nomDownCaptureRate;
        }

        public Float getNhyDownCaptureRate() {
            return nhyDownCaptureRate;
        }

        public void setNhyDownCaptureRate(Float nhyDownCaptureRate) {
            this.nhyDownCaptureRate = nhyDownCaptureRate;
        }

        public Float getNoyDownCaptureRate() {
            return noyDownCaptureRate;
        }

        public void setNoyDownCaptureRate(Float noyDownCaptureRate) {
            this.noyDownCaptureRate = noyDownCaptureRate;
        }

        public Float getTmDownCaptureRate() {
            return tmDownCaptureRate;
        }

        public void setTmDownCaptureRate(Float tmDownCaptureRate) {
            this.tmDownCaptureRate = tmDownCaptureRate;
        }

        public Float getTqDownCaptureRate() {
            return tqDownCaptureRate;
        }

        public void setTqDownCaptureRate(Float tqDownCaptureRate) {
            this.tqDownCaptureRate = tqDownCaptureRate;
        }

        public Float getTyDownCaptureRate() {
            return tyDownCaptureRate;
        }

        public void setTyDownCaptureRate(Float tyDownCaptureRate) {
            this.tyDownCaptureRate = tyDownCaptureRate;
        }

        public Float getAllDownCaptureRate() {
            return allDownCaptureRate;
        }

        public void setAllDownCaptureRate(Float allDownCaptureRate) {
            this.allDownCaptureRate = allDownCaptureRate;
        }

        public Float getNomInfoRatio() {
            return nomInfoRatio;
        }

        public void setNomInfoRatio(Float nomInfoRatio) {
            this.nomInfoRatio = nomInfoRatio;
        }

        public Float getNhyInfoRatio() {
            return nhyInfoRatio;
        }

        public void setNhyInfoRatio(Float nhyInfoRatio) {
            this.nhyInfoRatio = nhyInfoRatio;
        }

        public Float getNoyInfoRatio() {
            return noyInfoRatio;
        }

        public void setNoyInfoRatio(Float noyInfoRatio) {
            this.noyInfoRatio = noyInfoRatio;
        }

        public Float getTmInfoRatio() {
            return tmInfoRatio;
        }

        public void setTmInfoRatio(Float tmInfoRatio) {
            this.tmInfoRatio = tmInfoRatio;
        }

        public Float getTqInfoRatio() {
            return tqInfoRatio;
        }

        public void setTqInfoRatio(Float tqInfoRatio) {
            this.tqInfoRatio = tqInfoRatio;
        }

        public Float getTyInfoRatio() {
            return tyInfoRatio;
        }

        public void setTyInfoRatio(Float tyInfoRatio) {
            this.tyInfoRatio = tyInfoRatio;
        }

        public Float getAllInfoRatio() {
            return allInfoRatio;
        }

        public void setAllInfoRatio(Float allInfoRatio) {
            this.allInfoRatio = allInfoRatio;
        }

        public Float getNomAlgorithmVolatility() {
            return nomAlgorithmVolatility;
        }

        public void setNomAlgorithmVolatility(Float nomAlgorithmVolatility) {
            this.nomAlgorithmVolatility = nomAlgorithmVolatility;
        }

        public Float getNhyAlgorithmVolatility() {
            return nhyAlgorithmVolatility;
        }

        public void setNhyAlgorithmVolatility(Float nhyAlgorithmVolatility) {
            this.nhyAlgorithmVolatility = nhyAlgorithmVolatility;
        }

        public Float getNoyAlgorithmVolatility() {
            return noyAlgorithmVolatility;
        }

        public void setNoyAlgorithmVolatility(Float noyAlgorithmVolatility) {
            this.noyAlgorithmVolatility = noyAlgorithmVolatility;
        }

        public Float getTmAlgorithmVolatility() {
            return tmAlgorithmVolatility;
        }

        public void setTmAlgorithmVolatility(Float tmAlgorithmVolatility) {
            this.tmAlgorithmVolatility = tmAlgorithmVolatility;
        }

        public Float getTqAlgorithmVolatility() {
            return tqAlgorithmVolatility;
        }

        public void setTqAlgorithmVolatility(Float tqAlgorithmVolatility) {
            this.tqAlgorithmVolatility = tqAlgorithmVolatility;
        }

        public Float getTyAlgorithmVolatility() {
            return tyAlgorithmVolatility;
        }

        public void setTyAlgorithmVolatility(Float tyAlgorithmVolatility) {
            this.tyAlgorithmVolatility = tyAlgorithmVolatility;
        }

        public Float getAllAlgorithmVolatility() {
            return allAlgorithmVolatility;
        }

        public void setAllAlgorithmVolatility(Float allAlgorithmVolatility) {
            this.allAlgorithmVolatility = allAlgorithmVolatility;
        }

        public Float getNomMSquare() {
            return nomMSquare;
        }

        public void setNomMSquare(Float nomMSquare) {
            this.nomMSquare = nomMSquare;
        }

        public Float getNhyMSquare() {
            return nhyMSquare;
        }

        public void setNhyMSquare(Float nhyMSquare) {
            this.nhyMSquare = nhyMSquare;
        }

        public Float getNoyMSquare() {
            return noyMSquare;
        }

        public void setNoyMSquare(Float noyMSquare) {
            this.noyMSquare = noyMSquare;
        }

        public Float getTmMSquare() {
            return tmMSquare;
        }

        public void setTmMSquare(Float tmMSquare) {
            this.tmMSquare = tmMSquare;
        }

        public Float getTqMSquare() {
            return tqMSquare;
        }

        public void setTqMSquare(Float tqMSquare) {
            this.tqMSquare = tqMSquare;
        }

        public Float getTyMSquare() {
            return tyMSquare;
        }

        public void setTyMSquare(Float tyMSquare) {
            this.tyMSquare = tyMSquare;
        }

        public Float getAllMSquare() {
            return allMSquare;
        }

        public void setAllMSquare(Float allMSquare) {
            this.allMSquare = allMSquare;
        }

        public Float getNomTreynorIndex() {
            return nomTreynorIndex;
        }

        public void setNomTreynorIndex(Float nomTreynorIndex) {
            this.nomTreynorIndex = nomTreynorIndex;
        }

        public Float getNhyTreynorIndex() {
            return nhyTreynorIndex;
        }

        public void setNhyTreynorIndex(Float nhyTreynorIndex) {
            this.nhyTreynorIndex = nhyTreynorIndex;
        }

        public Float getNoyTreynorIndex() {
            return noyTreynorIndex;
        }

        public void setNoyTreynorIndex(Float noyTreynorIndex) {
            this.noyTreynorIndex = noyTreynorIndex;
        }

        public Float getTmTreynorIndex() {
            return tmTreynorIndex;
        }

        public void setTmTreynorIndex(Float tmTreynorIndex) {
            this.tmTreynorIndex = tmTreynorIndex;
        }

        public Float getTqTreynorIndex() {
            return tqTreynorIndex;
        }

        public void setTqTreynorIndex(Float tqTreynorIndex) {
            this.tqTreynorIndex = tqTreynorIndex;
        }

        public Float getTyTreynorIndex() {
            return tyTreynorIndex;
        }

        public void setTyTreynorIndex(Float tyTreynorIndex) {
            this.tyTreynorIndex = tyTreynorIndex;
        }

        public Float getAllTreynorIndex() {
            return allTreynorIndex;
        }

        public void setAllTreynorIndex(Float allTreynorIndex) {
            this.allTreynorIndex = allTreynorIndex;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getShortName() {
            return shortName;
        }

        public void setShortName(String shortName) {
            this.shortName = shortName;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getRecordNumber() {
            return recordNumber;
        }

        public void setRecordNumber(String recordNumber) {
            this.recordNumber = recordNumber;
        }

        public Byte getFundType() {
            return fundType;
        }

        public void setFundType(Byte fundType) {
            this.fundType = fundType;
        }

        public Byte getFundBreed() {
            return fundBreed;
        }

        public void setFundBreed(Byte fundBreed) {
            this.fundBreed = fundBreed;
        }

        public Byte getFundStatus() {
            return fundStatus;
        }

        public void setFundStatus(Byte fundStatus) {
            this.fundStatus = fundStatus;
        }

        public String getBuyStatus() {
            return buyStatus;
        }

        public void setBuyStatus(String buyStatus) {
            this.buyStatus = buyStatus;
        }

        public String getRedeemStatus() {
            return redeemStatus;
        }

        public void setRedeemStatus(String redeemStatus) {
            this.redeemStatus = redeemStatus;
        }

        public String getRecordDate() {
            return recordDate;
        }

        public void setRecordDate(String recordDate) {
            this.recordDate = recordDate;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getStopDate() {
            return stopDate;
        }

        public void setStopDate(String stopDate) {
            this.stopDate = stopDate;
        }

        public String getFundFilingStage() {
            return fundFilingStage;
        }

        public void setFundFilingStage(String fundFilingStage) {
            this.fundFilingStage = fundFilingStage;
        }

        public String getFundInvestmentType() {
            return fundInvestmentType;
        }

        public void setFundInvestmentType(String fundInvestmentType) {
            this.fundInvestmentType = fundInvestmentType;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getManagerType() {
            return managerType;
        }

        public void setManagerType(String managerType) {
            this.managerType = managerType;
        }

        public String getManagerName() {
            return managerName;
        }

        public void setManagerName(String managerName) {
            this.managerName = managerName;
        }

        public String getInvestmentTarget() {
            return investmentTarget;
        }

        public void setInvestmentTarget(String investmentTarget) {
            this.investmentTarget = investmentTarget;
        }

        public String getMajorInvestAreas() {
            return majorInvestAreas;
        }

        public void setMajorInvestAreas(String majorInvestAreas) {
            this.majorInvestAreas = majorInvestAreas;
        }

        public String getFundLastModifyDate() {
            return fundLastModifyDate;
        }

        public void setFundLastModifyDate(String fundLastModifyDate) {
            this.fundLastModifyDate = fundLastModifyDate;
        }

        public String getSpecialNote() {
            return specialNote;
        }

        public void setSpecialNote(String specialNote) {
            this.specialNote = specialNote;
        }

        public String getRegisteredAddress() {
            return registeredAddress;
        }

        public void setRegisteredAddress(String registeredAddress) {
            this.registeredAddress = registeredAddress;
        }

        public String getInvestmentStrategy() {
            return investmentStrategy;
        }

        public void setInvestmentStrategy(String investmentStrategy) {
            this.investmentStrategy = investmentStrategy;
        }

        public String getInvestmentSubStrategy() {
            return investmentSubStrategy;
        }

        public void setInvestmentSubStrategy(String investmentSubStrategy) {
            this.investmentSubStrategy = investmentSubStrategy;
        }

        public List<String> getFundManagerIds() {
            return fundManagerIds;
        }

        public void setFundManagerIds(List<String> fundManagerIds) {
            this.fundManagerIds = fundManagerIds;
        }

        public String getCompanyId() {
            return companyId;
        }

        public void setCompanyId(String companyId) {
            this.companyId = companyId;
        }

        public Long getOrderNum() {
            return orderNum;
        }

        public void setOrderNum(Long orderNum) {
            this.orderNum = orderNum;
        }

        public String getCreateScale() {
            return createScale;
        }

        public void setCreateScale(String createScale) {
            this.createScale = createScale;
        }

        public String getLatestScale() {
            return latestScale;
        }

        public void setLatestScale(String latestScale) {
            this.latestScale = latestScale;
        }

        public String getBenchmark() {
            return benchmark;
        }

        public void setBenchmark(String benchmark) {
            this.benchmark = benchmark;
        }

        public Byte getNetValueUpdateRate() {
            return netValueUpdateRate;
        }

        public void setNetValueUpdateRate(Byte netValueUpdateRate) {
            this.netValueUpdateRate = netValueUpdateRate;
        }

        public String getFundOuterId() {
            return fundOuterId;
        }

        public void setFundOuterId(String fundOuterId) {
            this.fundOuterId = fundOuterId;
        }

        public String getTags() {
            return tags;
        }

        public void setTags(String tags) {
            this.tags = tags;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getStrategyCapacity() {
            return strategyCapacity;
        }

        public void setStrategyCapacity(String strategyCapacity) {
            this.strategyCapacity = strategyCapacity;
        }

        public Long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(Long createTime) {
            this.createTime = createTime;
        }

        public String getCreatorId() {
            return creatorId;
        }

        public void setCreatorId(String creatorId) {
            this.creatorId = creatorId;
        }

        public Long getLastModifyTime() {
            return lastModifyTime;
        }

        public void setLastModifyTime(Long lastModifyTime) {
            this.lastModifyTime = lastModifyTime;
        }

        public String getLastModifierId() {
            return lastModifierId;
        }

        public void setLastModifierId(String lastModifierId) {
            this.lastModifierId = lastModifierId;
        }

        public String getCompanyOuterId() {
            return companyOuterId;
        }

        public void setCompanyOuterId(String companyOuterId) {
            this.companyOuterId = companyOuterId;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public List<String> getManagerOuterIds() {
            return managerOuterIds;
        }

        public void setManagerOuterIds(List<String> managerOuterIds) {
            this.managerOuterIds = managerOuterIds;
        }

        public List<String> getFundIds() {
            return fundIds;
        }

        public void setFundIds(List<String> fundIds) {
            this.fundIds = fundIds;
        }

        public List<String> getCompanyIds() {
            return companyIds;
        }

        public void setCompanyIds(List<String> companyIds) {
            this.companyIds = companyIds;
        }

        public Float getStartAnnualizedReturn() {
            return startAnnualizedReturn;
        }

        public void setStartAnnualizedReturn(Float startAnnualizedReturn) {
            this.startAnnualizedReturn = startAnnualizedReturn;
        }

        public Float getEndAnnualizedReturn() {
            return endAnnualizedReturn;
        }

        public void setEndAnnualizedReturn(Float endAnnualizedReturn) {
            this.endAnnualizedReturn = endAnnualizedReturn;
        }

        public String getTimeInterval() {
            return timeInterval;
        }

        public void setTimeInterval(String timeInterval) {
            this.timeInterval = timeInterval;
        }

        public List<String> getFundManagerNames() {
            return fundManagerNames;
        }

        public void setFundManagerNames(List<String> fundManagerNames) {
            this.fundManagerNames = fundManagerNames;
        }

        public String getFundStatusName() {
            return fundStatusName;
        }

        public void setFundStatusName(String fundStatusName) {
            this.fundStatusName = fundStatusName;
        }

        public String getFundTypeName() {
            return fundTypeName;
        }

        public void setFundTypeName(String fundTypeName) {
            this.fundTypeName = fundTypeName;
        }

        public Byte getIsConcern() {
            return isConcern;
        }

        public void setIsConcern(Byte isConcern) {
            this.isConcern = isConcern;
        }

        public Float getConfigWeight() {
            return configWeight;
        }

        public void setConfigWeight(Float configWeight) {
            this.configWeight = configWeight;
        }

        public String getNetValueDateString() {
            return netValueDateString;
        }

        public void setNetValueDateString(String netValueDateString) {
            this.netValueDateString = netValueDateString;
        }

        public String getManagerId() {
            return managerId;
        }

        public void setManagerId(String managerId) {
            this.managerId = managerId;
        }

        public String getTagId() {
            return tagId;
        }

        public void setTagId(String tagId) {
            this.tagId = tagId;
        }
    }
}