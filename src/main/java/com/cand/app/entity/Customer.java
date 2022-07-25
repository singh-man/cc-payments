package com.cand.app.entity;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class Customer {

    public static final String UNKNOWN = "UNKNOWN";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NonNull
    private String fullName;

    @OneToMany(cascade = javax.persistence.CascadeType.ALL, mappedBy = "customer")
    @SerializedName("banks") // for GSON
    private Set<Bank> accounts;

    public Customer(String fullName, Bank... banks) {
        this(fullName);
        this.accounts = new HashSet<>(Arrays.asList(banks));
    }

}
