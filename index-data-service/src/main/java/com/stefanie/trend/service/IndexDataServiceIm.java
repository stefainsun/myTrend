package com.stefanie.trend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stefanie.trend.pojo.IndexData;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@CacheConfig(cacheNames = "my_trend")
public class IndexDataServiceIm implements IndexDataService{

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    ObjectMapper objectMapper;
    @Override
//    @Cacheable(key = "#p0")
    public List<IndexData> getData(String code) {
        List<IndexData> indexDates = new ArrayList<>();

        Set<ZSetOperations.TypedTuple<Object>> objectSet  = redisTemplate.opsForZSet().rangeWithScores("my_trend::"+code, 0, -1);
        IndexData indexData = null;
        for(Object object:objectSet) {
            try {
                indexData = objectMapper.readValue(((DefaultTypedTuple) object).getValue().toString(), IndexData.class);
                indexDates.add(indexData);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
        return indexDates;
    }
}
