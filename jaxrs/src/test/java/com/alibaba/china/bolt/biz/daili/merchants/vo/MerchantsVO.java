package com.alibaba.china.bolt.biz.daili.merchants.vo;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.alibaba.fastjson.JSON;


/**
 * 商家基本信息
 * @author hongwei.quhw
 *
 */
public class MerchantsVO implements Serializable {
	/**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    /** -----------------实体商家信息----------**/
	//店铺类型code
	private String type;
	//店铺类型name
    private String typename;
	//招商区域code
	private String[] region;
	//招商区域name
	private String[] regionname;
	//最小面积
	private Integer minarea = -1;
	//最大面积
	private Integer maxarea;
	//启动资金
	private Long initialcapital;
	//加盟保证金
	private Long cashdeposit;
	/** -----------------网络商家信息----------**/
	//招商渠道搜索索引名
	private String[] shoptype;
	//招商渠道搜索名称
    private String[] shoptypename;
	//主营类目
	private String[] categoryids;
	//主营类目名称
	private String[] categoryidsname;
	
	/** -----------------商家共有信息----------**/
	//MemberId
	private String memberid;
	//商家类型
	private int merchantstype;
	//是否已删除
	private boolean isdelete;
	//招商截止日期
	private Date expirationdate;
	//旺旺
	private String wangwang;
	//联系电话
	private String tel;
	//是否品牌 
	private boolean hasbrand;
	//（30天）加盟人数 
	private int joincount;
	//公司旺铺地址
    private String winportdomain;
	/** ----------------下面是品牌库信息----------------  **/
	//logo/商标 图片URL
	private String brandlogourl;
	//品牌名称
	private String brandname;
	//创立时间
	private Date brandfoundtime;
	//详情
	private String brandintroduction;
	//证书  图片URL 
	private String brandcertificateurl; 
	
	/** ----------------下面是公司库信息----------------  **/
	//公司名
	private String companyname;
	//成立年份
	private String companyestablishedyear;
	//注册资本
	private Double companyregcapital;
	//注册地
	private String companyfoundedplace;
	//简介
    private String companyintroduction;
    
	/** ----------------下面是DW交易信息----------------  **/
	//最近30天代理商支付订单金额（单位为分）
	private Double payordamt30;
	//最近30天代理商支付订单数
	private long payordcnt30;
	//最近30天支付订单代理商买家数
	private long payordbuyercnt30;
	//最近90天旺铺回头率
	private Double returnordrate90;
	//截至当日成功申请代理商人数
	private int membercnttd;
	//主营一级类目ID（主营top1、top2、top3一级类目以char(6)拼装成一串，需要解析出top1一级类目即可）example:"7"
	private String stdcategoryid1;
	//主营二级类目ID（主营top1、top2、top3二级类目以char(6)拼装成一串，需要解析出top1二级类目即可）example:"7"
	private String stdcategoryid2;
	//主营一级类目ID名称 example:"服装"
    private String stdcategoryname1;
    //主营二级类目ID名称 example:"男装"
    private String stdcategoryname2;
    
    /** ----------------下面是offer信息----------------  **/
    //offer缩略图url地址
    private String[]  summimageurilist;
    //offer链接url地址
    private String[] detailurl;
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public String[] getRegion() {
        return region;
    }

    public void setRegionArray(String[] region) {
        this.region = region;
    }

    public String[] getRegionname() {
        return regionname;
    }

    public void setRegionnameArray(String[] regionname) {
        this.regionname = regionname;
    }

    public Integer getMinarea() {
		return minarea;
	}

	public void setMinarea(Integer minarea) {
		if (null == minarea) {
			minarea = -1; //opensearch 搜索空问题 ，设置默认值
		}
		this.minarea = minarea;
	}

	public Integer getMaxarea() {
		return maxarea;
	}

	public void setMaxarea(Integer maxarea) {
		this.maxarea = maxarea;
	}

	public Long getInitialcapital() {
        return initialcapital;
    }

    public void setInitialcapital(Long initialcapital) {
        this.initialcapital = initialcapital;
    }

    public Long getCashdeposit() {
        return cashdeposit;
    }

    public void setCashdeposit(Long cashdeposit) {
        this.cashdeposit = cashdeposit;
    }

    public String[] getShoptype() {
        return shoptype;
    }

    public void setShoptypeArray(String[] shoptype) {
        this.shoptype = shoptype;
    }

    public String[] getShoptypename() {
        return shoptypename;
    }

    public void setShoptypenameArray(String[] shoptypename) {
        this.shoptypename = shoptypename;
    }

    public String[] getCategoryids() {
        return categoryids;
    }

    public void setCategoryidsArray(String[] categoryids) {
        this.categoryids = categoryids;
    }

    public String[] getCategoryidsname() {
        return categoryidsname;
    }

    public void setCategoryidsnameArray(String[] categoryidsname) {
        this.categoryidsname = categoryidsname;
    }

    public String getMemberid() {
        return memberid;
    }

    public void setMemberid(String memberid) {
        this.memberid = memberid;
    }

    public int getMerchantstype() {
        return merchantstype;
    }

    public void setMerchantstype(int merchantstype) {
        this.merchantstype = merchantstype;
    }

    public boolean isIsdelete() {
        return isdelete;
    }

    public void setIsdelete(boolean isdelete) {
        this.isdelete = isdelete;
    }

    public Date getExpirationdate() {
        return expirationdate;
    }

    public void setExpirationdate(Date expirationdate) {
        this.expirationdate = expirationdate;
    }

    public String getWangwang() {
        return wangwang;
    }

    public void setWangwang(String wangwang) {
        this.wangwang = wangwang;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public boolean isHasbrand() {
        return hasbrand;
    }

    public void setHasbrand(boolean hasbrand) {
        this.hasbrand = hasbrand;
    }

    public int getJoincount() {
        return joincount;
    }

    public void setJoincount(int joincount) {
        this.joincount = joincount;
    }

    public String getWinportdomain() {
        return winportdomain;
    }

    public void setWinportdomain(String winportdomain) {
        this.winportdomain = winportdomain;
    }

    public String getBrandlogourl() {
        return brandlogourl;
    }

    public void setBrandlogourl(String brandlogourl) {
        this.brandlogourl = brandlogourl;
    }

    public String getBrandname() {
        return brandname;
    }

    public void setBrandname(String brandname) {
        this.brandname = brandname;
    }

    public Date getBrandfoundtime() {
        return brandfoundtime;
    }

    public void setBrandfoundtime(Date brandfoundtime) {
        this.brandfoundtime = brandfoundtime;
    }

    public String getBrandintroduction() {
        return brandintroduction;
    }

    public void setBrandintroduction(String brandintroduction) {
        this.brandintroduction = brandintroduction;
    }

    public String getBrandcertificateurl() {
        return brandcertificateurl;
    }

    public void setBrandcertificateurl(String brandcertificateurl) {
        this.brandcertificateurl = brandcertificateurl;
    }

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    public String getCompanyestablishedyear() {
        return companyestablishedyear;
    }

    public void setCompanyestablishedyear(String companyestablishedyear) {
        this.companyestablishedyear = companyestablishedyear;
    }

    public Double getCompanyregcapital() {
        return companyregcapital;
    }

    public void setCompanyregcapital(Double companyregcapital) {
        this.companyregcapital = companyregcapital;
    }

    public String getCompanyfoundedplace() {
        return companyfoundedplace;
    }

    public void setCompanyfoundedplace(String companyfoundedplace) {
        this.companyfoundedplace = companyfoundedplace;
    }

    public String getCompanyintroduction() {
        return companyintroduction;
    }

    public void setCompanyintroduction(String companyintroduction) {
        this.companyintroduction = companyintroduction;
    }

    public String[] getSummimageurilist() {
        return summimageurilist;
    }

    public void setSummimageurilistArray(String[] summimageuriList) {
        this.summimageurilist = summimageuriList;
    }

    public String[] getDetailurl() {
        return detailurl;
    }
    
    public void setDetailurlArray(String[] detailurl) {
        this.detailurl = detailurl;
    }
    
    public String getExpirationdateForString(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日", JSON.defaultLocale);
        dateFormat.setTimeZone(JSON.defaultTimeZone);
        return dateFormat.format(this.expirationdate);
     }

    /**
     * 为opensearch特供
     * @param region
     */
    public void setRegion(String region) {
        this.region = region== null ?new String[0]:region.split("\\t");
    }

    public void setRegionname(String regionname) {
        this.regionname = regionname== null ?new String[0]:regionname.split("\\t");;
    }

    public void setShoptype(String shoptype) {
        this.shoptype = shoptype== null ?new String[0]:shoptype.split("\\t");;
    }

    public void setShoptypename(String shoptypename) {
        this.shoptypename = shoptypename== null ?new String[0]:shoptypename.split("\\t");;
    }

    public void setCategoryids(String categoryids) {
        this.categoryids = categoryids== null ?new String[0]:categoryids.split("\\t");;
    }

    public void setCategoryidsname(String categoryidsname) {
        this.categoryidsname = categoryidsname== null ?new String[0]:categoryidsname.split("\\t");;
    }

    public void setSummimageurilist(String summimageuriList) {
        this.summimageurilist = summimageuriList== null ?new String[0]:summimageuriList.split("\\t");;
    }

    public void setDetailurl(String detailurl) {
        this.detailurl = detailurl== null ?new String[0]:detailurl.split("\\t");;
    }

    /**
     * @return the payordamt30
     */
    public Double getPayordamt30() {
        return payordamt30;
    }

    /**
     * @param payordamt30 the payordamt30 to set
     */
    public void setPayordamt30(Double payordamt30) {
        this.payordamt30 = payordamt30;
    }

    /**
     * @return the payordcnt30
     */
    public long getPayordcnt30() {
        return payordcnt30;
    }

    /**
     * @param payordcnt30 the payordcnt30 to set
     */
    public void setPayordcnt30(long payordcnt30) {
        this.payordcnt30 = payordcnt30;
    }

    /**
     * @return the payordbuyercnt30
     */
    public long getPayordbuyercnt30() {
        return payordbuyercnt30;
    }

    /**
     * @param payordbuyercnt30 the payordbuyercnt30 to set
     */
    public void setPayordbuyercnt30(long payordbuyercnt30) {
        this.payordbuyercnt30 = payordbuyercnt30;
    }

    /**
     * @return the returnordrate90
     */
    public Double getReturnordrate90() {
        return returnordrate90;
    }

    /**
     * @param returnordrate90 the returnordrate90 to set
     */
    public void setReturnordrate90(Double returnordrate90) {
        this.returnordrate90 = returnordrate90;
    }

    /**
     * @return the membercnttd
     */
    public int getMembercnttd() {
        return membercnttd;
    }

    /**
     * @param membercnttd the membercnttd to set
     */
    public void setMembercnttd(int membercnttd) {
        this.membercnttd = membercnttd;
    }

    public String getStdcategoryid1() {
        return stdcategoryid1;
    }

    public void setStdcategoryid1(String stdcategoryid1) {
        this.stdcategoryid1 = stdcategoryid1;
    }

    public String getStdcategoryid2() {
        return stdcategoryid2;
    }

    public void setStdcategoryid2(String stdcategoryid2) {
        this.stdcategoryid2 = stdcategoryid2;
    }

    public String getStdcategoryname1() {
        return stdcategoryname1;
    }

    public void setStdcategoryname1(String stdcategoryname1) {
        this.stdcategoryname1 = stdcategoryname1;
    }

    public String getStdcategoryname2() {
        return stdcategoryname2;
    }

    public void setStdcategoryname2(String stdcategoryname2) {
        this.stdcategoryname2 = stdcategoryname2;
    }

  
	
}