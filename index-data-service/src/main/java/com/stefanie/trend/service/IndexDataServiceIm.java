package com.stefanie.trend.service;

import com.stefanie.trend.pojo.IndexData;
import lombok.Data;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames = "index_datas")
public class IndexDataServiceIm implements IndexDataService{


    @Override
    @Cacheable(key = "'indexData-code-'+#p0")
    public List<IndexData> getData(String code) {
        return null;
    }
}
