package com.stefanie.trend.service;

import cn.hutool.core.collection.CollUtil;
import com.stefanie.trend.pojo.IndexData;
import com.stefanie.trend.util.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@CacheConfig(cacheNames = "index_datas")
public class IndexDataServiceIm implements IndexDataService{
    Map<String,List<IndexData>> indexDatas = new HashMap<>();
    @Autowired
    SpringContextUtil springContextUtil;
    @Override
    @Cacheable(key = "'indexData-code-'+#p0")
    public List<IndexData> get(String code) {
        return CollUtil.toList();
    }

    @Override
    @Cacheable(key = "'indexData-code-'+#p0")
    public List<IndexData> store(String code) {
        return indexDatas.get(code);
    }

    @Override
    @CacheEvict(key = "'indexData-code-'+#p0")
    public void remove(String code) {

    }

    @Override
    public List<IndexData> refresh(String code) {

        IndexDataServiceIm indexDataServiceIm = springContextUtil.getBean(IndexDataServiceIm.class);
        indexDataServiceIm.remove(code);
        indexDatas.put(code,springContextUtil.fetch_indexes_from_third_part("http://localhost:8090/indexes2/"+code+".json"));
        return indexDataServiceIm.store(code);
    }
}
