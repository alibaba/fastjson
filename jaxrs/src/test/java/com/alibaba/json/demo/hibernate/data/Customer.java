package com.alibaba.json.demo.hibernate.data;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import static javax.persistence.GenerationType.IDENTITY;

import com.fasterxml.jackson.annotation.*;

@Entity
@Table(name="Customer", catalog="classicmodels")
public class Customer implements java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    private Integer customerNumber;
    private Employee employee;
    private String customerName;
    private String contactLastName;
    private String contactFirstName;
    private String phone;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private Double creditLimit;
    private Set<Payment> payments = new HashSet<Payment>();
    private Set<Order> orders = new HashSet<Order>();

    public Customer() { }

    public Customer(String customerName, String contactLastName, String contactFirstName, String phone, String addressLine1, String city, String country) {
        this.customerName = customerName;
        this.contactLastName = contactLastName;
        this.contactFirstName = contactFirstName;
        this.phone = phone;
        this.addressLine1 = addressLine1;
        this.city = city;
        this.country = country;
    }

    public Customer(Employee employee, String customerName, String contactLastName, String contactFirstName, String phone, String addressLine1, String addressLine2, String city, String state, String postalCode, String country, Double creditLimit, Set<Payment> payments, Set<Order> orders) {
       this.employee = employee;
       this.customerName = customerName;
       this.contactLastName = contactLastName;
       this.contactFirstName = contactFirstName;
       this.phone = phone;
       this.addressLine1 = addressLine1;
       this.addressLine2 = addressLine2;
       this.city = city;
       this.state = state;
       this.postalCode = postalCode;
       this.country = country;
       this.creditLimit = creditLimit;
       this.payments = payments;
       this.orders = orders;
    }

    @Id @GeneratedValue(strategy=IDENTITY)
    @Column(name="customerNumber", unique=true, nullable=false)
    public Integer getCustomerNumber() {
        return this.customerNumber;
    }

     public void setCustomerNumber(Integer customerNumber) {
        this.customerNumber = customerNumber;
    }

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="salesRepEmployeeNumber")
    @JsonBackReference
    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @Column(name="customerName", nullable=false, length=50)
    public String getCustomerName() {
        return this.customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    @Column(name="contactLastName", nullable=false, length=50)
    public String getContactLastName() {
        return this.contactLastName;
    }

    public void setContactLastName(String contactLastName) {
        this.contactLastName = contactLastName;
    }

    @Column(name="contactFirstName", nullable=false, length=50)
    public String getContactFirstName() {
        return this.contactFirstName;
    }

    public void setContactFirstName(String contactFirstName) {
        this.contactFirstName = contactFirstName;
    }
    
    @Column(name="phone", nullable=false, length=50)
    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Column(name="addressLine1", nullable=false, length=50)
    public String getAddressLine1() {
        return this.addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    @Column(name="addressLine2", length=50)
    public String getAddressLine2() {
        return this.addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    @Column(name="city", nullable=false, length=50)
    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Column(name="state", length=50)
    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Column(name="postalCode", length=15)
    public String getPostalCode() {
        return this.postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    @Column(name="country", nullable=false, length=50)
    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Column(name="creditLimit", precision=22, scale=0)
    public Double getCreditLimit() {
        return this.creditLimit;
    }

    public void setCreditLimit(Double creditLimit) {
        this.creditLimit = creditLimit;
    }

    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="customer")
    public Set<Order> getOrders() {
        return this.orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="customer")
    public Set<Payment> getPayments() {
        return this.payments;
    }

    public void setPayments(Set<Payment> payments) {
        this.payments = payments;
    }
}
