package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.dao.CheckItemDao;
import com.itheima.health.dao.MenuDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.pojo.Menu;
import com.itheima.health.service.CheckItemService;
import com.itheima.health.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass = MenuService.class)
@Transactional
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuDao menuDao;

    //新增检查项
    @Override
    public void add(Menu menu) {
        menuDao.add(menu);
    }

    //分页查询
    @Override
    public PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage,pageSize);
        Page<Menu> page = menuDao.selectByCondition(queryString);
        return new PageResult(page.getTotal(),page.getResult());
    }

    //删除检查项
    @Override
    public void delete(Integer id) {
       long count  = menuDao.findCountByCheckItemId(id);
       if (count>0){
           //当前检查项被引用，不能删除
           throw new RuntimeException("当前子菜单项被引用，不能删除");
       }
        menuDao.deleteById(id);
    }

    //编辑前根据id查询
    @Override
    public Menu findById(Integer id) {
        Menu menu = menuDao.findById(id);
        return menu;
    }

    //编辑更新
    @Override
    public void edit(Menu menu) {
        menuDao.edit(menu);
    }

}
