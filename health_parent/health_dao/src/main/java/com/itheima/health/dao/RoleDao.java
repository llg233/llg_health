package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface RoleDao {


    //    分页查询
    Page<Role> findPage(String queryString);


    Set<Role> findRolesByUserId(Integer userId);

    //添加角色
    void add(Role role);

    //更新角色
    void update(Role role);

    //添加角色权限中间表
    void roleAddPermission(@Param("role_id") Integer id, @Param("permission_id") Integer permission);

    //添加角色菜单中间表
    void roleAddmenu(@Param("role_id")Integer id, @Param("menu_id")Integer menuId);

    //删除角色
    void delete(int id);

    //清除关联菜单的数据
    void removeMenu(int id);

    //清除关联权限的数据
    void removePremission(int id);

    //根据id查找
    Role findById(int id);

    //编辑role对象
    void edit(Role role);

    //清除中间表
    void clearPermission(Integer id);

    void clearMenu(Integer id);

    List<Integer> findParentMenu(List<Integer> menuIds);
}
