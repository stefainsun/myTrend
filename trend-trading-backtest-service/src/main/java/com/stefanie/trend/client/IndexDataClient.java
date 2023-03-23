package com.stefanie.trend.client;

import com.stefanie.trend.pojo.IndexData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
@FeignClient(value = "INDEX-DATA-SERVICE")
public interface IndexDataClient {
    @RequestMapping(method = RequestMethod.GET,value = "/getData")
    public List<IndexData> getIndexData(@RequestParam("code") String code);
}

