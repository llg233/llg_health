package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Permission;
import com.itheima.health.service.PermissionService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/permission")
public class PermissionController {

    @Reference
    private PermissionService permissionService;

    @RequestMapping(value = "/add.do")
    public Result add(@RequestBody Permission permission){
        try {
            permissionService.add(permission);
        } catch (Exception e) {
            return new Result(false, MessageConstant.ADD_AUTHORIZATION_FAIL);
        }
        return new Result(true,MessageConstant.ADD_AUTHORIZATION_SUCCESS);
    }

    @RequestMapping(value = "/findpage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult = permissionService.findPage(
                queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize(),
                queryPageBean.getQueryString());

        return pageResult;
    }

    @RequestMapping(value = "/delete.do")
    public Result delete(int id){
        try {
            permissionService.delete(id);
        }catch (RuntimeException e){
            return new Result(false,e.getMessage());
        }catch (Exception e) {
            return new Result(false,MessageConstant.DELETE_CHECKITEM_FAIL);
        }
        return new Result(true,MessageConstant.DELETE_CHECKGROUP_SUCCESS);
    }

    @RequestMapping(value = "/edit.do")
    public Result edit(@RequestBody Permission authorization){
        try {
            permissionService.edit(authorization);
        } catch (Exception e) {
            return new Result(false,MessageConstant.EDIT_CHECKITEM_FAIL);
        }
        return new Result(true,MessageConstant.EDIT_CHECKITEM_SUCCESS);
    }

    @RequestMapping(value = "/findById.do")
    public Result findById(int id){
        Permission authorization = permissionService.findById(id);
        if (authorization == null){
            return new Result(false,MessageConstant.QUERY_CHECKITEM_FAIL);
        }else {
            return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,authorization);
        }
    }


}
