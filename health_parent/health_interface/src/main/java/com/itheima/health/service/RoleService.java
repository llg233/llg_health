package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.pojo.Menu;
import com.itheima.health.pojo.Permission;
import com.itheima.health.pojo.Role;

import java.util.HashMap;
import java.util.List;

public interface RoleService {

    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);

    List<Permission> findPermission();

    List<Menu> findMenu();

    //添加角色
    void add(Role role, Integer[] permissionIds, Integer[] menuIds);

    void delete(int id);

    //根据id查找
    HashMap<String, Object> findById(int id);

    void edit(Role role, Integer[] permissionIds, Integer[] menuIds);
}
