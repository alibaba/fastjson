package com.alibaba.json.demo.hibernate.data;


import javax.persistence.Column;
import javax.persistence.Embeddable;

@SuppressWarnings("serial")
@Embeddable
public class OrderDetailId  implements java.io.Serializable {


     private Integer orderNumber;
     private String productCode;

    public OrderDetailId() {
    }

    public OrderDetailId(Integer orderNumber, String productCode) {
       this.orderNumber = orderNumber;
       this.productCode = productCode;
    }
   

    @Column(name="orderNumber", nullable=false)
    public Integer getOrderNumber() {
        return this.orderNumber;
    }
    
    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    @Column(name="productCode", nullable=false, length=50)
    public String getProductCode() {
        return this.productCode;
    }
    
    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    @Override
    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( (other == null ) ) return false;
        if ( !(other instanceof OrderDetailId) ) return false;
        OrderDetailId castOther = ( OrderDetailId ) other; 

        return ( (this.getOrderNumber()==castOther.getOrderNumber()) || ( this.getOrderNumber()!=null && castOther.getOrderNumber()!=null && this.getOrderNumber().equals(castOther.getOrderNumber()) ) )
                && ( (this.getProductCode()==castOther.getProductCode()) || ( this.getProductCode()!=null && castOther.getProductCode()!=null && this.getProductCode().equals(castOther.getProductCode()) ) );
    }
   
    @Override
    public int hashCode() {
        int result = 17;

        result = 37 * result + ( getOrderNumber() == null ? 0 : this.getOrderNumber().hashCode() );
        result = 37 * result + ( getProductCode() == null ? 0 : this.getProductCode().hashCode() );
        return result;
    }   
}


