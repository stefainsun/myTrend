package com.stefanie.trend.web;

import com.stefanie.trend.pojo.Index;
import com.stefanie.trend.pojo.IndexData;
import com.stefanie.trend.service.IndexDataService;
import com.stefanie.trend.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
public class IndexDataController {
    @Autowired
    IndexDataService indexDataService;
    @RequestMapping(method= RequestMethod.GET,value = "/getData")
    public List<IndexData> get(@RequestParam(value = "code") String code){
        System.out.println(code);
        return indexDataService.get(code);
    }
    @RequestMapping(method= RequestMethod.GET,value = "/removeData")
    public String remove(@RequestParam(value = "code") String code){

        indexDataService.remove(code);
        return "删除成功";
    }
    @RequestMapping(method= RequestMethod.GET,value = "/refreshData")
    public List<IndexData> refresh(@RequestParam(value = "code") String code){

        return indexDataService.refresh(code);
    }
}
