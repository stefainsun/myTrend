package com.stefanie.trend.util;
  
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import com.stefanie.trend.pojo.Index;
import com.stefanie.trend.pojo.IndexData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class SpringContextUtil implements ApplicationContextAware {

    private SpringContextUtil() {
         System.out.println("SpringContextUtil()");
    }
      
    private static ApplicationContext applicationContext;
      
    @Override
    public void setApplicationContext(ApplicationContext applicationContext){
        System.out.println("applicationContext:"+applicationContext);
        SpringContextUtil.applicationContext = applicationContext;
    }
      
    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }
    @Autowired
    RestTemplate restTemplate;
//    public List<IndexData> fetch_indexes_from_third_part(String url){
//        List<Map> temp = restTemplate.getForObject(url,List.class);
//        return map2List(temp);
//
//    }
//    public List<IndexData> map2List(List<Map> temp){
//        List<IndexData> indexDatas = new ArrayList<>();
//        for(Map map:temp){
//            String code = map.get("date").toString();
//            float name =  Convert.toFloat(map.get("closePoint"));
//            IndexData indexData = new IndexData(code,name);
//            indexDatas.add(indexData);
//        }
//        return indexDatas;
//    }
//    public List<Index> third_part_not_connected(){
//        Index index= new Index("000","无效代码");
//        return CollUtil.toList(index);
//    }
}