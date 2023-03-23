package com.stefanie.trend.web;

import com.stefanie.trend.pojo.Index;
import com.stefanie.trend.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class IndexCodeController {
    @Autowired
    IndexService indexService;
    @RequestMapping(method = RequestMethod.GET,value = "/codes")
    public List<Index> Code(){
        return indexService.get();
    }
}
