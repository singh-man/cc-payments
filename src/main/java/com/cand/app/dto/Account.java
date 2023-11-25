package com.cand.app.dto;

import java.math.BigDecimal;

public record Account(String routingNo, String accountNo, BigDecimal balance){
}
