package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.List;

/**
 * @ClassName SetmealMobileController
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2019/11/25 15:40
 * @Version V1.0
 */
@RestController
@RequestMapping(value = "/setmeal")
public class SetmealMobileController {

    @Reference
    SetmealService setmealService;

    @Autowired
    JedisPool jedisPool;

    // 查询所有的套餐
    @RequestMapping(value = "/getSetmeal")
    public Result getSetmeal() {

        //1、先从Redis里面获取数据
        String setMealList = getFromRedis(jedisPool);
        List<Setmeal> setMeals = JSON.parseArray(setMealList, Setmeal.class);

        try {
            if(setMeals == null) {
                System.out.println("Redis没有从mysql里面获取");

                //2、Redis没有从mysql里面获取
                Result result = getFromMysql();

                ObjectMapper objectMapper = new ObjectMapper();
                List<Setmeal> list = (List<Setmeal>) result.getData();
                setMeals = list;
                String data = objectMapper.writeValueAsString(list);

                //3.再存到Redis
                saveToRedis(jedisPool,data);

            } else {
                //Redis里面有数据，直接取
                System.out.println("Redis里面有数据，直接取");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Redis服务器挂了，从mysql获得");
            getFromMysql();
        }
        return new Result(true, MessageConstant.GET_SETMEAL_LIST_SUCCESS, setMeals);
    }

    //将数据存到Redis
    public void saveToRedis(JedisPool jedisPool, String data) {
        jedisPool.getResource().set("setMealList", data);
    }

    //从Redis中获取数据
    public String getFromRedis(JedisPool jedisPool) {
        String setMealList = jedisPool.getResource().get("setMealList");
        return setMealList;
    }


    //从mysql中获取数据
    public Result getFromMysql() {
        //1、调用接口的findAll()方法查询所有套餐信息，返回list集合，数据类型为Setmeal
        List<Setmeal> list = setmealService.findAll();

        //2、对查询到的Setmeal集合进行非空判断
        if(list!=null && list.size()>0){
            return new Result(true,MessageConstant.GET_SETMEAL_LIST_SUCCESS,list);
        }
        else{
            return new Result(false,MessageConstant.GET_SETMEAL_LIST_FAIL);
        }
    }


    // 使用套餐id，查询套餐id所对应的套餐信息（详情）
    @RequestMapping(value = "/findById")
    public Result findById(Integer id) throws Exception{

        //1、先从Redis里面获取数据
        String setMealDetails = getDetailsFromRedisById(jedisPool, id);
        Object setMealDetailsObject = JSON.parse(setMealDetails);

        try {
            if(setMealDetailsObject == null) {
                System.out.println("Redis没有套餐详情，从mysql里面获取");

                //2、Redis没有从mysql里面获取
                Result result = getDetailsFromMysqlById(id);

                //获取根据id拿到的setMeal对象
                Setmeal setMeal = (Setmeal) result.getData();

                //把setMeal对象转成Json字符串
                ObjectMapper objectMapper = new ObjectMapper();
                String data = objectMapper.writeValueAsString(setMeal);

                //将获取到的JSON字符串对setMealDetails进行赋值
                setMealDetails = data;
                //将JSON字符串转成JSON对象
                setMealDetailsObject = JSON.parse(setMealDetails);

                //3.再存到Redis
                saveDetailsToRedis(jedisPool, id.toString(), setMealDetails);

            } else {
                //Redis里面有数据，直接取
                System.out.println("Redis里面有套餐详情，直接取");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Redis服务器挂了，从mysql获得套餐详情");
            getDetailsFromMysqlById(id);
        }
        return new Result(true, MessageConstant.GET_SETMEAL_LIST_SUCCESS, setMealDetailsObject);
    }

    /*将数据存到Redis*/
    public void saveDetailsToRedis(JedisPool jedisPool, String id, String data) {
        jedisPool.getResource().set(id,data);
    }

    /*从Redis中获取数据*/
    private String getDetailsFromRedisById(JedisPool jedisPool, Integer id) {
        String setMealDetails = jedisPool.getResource().get(id.toString());
        return setMealDetails;
    }

    /*从mysql中获取数据*/
    public Result getDetailsFromMysqlById(Integer id) throws Exception {
        //1、根据前端传过去的套餐id，调用接口的findById()方法查询套餐详情，返回Setmeal
        Setmeal setmeal = setmealService.findById(id);

        //2、对查询到的Setmeal进行非空判断
        if(setmeal!=null){
            return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
        }
        else{
            return new Result(false,MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }

}
