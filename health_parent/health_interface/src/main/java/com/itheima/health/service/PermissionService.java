package com.itheima.health.service;


import com.itheima.health.entity.PageResult;
import com.itheima.health.pojo.Permission;

public interface PermissionService {
    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);

    void add(Permission permission);

    void edit(Permission permission);

    Permission findById(int id);

    void delete(int id);
}
