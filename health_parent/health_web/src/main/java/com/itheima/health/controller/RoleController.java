package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Menu;
import com.itheima.health.pojo.Permission;
import com.itheima.health.pojo.Role;
import com.itheima.health.service.RoleService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/role")
public class RoleController {

    @Reference
    private RoleService roleService;

    //分页查询
    @RequestMapping(value = "/findpage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        //查找页数据
        return roleService.findPage(queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize(),
                queryPageBean.getQueryString());
    }

    //查询所有权限
    @RequestMapping(value = "findPermission")
    public Result findPermission() {
        List<Permission> list= roleService.findPermission();
        return new Result(true,"查找成功" ,list);
    }

    //查询所有菜单
    @RequestMapping(value = "findMenu")
    public Result findMenu() {
        List<Menu> list= roleService.findMenu();
        return new Result(true,"查找成功" ,list);
    }

    //增加
    @RequestMapping(value = "/add")
    public Result add(@RequestBody Role role, Integer[] permissionIds,Integer[] menuIds) {
        try {
            //存储role,以及中间表
            roleService.add(role, permissionIds,menuIds);
            return new Result(true, "添加角色成功");
        } catch (RuntimeException e) {
            return new Result(false, e.getMessage());
        } catch (Exception e) {
            return new Result(false, "添加角色失败");
        }
    }

    //删除
    @RequestMapping(value = "/delete")
    public Result delete(int id){
        try {
            roleService.delete(id);
            return new Result(true,"删除角色成功");
        } catch (Exception e) {
            return new Result(false,"删除角色失败");
        }
    }

    //根据id查找
    @RequestMapping(value = "/findById")
    public Map<String,Object> findById(int id){
        return roleService.findById(id);
    }
    //编辑
    @RequestMapping(value = "/edit")
    public Result edit(@RequestBody Role role, Integer[] permissionIds,Integer[] menuIds){
        try {
            roleService.edit(role,permissionIds,menuIds);
            return new Result(true,"编辑成功");
        } catch (RuntimeException e) {
            return new Result(false,e.getMessage());
        } catch (Exception e2) {
            return new Result(false,"编辑失败");
        }
    }
}
