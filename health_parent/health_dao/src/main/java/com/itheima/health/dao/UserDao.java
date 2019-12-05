package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.Role;
import com.itheima.health.pojo.User;

import java.util.List;
import java.util.Map;

public interface UserDao {

    User findUserByUsername(String username);

    void add(User user);

    void setUserAndRole(Map<String, Integer> map) ;

    Page<User> selectByCondition(String queryString);

    User findById(Integer id);

    List<Integer> findRoleIdsByUserId(Integer id);

    void deleteAssociation(Integer id);

    void edit(User user);

    void deleteById(Integer id);

    long findCountByRoleId(Integer id);

    List<Role> findAll();
}
