package com.stefanie.trend.pojo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AnnualProfit {
    int year;
    BigDecimal indexIncome;
    BigDecimal trendIncome;
}
