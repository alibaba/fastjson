package com.alibaba.json.demo.hibernate.data;


import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name="Product"
    ,catalog="classicmodels"
)
public class Product  implements java.io.Serializable {


     private String productCode;
     private String productName;
     private String productLine;
     private String productScale;
     private String productVendor;
     private String productDescription;
     private short quantityInStock;
     private double buyPrice;
     private double msrp;
     private Set<OrderDetail> orderDetails = new HashSet<OrderDetail>(0);

    public Product() {
    }

	
    public Product(String productCode, String productName, String productLine, String productScale, String productVendor, String productDescription, short quantityInStock, double buyPrice, double msrp) {
        this.productCode = productCode;
        this.productName = productName;
        this.productLine = productLine;
        this.productScale = productScale;
        this.productVendor = productVendor;
        this.productDescription = productDescription;
        this.quantityInStock = quantityInStock;
        this.buyPrice = buyPrice;
        this.msrp = msrp;
    }
    public Product(String productCode, String productName, String productLine, String productScale, String productVendor, String productDescription, short quantityInStock, double buyPrice, double msrp, Set<OrderDetail> orderDetails) {
       this.productCode = productCode;
       this.productName = productName;
       this.productLine = productLine;
       this.productScale = productScale;
       this.productVendor = productVendor;
       this.productDescription = productDescription;
       this.quantityInStock = quantityInStock;
       this.buyPrice = buyPrice;
       this.msrp = msrp;
       this.orderDetails = orderDetails;
    }
   
     @Id 
    
    @Column(name="productCode", unique=true, nullable=false, length=50)
    public String getProductCode() {
        return this.productCode;
    }
    
    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }
    
    @Column(name="productName", nullable=false, length=70)
    public String getProductName() {
        return this.productName;
    }
    
    public void setProductName(String productName) {
        this.productName = productName;
    }
    
    @Column(name="productLine", nullable=false, length=50)
    public String getProductLine() {
        return this.productLine;
    }
    
    public void setProductLine(String productLine) {
        this.productLine = productLine;
    }
    
    @Column(name="productScale", nullable=false, length=10)
    public String getProductScale() {
        return this.productScale;
    }
    
    public void setProductScale(String productScale) {
        this.productScale = productScale;
    }
    
    @Column(name="productVendor", nullable=false, length=50)
    public String getProductVendor() {
        return this.productVendor;
    }
    
    public void setProductVendor(String productVendor) {
        this.productVendor = productVendor;
    }
    
    @Column(name="productDescription", nullable=false, length=65535)
    public String getProductDescription() {
        return this.productDescription;
    }
    
    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }
    
    @Column(name="quantityInStock", nullable=false)
    public short getQuantityInStock() {
        return this.quantityInStock;
    }
    
    public void setQuantityInStock(short quantityInStock) {
        this.quantityInStock = quantityInStock;
    }
    
    @Column(name="buyPrice", nullable=false, precision=22, scale=0)
    public double getBuyPrice() {
        return this.buyPrice;
    }
    
    public void setBuyPrice(double buyPrice) {
        this.buyPrice = buyPrice;
    }
    
    @Column(name="MSRP", nullable=false, precision=22, scale=0)
    public double getMsrp() {
        return this.msrp;
    }
    
    public void setMsrp(double msrp) {
        this.msrp = msrp;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="product")
    public Set<OrderDetail> getOrderDetails() {
        return this.orderDetails;
    }
    
    public void setOrderDetails(Set<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }




}


