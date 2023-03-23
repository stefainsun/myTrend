package com.stefanie.trend.web;

import com.stefanie.trend.pojo.IndexData;
import com.stefanie.trend.service.IndexDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class IndexDataController {
    @Autowired
    IndexDataService indexDataService;
    @RequestMapping(value = "/getData",method = RequestMethod.GET)
    public List<IndexData> getData(@RequestParam("code") String code){
        return indexDataService.getData(code);

    }
}
