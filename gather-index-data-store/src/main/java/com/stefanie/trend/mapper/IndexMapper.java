package com.stefanie.trend.mapper;

import com.stefanie.trend.pojo.Index;
import com.stefanie.trend.pojo.IndexData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface IndexMapper {
    @Select("select date,closePoint from ${code}")
    public List<IndexData> findAllIndexData (@Param(value = "code") String code);
    @Select("select code,name from index_codes")
    public List<Index> findAllIndexCode ();
}
