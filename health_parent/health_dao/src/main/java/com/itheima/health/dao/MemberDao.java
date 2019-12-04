package com.itheima.health.dao;

import com.itheima.health.pojo.Member;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface MemberDao {
    Member findMemberByTelephone(String telephone);

    void add(Member member);

    Integer findMemberNumBetweenDate(@Param("beginMonth") String beginMonth, @Param("endMonth") String endMonth);

    Integer findTodayNewMember(String today);

    Integer findTotalMember();

    Integer findThisNewMember(String monday);

    List<Map<String, Object>> findMemberNumBySex();

    List<Map<String, Object>> findMemberNumByAge();
}
