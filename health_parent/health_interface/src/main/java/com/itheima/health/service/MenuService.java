package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.pojo.Menu;

import java.util.List;

public interface MenuService {

    //分页查询
    PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString);

    void add(Menu menu);

    Menu findById(Integer id);

    void edit(Menu menu);

    void delete(Integer id);
}
