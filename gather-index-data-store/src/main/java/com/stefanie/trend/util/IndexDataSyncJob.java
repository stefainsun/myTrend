package com.stefanie.trend.util;

import cn.hutool.core.date.DateUtil;
import com.stefanie.trend.pojo.Index;
import com.stefanie.trend.service.IndexDataService;
import com.stefanie.trend.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IndexDataSyncJob{
 
    @Autowired
    private IndexService indexService;
 
    @Autowired
    private IndexDataService indexDataService;
 
    @Scheduled(cron = "0 */1 * * * ?")
    protected void executeInternal() {
        System.out.println("定时启动：" + DateUtil.now());
        List<Index> indexes = indexService.refresh();
        for (Index index : indexes) {
            indexDataService.refresh(index.getCode());
        }
        System.out.println("定时结束：" + DateUtil.now());
    }
}