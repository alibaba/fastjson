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
@Table(name="Office"
    ,catalog="classicmodels"
)
public class Office  implements java.io.Serializable {


     private String officeCode;
     private String city;
     private String phone;
     private String addressLine1;
     private String addressLine2;
     private String state;
     private String country;
     private String postalCode;
     private String territory;
     private Set<Employee> employees = new HashSet<Employee>(0);

    public Office() {
    }

	
    public Office(String officeCode, String city, String phone, String addressLine1, String country, String postalCode, String territory) {
        this.officeCode = officeCode;
        this.city = city;
        this.phone = phone;
        this.addressLine1 = addressLine1;
        this.country = country;
        this.postalCode = postalCode;
        this.territory = territory;
    }
    public Office(String officeCode, String city, String phone, String addressLine1, String addressLine2, String state, String country, String postalCode, String territory, Set<Employee> employees) {
       this.officeCode = officeCode;
       this.city = city;
       this.phone = phone;
       this.addressLine1 = addressLine1;
       this.addressLine2 = addressLine2;
       this.state = state;
       this.country = country;
       this.postalCode = postalCode;
       this.territory = territory;
       this.employees = employees;
    }
   
     @Id 
    
    @Column(name="officeCode", unique=true, nullable=false, length=50)
    public String getOfficeCode() {
        return this.officeCode;
    }
    
    public void setOfficeCode(String officeCode) {
        this.officeCode = officeCode;
    }
    
    @Column(name="city", nullable=false, length=50)
    public String getCity() {
        return this.city;
    }
    
    public void setCity(String city) {
        this.city = city;
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
    
    @Column(name="state", length=50)
    public String getState() {
        return this.state;
    }
    
    public void setState(String state) {
        this.state = state;
    }
    
    @Column(name="country", nullable=false, length=50)
    public String getCountry() {
        return this.country;
    }
    
    public void setCountry(String country) {
        this.country = country;
    }
    
    @Column(name="postalCode", nullable=false, length=10)
    public String getPostalCode() {
        return this.postalCode;
    }
    
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
    
    @Column(name="territory", nullable=false, length=10)
    public String getTerritory() {
        return this.territory;
    }
    
    public void setTerritory(String territory) {
        this.territory = territory;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="office")
    public Set<Employee> getEmployees() {
        return this.employees;
    }
    
    public void setEmployees(Set<Employee> employees) {
        this.employees = employees;
    }




}


