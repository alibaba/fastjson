package com.alibaba.json.demo.hibernate.data;


import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name="OrderDetail"
    ,catalog="classicmodels"
)
public class OrderDetail  implements java.io.Serializable {


     private OrderDetailId id;
     private Order order;
     private Product product;
     private Integer quantityOrdered;
     private double priceEach;
     private short orderLineNumber;

    public OrderDetail() {
    }

    public OrderDetail(OrderDetailId id, Order order, Product product, Integer quantityOrdered, double priceEach, short orderLineNumber) {
       this.id = id;
       this.order = order;
       this.product = product;
       this.quantityOrdered = quantityOrdered;
       this.priceEach = priceEach;
       this.orderLineNumber = orderLineNumber;
    }
   
     @EmbeddedId
    
    @AttributeOverrides( {
        @AttributeOverride(name="orderNumber", column=@Column(name="orderNumber", nullable=false) ), 
        @AttributeOverride(name="productCode", column=@Column(name="productCode", nullable=false, length=50) ) } )
    public OrderDetailId getId() {
        return this.id;
    }
    
    public void setId(OrderDetailId id) {
        this.id = id;
    }
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="orderNumber", nullable=false, insertable=false, updatable=false)
    @JsonBackReference("orderdetail-order")
    public Order getOrder() {
        return this.order;
    }
    
    public void setOrder(Order order) {
        this.order = order;
    }
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="productCode", nullable=false, insertable=false, updatable=false)
    @JsonBackReference("order-product")
    public Product getProduct() {
        return this.product;
    }
    
    public void setProduct(Product product) {
        this.product = product;
    }
    
    @Column(name="quantityOrdered", nullable=false)
    public Integer getQuantityOrdered() {
        return this.quantityOrdered;
    }
    
    public void setQuantityOrdered(Integer quantityOrdered) {
        this.quantityOrdered = quantityOrdered;
    }
    
    @Column(name="priceEach", nullable=false, precision=22, scale=0)
    public double getPriceEach() {
        return this.priceEach;
    }
    
    public void setPriceEach(double priceEach) {
        this.priceEach = priceEach;
    }
    
    @Column(name="orderLineNumber", nullable=false)
    public short getOrderLineNumber() {
        return this.orderLineNumber;
    }
    
    public void setOrderLineNumber(short orderLineNumber) {
        this.orderLineNumber = orderLineNumber;
    }




}


