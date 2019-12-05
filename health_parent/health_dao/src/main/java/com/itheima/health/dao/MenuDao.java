package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.Menu;

import java.util.List;


public interface MenuDao {


    Page<Menu> selectByCondition(String queryString);

    void add(Menu menu);

    Menu findById(Integer id);

    void edit(Menu menu);

    long findCountByCheckItemId(Integer id);

    void deleteById(Integer id);

    //查找所有菜单
    List<Menu> findAll();

    //根据roleid查找menu
    List<Menu> findMenuByRoleId();

    //根据id查找menu
    Menu findById();
}
