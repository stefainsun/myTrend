package com.stefanie.trend.service;

import com.stefanie.trend.mapper.IndexMapper;
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
    @Autowired
    IndexMapper indexMapper;
    @Override
    @Cacheable(key = "'indexData-code-'+#p0")
    public List<IndexData> get(String code) {

        return indexMapper.findAllIndexData(code);
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
//        indexDatas.put(code,springContextUtil.fetch_indexes_from_third_part("http://localhost:8090/indexes2/"+code+".json"));
        indexDatas.put(code, indexMapper.findAllIndexData(code));
        return indexDataServiceIm.store(code);
    }
}
