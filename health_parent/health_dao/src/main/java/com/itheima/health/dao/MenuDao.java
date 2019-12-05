package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.pojo.Menu;

import java.util.List;


public interface MenuDao {


    Page<Menu> selectByCondition(String queryString);

    void add(Menu menu);

    Menu findById(Integer id);

    void edit(Menu menu);

    long findCountByCheckItemId(Integer id);

    void deleteById(Integer id);
}
