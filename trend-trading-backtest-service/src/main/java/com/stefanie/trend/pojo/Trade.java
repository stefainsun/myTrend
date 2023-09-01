package com.stefanie.trend.pojo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Trade {
    String buyDate;
    String sellDate;
    BigDecimal buyClosePoint;
    BigDecimal sellClosePoint;
    BigDecimal rate;
}
