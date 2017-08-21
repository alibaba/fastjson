package com.alibaba.json.demo.hibernate.data;


import com.fasterxml.jackson.annotation.JsonBackReference;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name="\"ORDER\""
    ,catalog="classicmodels"
)
public class Order  implements java.io.Serializable {


     private Integer orderNumber;
     private Customer customer;
     private Date orderDate;
     private Date requiredDate;
     private Date shippedDate;
     private String status;
     private String comments;
     private Set<OrderDetail> orderDetails = new HashSet<OrderDetail>(0);

    public Order() {
    }

	
    public Order(Customer customer, Date orderDate, Date requiredDate, String status) {
        this.customer = customer;
        this.orderDate = orderDate;
        this.requiredDate = requiredDate;
        this.status = status;
    }
    public Order(Customer customer, Date orderDate, Date requiredDate, Date shippedDate, String status, String comments, Set<OrderDetail> orderDetails) {
       this.customer = customer;
       this.orderDate = orderDate;
       this.requiredDate = requiredDate;
       this.shippedDate = shippedDate;
       this.status = status;
       this.comments = comments;
       this.orderDetails = orderDetails;
    }
   
     @Id @GeneratedValue(strategy=IDENTITY)
    
    @Column(name="orderNumber", unique=true, nullable=false)
    public Integer getOrderNumber() {
        return this.orderNumber;
    }
    
    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="customerNumber", nullable=false)
    @JsonBackReference("order-customer")
    public Customer getCustomer() {
        return this.customer;
    }
    
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
    @Column(name="orderDate", nullable=false, length=19)
    public Date getOrderDate() {
        return this.orderDate;
    }
    
    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }
    
    @Column(name="requiredDate", nullable=false, length=19)
    public Date getRequiredDate() {
        return this.requiredDate;
    }
    
    public void setRequiredDate(Date requiredDate) {
        this.requiredDate = requiredDate;
    }
    
    @Column(name="shippedDate", length=19)
    public Date getShippedDate() {
        return this.shippedDate;
    }
    
    public void setShippedDate(Date shippedDate) {
        this.shippedDate = shippedDate;
    }
    
    @Column(name="status", nullable=false, length=15)
    public String getStatus() {
        return this.status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    @Column(name="comments", length=65535)
    public String getComments() {
        return this.comments;
    }
    
    public void setComments(String comments) {
        this.comments = comments;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="order")
    public Set<OrderDetail> getOrderDetails() {
        return this.orderDetails;
    }
    
    public void setOrderDetails(Set<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }




}


