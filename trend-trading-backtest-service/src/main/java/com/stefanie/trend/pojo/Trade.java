package com.stefanie.trend.pojo;

import lombok.Data;

@Data
public class Trade {
    String buyDate;
    String sellDate;
    float buyClosePoint;
    float sellClosePoint;
    float rate;
}
