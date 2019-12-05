package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.dao.UserDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.pojo.Menu;
import com.itheima.health.pojo.Role;
import com.itheima.health.pojo.User;
import com.itheima.health.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName CheckItemServiceImpl
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2019/11/19 15:48
 * @Version V1.0
 */
@Service(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    @Override
    public User findUserByUsername(String username) {
        User user = userDao.findUserByUsername(username);
        return user;
    }

    //新增
    @Override
    public void add(User user, Integer[] roleIds) {
        userDao.add(user);

        setUserAndRole(user.getId(),roleIds);
    }
    private void setUserAndRole(Integer userId, Integer[] roleIds) {
        if(roleIds != null && roleIds.length > 0){
            for (Integer roleId : roleIds) {
                Map<String,Integer> map = new HashMap<String,Integer>();
                map.put("userid",userId);
                map.put("roleid",roleId);
                userDao.setUserAndRole(map);
            }
        }
    }

    //分页查询
    @Override
    public PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage,pageSize);
        Page<User> page = userDao.selectByCondition(queryString);
        PageResult pageResult = new PageResult(page.getTotal(),page.getResult());
        return pageResult;
    }

    //删除
    @Override
    public void delete(Integer id) {
        //查询当前用户是否和角色关联
        long count = userDao.findCountByRoleId(id);
        if (count >0){
            //当前用户被引用,不能删除
            throw new RuntimeException("当前用户信息被角色信息引用,不能删除");
        }
        userDao.deleteById(id);
    }

    @Override
    public User findById(Integer id) {
        return userDao.findById(id);
    }

    @Override
    public List<Integer> findRoleIdsByUserId(Integer id) {
        return userDao.findRoleIdsByUserId(id);
    }

    //编辑用户,同时需要更新和角色的关联关系
    @Override
    public void edit(User user, Integer[] roleIds) {
        //根据用户ID删除中间表数据
        userDao.deleteAssociation(user.getId());
        setUserAndRole(user.getId(),roleIds);
        //更新用户基本信息
        userDao.edit(user);
    }

    @Override
    public List<Role> findAll() {
        return userDao.findAll();
    }

    @Override
    public ArrayList<Menu> findUserMenu(String username) {
        List<Menu> userMenu = userDao.findUserMenu(username);
        ArrayList<Menu> menus = new ArrayList<>();
        //查询出没有父级菜单的菜单
        for (Menu menu : userMenu) {
            if (menu.getParentMenuId()==null) {
                menus.add(menu);
            }
        }
        //遍历menus中的所有菜单
        for (Menu menu : menus) {
            ArrayList<Menu> menus1 = new ArrayList<>();
            Integer id = menu.getId();
            //再次遍历用户所有菜单
            for (Menu userMenu1 : userMenu) {
                Integer parentMenuId = userMenu1.getParentMenuId();
                if (parentMenuId!=null) {
                    //判断是否该id为父菜单的id
                    if (id.equals(parentMenuId)) {
                        //添加到改菜单中的子菜单
                        menus1.add(userMenu1);
                    }
                }
            }
            menu.setChildren(menus1);
        }
        return menus;
    }
}
