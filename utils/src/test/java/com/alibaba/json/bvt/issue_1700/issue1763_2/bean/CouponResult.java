package com.alibaba.json.bvt.issue_1700.issue1763_2.bean;


/**
 *
 * @author cnlyml
 */
public class CouponResult{
    /**
     * 优惠券ID
     */
    private Long id;

    /**
     * 优惠券名称
     */
    private String couponName;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

}
