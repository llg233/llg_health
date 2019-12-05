package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.dao.PermissionDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.pojo.Permission;
import com.itheima.health.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


@Service(interfaceClass = PermissionService.class)
@Transactional
public class PermissionServiceImpl implements PermissionService{
    @Autowired
    private PermissionDao permissionDao;

    @Override
    public void add(Permission authorization) {
        permissionDao.add(authorization);
    }

    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage,pageSize);
        Page<Permission> page = permissionDao.selectByCondition(queryString);

        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public void delete(int id) throws RuntimeException{
        long count = permissionDao.findCountByPermissionId(id);
        if (count>0){
            throw new RuntimeException("当前权限被角色引用，不能删除");
        }

        permissionDao.delete(id);
    }

    @Override
    public void edit(Permission authorization) {
        permissionDao.edit(authorization);
    }

    @Override
    public Permission findById(int id) {
        return permissionDao.findById(id);
    }

}
