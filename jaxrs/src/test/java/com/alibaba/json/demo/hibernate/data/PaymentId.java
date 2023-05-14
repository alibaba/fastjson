package com.alibaba.json.demo.hibernate.data;


import javax.persistence.Column;
import javax.persistence.Embeddable;

@SuppressWarnings("serial")
@Embeddable
public class PaymentId  implements java.io.Serializable
{
    private Integer customerNumber;
    private String checkNumber;

    public PaymentId() { }

    public PaymentId(Integer customerNumber, String checkNumber) {
       this.customerNumber = customerNumber;
       this.checkNumber = checkNumber;
    }
   

    @Column(name="customerNumber", nullable=false)
    public Integer getCustomerNumber() {
        return this.customerNumber;
    }
    
    public void setCustomerNumber(Integer customerNumber) {
        this.customerNumber = customerNumber;
    }

    @Column(name="checkNumber", nullable=false, length=50)
    public String getCheckNumber() {
        return this.checkNumber;
    }
    
    public void setCheckNumber(String checkNumber) {
        this.checkNumber = checkNumber;
    }

    @Override
    public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof PaymentId) ) return false;
		 PaymentId castOther = ( PaymentId ) other; 
         
		 return ( (this.getCustomerNumber()==castOther.getCustomerNumber()) || ( this.getCustomerNumber()!=null && castOther.getCustomerNumber()!=null && this.getCustomerNumber().equals(castOther.getCustomerNumber()) ) )
		         && ( (this.getCheckNumber()==castOther.getCheckNumber()) || ( this.getCheckNumber()!=null && castOther.getCheckNumber()!=null && this.getCheckNumber().equals(castOther.getCheckNumber()) ) );
    }

    @Override
    public int hashCode() {
        int result = 17;

        result = 37 * result + ( getCustomerNumber() == null ? 0 : this.getCustomerNumber().hashCode() );
        result = 37 * result + ( getCheckNumber() == null ? 0 : this.getCheckNumber().hashCode() );
        return result;
    }   
}


