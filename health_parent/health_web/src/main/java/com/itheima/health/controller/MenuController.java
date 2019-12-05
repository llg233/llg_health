package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.pojo.Menu;
import com.itheima.health.service.CheckItemService;
import com.itheima.health.service.MenuService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.awt.*;
import java.util.List;

@RestController
@RequestMapping("/menu")
public class MenuController {

    @Reference
    private MenuService menuService;


    //新增检查项
    @RequestMapping("/add")
    public Result add(@RequestBody Menu menu){
        try {
            menuService.add(menu);
        } catch (Exception e) {
            return new Result(false,MessageConstant.ADD_MENU_FAIL);
        }
        return new Result(true, MessageConstant.ADD_MENU_SUCCESS);
    }

    //分页查询
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult = menuService.pageQuery(queryPageBean.getCurrentPage(),queryPageBean.getPageSize(),queryPageBean.getQueryString());
        return pageResult;
    }

    //删除检查项
    @RequestMapping("/delete")
    public Result delete(Integer id){
        try {
            menuService.delete(id);
        } catch (RuntimeException e) {
            return new Result(false,e.getMessage());
        }catch (Exception e){
            return new Result(false,MessageConstant.DELETE_MENU_FAIL);
        }
        return new Result(true,MessageConstant.DELETE_MENU_SUCCESS);
    }

    //编辑前根据id查询
    @RequestMapping("/findById")
    public Result findById(Integer id){
        try {
            Menu menu = menuService.findById(id);
            return new Result(true,MessageConstant.QUERY_MENU_SUCCESS,menu);
        } catch (Exception e) {
            return new Result(false,MessageConstant.QUERY_MENU_FAIL);

        }
    }

    //编辑更新
    @RequestMapping("/edit")
    public Result edit(@RequestBody Menu menu){
        try {
            menuService.edit(menu);
        } catch (Exception e) {
            return new Result(false,MessageConstant.EDIT_MENU_DAIL);
        }
        return new Result(true,MessageConstant.EDIT_MENU_SUCCESS);

    }
}


