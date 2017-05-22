package com.alibaba.json.demo.hibernate.data;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@SuppressWarnings("serial")
@Entity
@Table(name = "Payment", catalog = "classicmodels")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class Payment implements java.io.Serializable
{
    private PaymentId id;
    private Customer customer;
    private Date paymentDate;
    private double amount;

    public Payment() { }

    public Payment(PaymentId id, Customer customer, Date paymentDate,
            double amount) {
        this.id = id;
        this.customer = customer;
        this.paymentDate = paymentDate;
        this.amount = amount;
    }

        @EmbeddedId
        @AttributeOverrides({
                        @AttributeOverride(name = "customerNumber", column = @Column(name = "customerNumber", nullable = false)),
                        @AttributeOverride(name = "checkNumber", column = @Column(name = "checkNumber", nullable = false, length = 50)) })
        public PaymentId getId() {
                return this.id;
        }

        public void setId(PaymentId id) {
                this.id = id;
        }

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "customerNumber", nullable = false, insertable = false, updatable = false)
        @JsonIgnore
        public Customer getCustomer() {
                return this.customer;
        }

        public void setCustomer(Customer customer) {
                this.customer = customer;
        }

        @Column(name = "paymentDate", nullable = false, length = 19)
        public Date getPaymentDate() {
                return this.paymentDate;
        }

        public void setPaymentDate(Date paymentDate) {
                this.paymentDate = paymentDate;
        }

        @Column(name = "amount", nullable = false, precision = 22, scale = 0)
        public double getAmount() {
                return this.amount;
        }

        public void setAmount(double amount) {
                this.amount = amount;
        }
}
