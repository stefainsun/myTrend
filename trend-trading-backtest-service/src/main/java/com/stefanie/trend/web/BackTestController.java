package com.stefanie.trend.web;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.stefanie.trend.pojo.IndexData;
import com.stefanie.trend.pojo.Profit;
import com.stefanie.trend.pojo.Trade;
import com.stefanie.trend.service.BackTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@RestController
public class BackTestController {
    @Autowired
    BackTestService backTestService;
//    float years,indexIncomeTotal,indexIncomeAnnual,trendIncomeTotal,trendIncomeAnnual;
    BigDecimal years=new BigDecimal(0);
    BigDecimal indexIncomeTotal=new BigDecimal(0);
    float indexIncomeAnnual;
    float trendIncomeTotal;
    float trendIncomeAnnual;
    @GetMapping("simulate")
    @CrossOrigin
    public Map<String,Object> backTest(@RequestParam String code,@RequestParam String getStartDate,@RequestParam String getEndDate,@RequestParam BigDecimal serviceCharge,
                                       @RequestParam int ma,@RequestParam BigDecimal buyThreshold,@RequestParam BigDecimal sellThreshold) throws Exception {
        Map<String,Object> result = new HashMap<>();
        List<IndexData> allIndexDatas = backTestService.listIndexData(code);
        String startDate=allIndexDatas.get(0).getDate();
        String endDate=allIndexDatas.get(allIndexDatas.size()-1).getDate();
        allIndexDatas = fliterIndexDatasByDate(allIndexDatas,getStartDate,getEndDate);
        Map<String,Object> simulate = backTestService.simulate(ma,sellThreshold,buyThreshold,serviceCharge,allIndexDatas);
//        years = (float) simulate.get("years");
        years = BigDecimal.valueOf((float)simulate.get("years"));
        List<Profit> profits=(List<Profit>)simulate.get("profits");
        List<Trade> trades = (List<Trade>)simulate.get("trades");
        indexIncomeTotal=allIndexDatas.get(allIndexDatas.size()-1).getClosePoint().subtract(allIndexDatas.get(0).getClosePoint()).divide(allIndexDatas.get(0).getClosePoint(),3, RoundingMode.HALF_UP);
//        indexIncomeTotal = (allIndexDatas.get(allIndexDatas.size()-1).getClosePoint()-allIndexDatas.get(0).getClosePoint())/ allIndexDatas.get(0).getClosePoint();
//        indexIncomeAnnual = BigDecimal.pow(indexIncomeTotal.add(BigDecimal.valueOf(1)),BigDecimal.valueOf(1).divide(years)).subtract(BigDecimal.valueOf(1));
        indexIncomeAnnual =  (float) Math.pow(indexIncomeTotal.doubleValue()+1,1/years.doubleValue())-1;
        trendIncomeTotal = (profits.get(profits.size()-1).getValue().subtract(profits.get(0).getValue())).divide(profits.get(0).getValue(),3, RoundingMode.HALF_UP).floatValue();
        trendIncomeAnnual = (float)(Math.pow(trendIncomeTotal+1,1/years.doubleValue())-1);
        result.put("annualProfits",simulate.get("annualProfits"));
        result.put("winCount", simulate.get("winCount"));
        result.put("lossCount", simulate.get("lossCount"));
        result.put("avgWinRate", simulate.get("avgWinRate"));
        result.put("avgLossRate", simulate.get("avgLossRate"));
        result.put("years",years);
        result.put("indexIncomeTotal", indexIncomeTotal);
        result.put("indexIncomeAnnual", indexIncomeAnnual);
        result.put("trendIncomeTotal", trendIncomeTotal);
        result.put("trendIncomeAnnual", trendIncomeAnnual);
        result.put("profits",profits);
        result.put("trades",trades);
        result.put("indexDatas", allIndexDatas);
        result.put("indexStartDate", startDate);
        result.put("indexEndDate", endDate);
        return result;
    }
    public List<IndexData> fliterIndexDatasByDate(List<IndexData> allIndexDatas,String getStartDate,String getEndDate){
        if(StrUtil.isBlankOrUndefined(getStartDate)||StrUtil.isBlankOrUndefined(getEndDate)){
            return allIndexDatas;
        }
        Date getStart=DateUtil.parse(getStartDate);
        Date getEnd = DateUtil.parse(getEndDate);
        List<IndexData> fliterIndexDatasByDate = new ArrayList<>();
        for(IndexData indexData:allIndexDatas){
            if(DateUtil.parse(indexData.getDate()).getTime()>=getStart.getTime()&&
                    DateUtil.parse(indexData.getDate()).getTime()<=getEnd.getTime()){
                fliterIndexDatasByDate.add(indexData);

            }
        }
        return fliterIndexDatasByDate;
    }

}
