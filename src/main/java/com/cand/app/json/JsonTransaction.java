package com.cand.app.json;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Represents the transaction sample1.json file
 */
public class JsonTransaction {

    public int transaction_count;
    public List<Transaction> transactions;

    public class Transaction {
        public String id;
        @SerializedName("to")
        public To myto;
        public From from;
        public Amount amount;
    }

    public class To {
        public String routing_number;
        public String account_number;
    }

    class From {
        public String routing_number;
        public String account_number;
    }

    public class Amount {
        public float amount;
        public String currency;
    }
}

