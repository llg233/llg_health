package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.pojo.Menu;
import com.itheima.health.pojo.Role;
import com.itheima.health.pojo.User;

import java.util.ArrayList;
import java.util.List;

public interface UserService {

    User findUserByUsername(String username);

    void add(User user, Integer[] roleIds);

    PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString);

    void delete(Integer id);

    User findById(Integer id);

    List<Integer> findRoleIdsByUserId(Integer id);

    void edit(User user, Integer[] roleIds);

    List<Role> findAll();

    ArrayList<Menu> findUserMenu(String username);
}
