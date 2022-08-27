package com.alibaba.json.demo.hibernate.data;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import static javax.persistence.GenerationType.IDENTITY;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@SuppressWarnings("serial")
@Entity
@Table(name="Employee"
    ,catalog="classicmodels"
)
public class Employee  implements java.io.Serializable
{
     private Integer employeeNumber;
     private Office office;
     private String lastName;
     private String firstName;
     private String extension;
     private String email;
     private Integer reportsTo;
     private String jobTitle;
     
     private Set<Customer> customers = new HashSet<Customer>();

    public Employee() { }
	
    public Employee(Office office, String lastName, String firstName, String extension, String email, String jobTitle) {
        this.office = office;
        this.lastName = lastName;
        this.firstName = firstName;
        this.extension = extension;
        this.email = email;
        this.jobTitle = jobTitle;
    }
    public Employee(Office office, String lastName, String firstName, String extension, String email, Integer reportsTo, String jobTitle, Set<Customer> customers) {
       this.office = office;
       this.lastName = lastName;
       this.firstName = firstName;
       this.extension = extension;
       this.email = email;
       this.reportsTo = reportsTo;
       this.jobTitle = jobTitle;
       this.customers = customers;
    }
   
     @Id @GeneratedValue(strategy=IDENTITY)
    
    @Column(name="employeeNumber", unique=true, nullable=false)
    public Integer getEmployeeNumber() {
        return this.employeeNumber;
    }
    
    public void setEmployeeNumber(Integer employeeNumber) {
        this.employeeNumber = employeeNumber;
    }
@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="officeCode", nullable=false)
    public Office getOffice() {
        return this.office;
    }
    
    public void setOffice(Office office) {
        this.office = office;
    }
    
    @Column(name="lastName", nullable=false, length=50)
    public String getLastName() {
        return this.lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    @Column(name="firstName", nullable=false, length=50)
    public String getFirstName() {
        return this.firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    @Column(name="extension", nullable=false, length=10)
    public String getExtension() {
        return this.extension;
    }
    
    public void setExtension(String extension) {
        this.extension = extension;
    }
    
    @Column(name="email", nullable=false, length=100)
    public String getEmail() {
        return this.email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    @Column(name="reportsTo")
    public Integer getReportsTo() {
        return this.reportsTo;
    }
    
    public void setReportsTo(Integer reportsTo) {
        this.reportsTo = reportsTo;
    }
    
    @Column(name="jobTitle", nullable=false, length=50)
    public String getJobTitle() {
        return this.jobTitle;
    }
    
    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    @JsonManagedReference
    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="employee")
    public Set<Customer> getCustomers() {
        return this.customers;
    }

    public void setCustomers(Set<Customer> customers) {
        this.customers = customers;
    }
}
