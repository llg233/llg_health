package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Role;
import com.itheima.health.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName UserController
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2019/11/19 15:50
 * @Version V1.0
 */
@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Reference
    UserService userService;

    // 从SpringSecurity中获取用户信息
    @RequestMapping(value = "/getUsername")
    public Result getUsername(){
        try {
            User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            // 使用登录名，查询当前登录名对应用户信息
            //com.itheima.health.pojo.User user2 = userService.findUserByUsername(user.getUsername());

            return new Result(true, MessageConstant.GET_USERNAME_SUCCESS,user.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_USERNAME_FAIL);
        }
    }

    //增加
    @RequestMapping("/add")
    public Result add(@RequestBody com.itheima.health.pojo.User user, Integer[] roleIds){
        try {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String encodePassword = encoder.encode(user.getPassword());
            user.setPassword(encodePassword);
            userService.add(user,roleIds);
        }catch (Exception e){
            //新增失败
            return new Result(false, MessageConstant.ADD_USER_FAIL);
        }
        //新增成功
        return new Result(true,MessageConstant.ADD_USER_SUCCESS);
    }



    //分页查询
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) throws Exception {
        PageResult pageResult = userService.pageQuery(
                queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize(),
                queryPageBean.getQueryString()
        );
        return pageResult;
    }

    //删除
    @RequestMapping("/delete")
    public Result delete(Integer id){
        try {
            userService.delete(id);
        } catch (Exception e) {
            return new Result(false,MessageConstant.DELETE_USER_FAIL);
        }
        return new Result(true,MessageConstant.DELETE_USER_SUCCESS);
    }

    //根据ID查询
    @RequestMapping("/findById")
    public Result findById(Integer id){
        com.itheima.health.pojo.User user = userService.findById(id);
        if (user!=null){
            return new Result(true,MessageConstant.QUERY_USER_SUCCESS,user);
        }
        return new Result(false,MessageConstant.QUERY_USER_FAIL);
    }
    //根据用户ID查询对应的所有角色ID
    @RequestMapping("/findRoleIdsByUserId")
    public List<Integer> findRoleIdsByUserId(Integer id){
        List<Integer> list = userService.findRoleIdsByUserId(id);
        return list;
    }

    //编辑保存
    @RequestMapping("/edit")
    public Result edit(@RequestBody com.itheima.health.pojo.User user, Integer[] roleIds){
        try {
            userService.edit(user,roleIds);
        }catch (Exception e){
            return new Result(false,MessageConstant.EDIT_USER_FAIL);
        }
        return new Result(true,MessageConstant.EDIT_USER_SUCCESS);
    }

    //查询所有
    @RequestMapping("/findAll")
    public Result findAll(){
        List<Role> roleList =  userService.findAll();
        if (roleList != null && roleList.size()>0){
            Result result = new Result(true,MessageConstant.QUERY_USER_SUCCESS,roleList);
            return result;
        }
        return new Result(false,MessageConstant.QUERY_USER_FAIL);
    }
}
