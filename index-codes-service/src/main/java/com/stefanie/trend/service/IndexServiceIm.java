package com.stefanie.trend.service;

import cn.hutool.core.collection.CollUtil;
import com.stefanie.trend.pojo.Index;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
@CacheConfig(cacheNames = "my_trend")
public class IndexServiceIm implements IndexService{

    @Override
    @Cacheable(key = "'all_codes'")
    public List<Index> get() {
        return CollUtil.toList();
    }
}
