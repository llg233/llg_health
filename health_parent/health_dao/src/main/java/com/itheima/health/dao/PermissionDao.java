package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.Permission;

import java.util.List;
import java.util.Set;

public interface PermissionDao {

    Set<Permission> findPermissionsByRoleId(Integer roleId);

    Page<Permission> selectByCondition(String queryString);

    void add(Permission permission);

    void edit(Permission permission);

    Permission findById(int id);

    long findCountByPermissionId(int id);

    void delete(int id);

    //查找所有权限
    List<Permission> findAll();

}
