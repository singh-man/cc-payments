package com.cand.app.entity;

import com.google.gson.annotations.SerializedName;

import javax.persistence.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Customer {

    public static final String UNKNOWN = "UNKNOWN";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String fullName;

    @OneToMany(cascade = javax.persistence.CascadeType.ALL, mappedBy = "customer")
    @SerializedName("banks") // for GSON
    private Set<Bank> accounts;

    public Customer() {
    }

    public Customer(String fullName) {
        this.fullName = fullName;
    }

    public Customer(String fullName, Bank... banks) {
        this(fullName);
        this.accounts = new HashSet<>(Arrays.asList(banks));
    }

    public Set<Bank> getAccounts() {
        return accounts;
    }

    public void setAccounts(Set<Bank> accounts) {
        this.accounts = accounts;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(accounts, customer.accounts) && Objects.equals(fullName, customer.fullName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accounts, fullName);
    }
}
