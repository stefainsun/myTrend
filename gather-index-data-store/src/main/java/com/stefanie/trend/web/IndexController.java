package com.stefanie.trend.web;

import com.stefanie.trend.pojo.Index;
import com.stefanie.trend.service.IndexService;
import com.stefanie.trend.service.IndexServiceIm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class IndexController {
    @Autowired
    IndexService indexService;
    @RequestMapping(method= RequestMethod.GET,value = "/getCode")
    public List<Index> get(){

        return indexService.get();
    }
    @RequestMapping(method= RequestMethod.GET,value = "/remove")
    public String remove(){

        indexService.remove();
        return "删除成功";
    }
    @RequestMapping(method= RequestMethod.GET,value = "/refresh")
    public List<Index> refresh(){

        return indexService.refresh();
    }
}
