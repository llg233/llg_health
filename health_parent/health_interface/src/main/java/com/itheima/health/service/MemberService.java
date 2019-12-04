package com.itheima.health.service;

import com.itheima.health.pojo.Member;

import java.util.List;
import java.util.Map;

public interface MemberService {

    Member findMemberByTelephone(String telephone);

    void add(Member member);

    /**
     * 统计每个月的注册会员数量
     * @param months
     * @return
     */
    List<Integer> findMemberNumByMonth(List<String> months);

    /**
     * 统计男女会员占比饼形图
     * @return
     */
    List<Map<String, Object>> findMemberNumBySex();

    /**
     * 统计会员年龄段占比饼形图
     * @return
     */
    List<Map<String, Object>> findMemberNumByAge();
}
