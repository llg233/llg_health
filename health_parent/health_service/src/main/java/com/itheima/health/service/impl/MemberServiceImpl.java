package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.MemberDao;
import com.itheima.health.pojo.Member;
import com.itheima.health.service.MemberService;
import com.itheima.health.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {

    @Autowired
    MemberDao memberDao;

    @Override
    public Member findMemberByTelephone(String telephone) {
        return memberDao.findMemberByTelephone(telephone);
    }

    @Override
    public void add(Member member) {
        // 如果会员传递的密码不为空（123->数据库）
        if(member.getPassword()!=null && !member.getPassword().equals("")){
            member.setPassword(MD5Utils.md5(member.getPassword()));
        }
        memberDao.add(member);
    }

    @Override
    public List<Integer> findMemberNumByMonth(List<String> months) {
        ArrayList<Integer> arrayList = new ArrayList<>();
        for (String month : months) {
            String beginMonth = month + "-1";
            String endMonth = month + "-31";
            Integer count = memberDao.findMemberNumBetweenDate(beginMonth,endMonth);
            arrayList.add(count);
        }
        return arrayList;
    }

    @Override
    public List<Map<String, Object>> findMemberNumBySex() {
        return memberDao.findMemberNumBySex();
    }

    @Override
    public List<Map<String, Object>> findMemberNumByAge() {
        return memberDao.findMemberNumByAge();
    }
}
