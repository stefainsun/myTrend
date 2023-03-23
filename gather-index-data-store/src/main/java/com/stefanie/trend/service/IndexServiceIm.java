package com.stefanie.trend.service;

import cn.hutool.core.collection.CollUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.stefanie.trend.pojo.Index;
import com.stefanie.trend.util.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@CacheConfig(cacheNames = "indexes")
public class IndexServiceIm implements IndexService{
    private List<Index> indexes;
    @Autowired
    RestTemplate restTemplate;
    @Override
    @Cacheable(key = "'all_codes'")
    public List<Index> get() {
//        CollUtil.toList();
        return CollUtil.toList();
    }

    @Override
    @Cacheable(key = "'all_codes'")
    public List<Index> store() {

        return indexes;
    }

    @Override
    @CacheEvict(allEntries = true)
    public void remove() {

    }

    @Override
    @HystrixCommand(fallbackMethod = "third_part_not_connected")
    public List<Index> refresh() {
        IndexServiceIm indexServiceIm = SpringContextUtil.getBean(IndexServiceIm.class);
        indexServiceIm.remove();
        indexes = fetch_indexes_from_third_part();
        return  indexServiceIm.store();
    }


    public List<Index> fetch_indexes_from_third_part(){
        List<Map> temp = restTemplate.getForObject("http://localhost:8090/indexes2/codes.json",List.class);
        return map2List(temp);

    }
    public List<Index> map2List(List<Map> temp){
        List<Index> indexes = new ArrayList<>();
        for(Map map:temp){
            String code = map.get("code").toString();
            String name = map.get("name").toString();
            Index index = new Index(code,name);
            indexes.add(index);
        }
        return indexes;
    }
    public List<Index> third_part_not_connected(){
        Index index= new Index("000","无效代码");
        return CollUtil.toList(index);
    }
}
