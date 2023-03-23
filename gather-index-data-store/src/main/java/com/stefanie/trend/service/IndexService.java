package com.stefanie.trend.service;

import com.stefanie.trend.pojo.Index;

import java.util.List;

public interface IndexService {
    public List<Index> get();
    public List<Index> store();
    public void remove();
    public List<Index> refresh();
}
