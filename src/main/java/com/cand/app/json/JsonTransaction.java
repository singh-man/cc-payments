package com.cand.app.json;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Represents the transaction sample1.json file
 */
public class JsonTransaction {

    public int transaction_count;
    public List<Transaction> transactions;

    static public class Transaction {
        public String id;
        @JsonProperty("to")
        public To toAccount;
        public From from;
        public Amount amount;
    }

    static public class To {
        public String routing_number;
        public String account_number;
    }

    static class From {
        public String routing_number;
        public String account_number;
    }

    static public class Amount {
        public float amount;
        public String currency;
    }
}

