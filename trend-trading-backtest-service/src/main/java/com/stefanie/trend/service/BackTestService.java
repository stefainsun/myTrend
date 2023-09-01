package com.stefanie.trend.service;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.stefanie.trend.client.IndexDataClient;
import com.stefanie.trend.client.IndexDataClientFeignHystrix;
import com.stefanie.trend.pojo.AnnualProfit;
import com.stefanie.trend.pojo.IndexData;
import com.stefanie.trend.pojo.Profit;
import com.stefanie.trend.pojo.Trade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class BackTestService {
    @Qualifier("com.stefanie.trend.client.IndexDataClient")
    @Autowired
    IndexDataClient indexDataClient;

    public List<IndexData> listIndexData(String code){
        List<IndexData> result = indexDataClient.getIndexData(code);
//        Collections.reverse(result);

        for (IndexData indexData : result) {
            System.out.println(indexData.getDate());
        }

        return result;
    }
    //  serviceCharge服务费， ma为天数，sellRate出售阈值，buyRate购买阈值，
    public Map<String,Object> simulate(int ma, BigDecimal sellRate, BigDecimal buyRate, BigDecimal serviceCharge, List<IndexData> indexDatas){
        Map<String,Object> results = new HashMap<>();
//        float closePoint,avg,max,decrease_rate,initCrash=1000,cash = initCrash,share = 0,value=0,init=0;
        BigDecimal increase_rate=new BigDecimal(0);
        BigDecimal closePoint=new BigDecimal(0);
        BigDecimal avg=new BigDecimal(0);
        BigDecimal max=new BigDecimal(0);
        BigDecimal decrease_rate=new BigDecimal(0);
        BigDecimal initCrash=BigDecimal.valueOf(1000);
        BigDecimal cash=initCrash;
        int share =0;
        BigDecimal value=new BigDecimal(0);
        BigDecimal init=new BigDecimal(0);
        int winCount = 0;
        BigDecimal avgWinRate = BigDecimal.valueOf(0);
        int lossCount = 0;
        BigDecimal avgLossRate = BigDecimal.valueOf(0);
        BigDecimal totalWinRate = BigDecimal.valueOf(0);
        BigDecimal totalLossRate = BigDecimal.valueOf(0);


        List<Profit> profits = new ArrayList<>();
        List<Trade> trades = new ArrayList<>();
        if(!indexDatas.isEmpty()){
            init = indexDatas.get(0).getClosePoint();
        }
        for(int i = 0;i<indexDatas.size();i++){

            IndexData indexData = indexDatas.get(i);
            closePoint = indexData.getClosePoint();
            if(i>=ma+1){
                avg = getMA(ma,i,indexDatas);
                max = getMAX(ma,i,indexDatas);
                if(closePoint.divide(avg,3, RoundingMode.HALF_UP).compareTo(buyRate)>0){
//                    share += cash/closePoint;
                    if(share==0){
                        Trade trade = new Trade();
                        trade.setBuyDate(indexData.getDate());
                        trade.setBuyClosePoint(indexData.getClosePoint());
                        share += cash.divide(closePoint,3, RoundingMode.HALF_UP).intValue();
                        trades.add(trade);
                        cash=BigDecimal.valueOf(0);
                    }

                }
                else if(closePoint.divide(max,3, RoundingMode.HALF_UP).compareTo(sellRate)<0){
                    if(share!=0){
                        Trade trade = trades.get(trades.size()-1);
                        trade.setSellDate(indexData.getDate());
                        trade.setSellClosePoint(indexData.getClosePoint());
                        cash=BigDecimal.valueOf(share).multiply(closePoint.multiply(BigDecimal.valueOf(1).subtract(serviceCharge)));
//                        cash=share*closePoint*(1-serviceCharge);
                        BigDecimal rate = cash.divide(initCrash,3, RoundingMode.HALF_UP);
                        trade.setRate(rate);
                        share=0;

                        if(trade.getSellClosePoint().compareTo(trade.getBuyClosePoint())<0){
                            lossCount++;
                            totalLossRate=totalLossRate.add(trade.getSellClosePoint().subtract(trade.getBuyClosePoint()).divide(trade.getBuyClosePoint(),3, RoundingMode.HALF_UP));
//                            totalLossRate+=(trade.getSellClosePoint()-trade.getBuyClosePoint())/trade.getBuyClosePoint();

                        }
                        else{
                            totalWinRate=totalWinRate.add(trade.getSellClosePoint().subtract(trade.getBuyClosePoint()).divide(trade.getBuyClosePoint(),3, RoundingMode.HALF_UP));

//                            totalWinRate+=(trade.getSellClosePoint()-trade.getBuyClosePoint())/trade.getBuyClosePoint();
                            winCount++;
                        }
                    }
                }
                else{

                }
            }
            if(share!=0){
                value = BigDecimal.valueOf(share).multiply(closePoint);
            }
            else{
                value = cash;
            }
            BigDecimal rate = value.divide(initCrash,3, RoundingMode.HALF_UP);
            Profit profit = new Profit();
            profit.setDate(indexData.getDate());
            profit.setValue(rate.multiply(init));
            profits.add(profit);
            System.out.println("rate="+rate+"\tvalue="+value+"\tshare="+share+"\t利润为:"+profit.getValue());
        }
        results.put("annualProfits",caculateAnnualProfits(indexDatas,profits));
        avgWinRate=totalWinRate.divide(BigDecimal.valueOf(winCount),3, RoundingMode.HALF_UP);
        avgLossRate=totalLossRate.divide(BigDecimal.valueOf(lossCount),3, RoundingMode.HALF_UP);
        results.put("years",getYear(indexDatas));
        results.put("winCount", winCount);
        results.put("lossCount", lossCount);
        results.put("avgWinRate", avgWinRate);
        results.put("avgLossRate", avgLossRate);
        results.put("profits",profits);
        results.put("trades",trades);

        return results;
    }
    public BigDecimal getMA(int ma,int i,List<IndexData> indexDatas){
        int start = i-ma-1;
        int end = i-1;
        BigDecimal sum=BigDecimal.valueOf(0);
        if(start<0){
            return BigDecimal.valueOf(0);
        }
        else{
            for(int j=start;j<end;j++){
                sum = sum.add(indexDatas.get(j).getClosePoint());
//                sum += indexDatas.get(j).getClosePoint();
            }
        }
        return sum.divide(BigDecimal.valueOf(ma),3, RoundingMode.HALF_UP);

    }
    public BigDecimal getMAX(int ma,int i,List<IndexData> indexDatas){
        int start = i-ma-1;
        int end = i-1;
        BigDecimal max = BigDecimal.valueOf(0),closePoint=BigDecimal.valueOf(0);
        if(start<0){
            return BigDecimal.valueOf(0);
        }
        for(int j=start;j<end;j++){
            closePoint = indexDatas.get(j).getClosePoint();
            if(max.compareTo(closePoint)<0){
                max = closePoint;
            }
        }
        return max;
    }
    private float getYear(List<IndexData> indexDatas){
        Date startDate = DateUtil.parse(indexDatas.get(0).getDate());
        Date endDate = DateUtil.parse(indexDatas.get(indexDatas.size()-1).getDate());
        float days = DateUtil.between(startDate,endDate, DateUnit.DAY);
        return days/365f;
    }
    private int getYear(String date){
        return Convert.toInt(StrUtil.subBefore(date,"-",false));
    }

    private BigDecimal getIndexIncome(int year, List<IndexData> indexDatas) {
        IndexData first=null,last=null;
        int year2;
        for(IndexData indexData:indexDatas){
            year2 = DateUtil.year(DateUtil.parse(indexData.getDate()));
            if(year2==year){
                if(first==null){
                    first=indexData;
                }
                last=indexData;
            }
        }
        return last.getClosePoint().subtract(first.getClosePoint()).divide(first.getClosePoint(),3, RoundingMode.HALF_UP);
//        return (last.getClosePoint()-first.getClosePoint())/first.getClosePoint();
    }
    private BigDecimal getTrendIncome(int year, List<Profit> profits){
        Profit first=null,last=null;
        int year2;
        for(Profit profit:profits){
            year2 = DateUtil.year(DateUtil.parse(profit.getDate()));
            if(year2==year){
                if(first==null){
                    first=profit;
                }
                last=profit;
            }
        }
        System.out.println(first.getValue());
//        if(first.getValue().doubleValue()==0){
//            return BigDecimal.valueOf(0);
//        }
        return last.getValue().subtract(first.getValue()).divide(first.getValue(),3, RoundingMode.HALF_UP);

//        return (last.getValue()-first.getValue())/first.getValue();
    }
    private List<AnnualProfit> caculateAnnualProfits(List<IndexData> indexDatas, List<Profit> profits) {
        int firstYear = getYear(indexDatas.get(0).getDate());
        int lastYear = getYear(indexDatas.get(indexDatas.size()-1).getDate());
        List<AnnualProfit> annualProfits = new ArrayList<>();
        for(int year=firstYear;year<=lastYear;year++){
            AnnualProfit annualProfit = new AnnualProfit();
            annualProfit.setIndexIncome(getIndexIncome(year,indexDatas));
            annualProfit.setTrendIncome(getTrendIncome(year,profits));
            annualProfit.setYear(year);
            annualProfits.add(annualProfit);
        }
        return annualProfits;
    }

}