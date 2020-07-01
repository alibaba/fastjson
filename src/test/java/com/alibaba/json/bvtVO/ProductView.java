package com.alibaba.json.bvtVO;

public class ProductView {

    private Integer id;
    private String  keyword;
    private boolean hasProduct;
    private boolean hasCompany;
    private boolean hasBuyLead;
    private String  country;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public boolean isHasProduct() {
        return hasProduct;
    }

    public void setHasProduct(boolean hasProduct) {
        this.hasProduct = hasProduct;
    }

    public boolean isHasCompany() {
        return hasCompany;
    }

    public void setHasCompany(boolean hasCompany) {
        this.hasCompany = hasCompany;
    }

    public boolean isHasBuyLead() {
        return hasBuyLead;
    }

    public void setHasBuyLead(boolean hasBuyLead) {
        this.hasBuyLead = hasBuyLead;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
