package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.service.MemberService;
import com.itheima.health.service.ReportService;
import com.itheima.health.service.SetmealService;
import com.itheima.health.utils.DateUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName CheckItemController
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2019/11/19 15:50
 * @Version V1.0
 */
@RestController
@RequestMapping(value = "/report")
public class ReportController {

    @Reference
    MemberService memberService;

    @Reference
    SetmealService setmealService;

    @Reference
    ReportService reportService;

    /**
     * 统计每个月的注册会员数量折线图
     *
     * @return
     */
    @RequestMapping("/getMemberReport")
    public Result getMemberReport(@RequestParam(required = true) String[] dates) {
        try {
            //获取两个日期之间的所有月份
            List<String> months = DateUtils.getMonthBetween(dates[0], dates[1]);

            //根据月份获取每月的会员数量
            List<Integer> memberNum = memberService.findMemberNumByMonth(months);

            HashMap<String, Object> map = new HashMap<>();
            map.put("months", months);

            map.put("memberNum", memberNum);
            return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
        }

    }

    /**
     * 统计男女会员占比饼形图
     *
     * @return
     */
    @RequestMapping("/getMemberBySex")
    public Result getMemberBySex() {
        List<Map<String, Object>> list = memberService.findMemberNumBySex();

        HashMap<String, Object> map = new HashMap<>();
        map.put("sexNum", list);

        ArrayList<String> sexNames = new ArrayList<>();
        for (Map<String, Object> m : list) {
            String name = (String) m.get("name");
            sexNames.add(name);
        }
        map.put("sexNames", sexNames);

        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS, map);
    }

    /**
     * 统计会员年龄段占比饼形图
     *
     * @return
     */
    @RequestMapping("/getMemberByAge")
    public Result getMemberByAge() {
        List<Map<String, Object>> list = memberService.findMemberNumByAge();

        HashMap<String, Object> map = new HashMap<>();
        map.put("ageNum", list);

        ArrayList<String> ages = new ArrayList<>();
        for (Map<String, Object> m : list) {
            String name = (String) m.get("name");
            ages.add(name);
        }
        map.put("ages", ages);

        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS, map);
    }


    // 套餐预约占比（饼图）
    @RequestMapping(value = "/getSetmealReport")
    public Result getSetmealReport(){
        try {
            // 构造数据1
            List<String> setmealNames = new ArrayList<>();
            // 构造数据2
            List<Map> setmealCount = setmealService.getSetmealReport();
            if(setmealCount!=null && setmealCount.size()>0){
                for (Map map : setmealCount) {
                    String name = (String)map.get("name");
                    setmealNames.add(name);
                }
            }
            // 响应的数据
            Map map = new HashMap();
            map.put("setmealNames",setmealNames);
            map.put("setmealCount",setmealCount);
            return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL);
        }
    }

    // 运营数据统计
    @RequestMapping(value = "/getBusinessReportData")
    public Result getBusinessReportData(){
        try {
            // 调用Service
            Map<String,Object> map = reportService.findBusinessReportData();
            return new Result(true, MessageConstant.GET_BUSINESS_REPORT_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }

    // 运营数据统计（excel报表）
    @RequestMapping(value = "/exportBusinessReport")
    public Result exportBusinessReport(HttpServletRequest request, HttpServletResponse response){
        try {
            // 1：调用Service，获取在excel中填充的数据
            Map<String,Object> map = reportService.findBusinessReportData();
            String reportDate = (String)map.get("reportDate");
            Integer todayNewMember = (Integer)map.get("todayNewMember");
            Integer totalMember = (Integer)map.get("totalMember");
            Integer thisWeekNewMember = (Integer)map.get("thisWeekNewMember");
            Integer thisMonthNewMember = (Integer)map.get("thisMonthNewMember");
            Integer todayOrderNumber = (Integer)map.get("todayOrderNumber");
            Integer todayVisitsNumber = (Integer)map.get("todayVisitsNumber");
            Integer thisWeekOrderNumber = (Integer)map.get("thisWeekOrderNumber");
            Integer thisWeekVisitsNumber = (Integer)map.get("thisWeekVisitsNumber");
            Integer thisMonthOrderNumber = (Integer)map.get("thisMonthOrderNumber");
            Integer thisMonthVisitsNumber = (Integer)map.get("thisMonthVisitsNumber");
            List<Map> hotSetmeal = (List<Map>)map.get("hotSetmeal");
            //2：获取ecxel模板的位置，把数据填充到Excel的模板中
            String realpath = request.getSession().getServletContext().getRealPath("template") + File.separator + "report_template.xlsx";
            // 获取WorkBook
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(new File(realpath)));
            // 获取操作Sheet（第1个）
            XSSFSheet sheet = workbook.getSheetAt(0);
            XSSFRow row = sheet.getRow(2);
            row.getCell(5).setCellValue(reportDate);

            row = sheet.getRow(4);
            row.getCell(5).setCellValue(todayNewMember);
            row.getCell(7).setCellValue(totalMember);

            row = sheet.getRow(5);
            row.getCell(5).setCellValue(thisWeekNewMember);
            row.getCell(7).setCellValue(thisMonthNewMember);
            // 复制（笔记）
            row = sheet.getRow(7);
            row.getCell(5).setCellValue(todayOrderNumber);//今日预约数
            row.getCell(7).setCellValue(todayVisitsNumber);//今日到诊数

            row = sheet.getRow(8);
            row.getCell(5).setCellValue(thisWeekOrderNumber);//本周预约数
            row.getCell(7).setCellValue(thisWeekVisitsNumber);//本周到诊数

            row = sheet.getRow(9);
            row.getCell(5).setCellValue(thisMonthOrderNumber);//本月预约数
            row.getCell(7).setCellValue(thisMonthVisitsNumber);//本月到诊数

            int rowNum = 12;
            for(Map map1 : hotSetmeal){//热门套餐
                String name = (String) map1.get("name");
                Long setmeal_count = (Long) map1.get("setmeal_count");
                BigDecimal proportion = (BigDecimal) map1.get("proportion");
                row = sheet.getRow(rowNum ++);
                row.getCell(4).setCellValue(name);//套餐名称
                row.getCell(5).setCellValue(setmeal_count);//预约数量
                row.getCell(6).setCellValue(proportion.doubleValue());//占比
            }
            // 3：将Excel的文件使用IO的形式完成输出
            ServletOutputStream out = response.getOutputStream();
            // 下载（注意2点）
            // 注意1：定义下载的文件类型
            response.setContentType("application/vnd.ms-excel");
            // 注意2：定义下载头部信息，指定附件名
            // inline：直接在浏览器中查看
            // attachment;filename=report79.xlsx：指定附件名，将附件下载到本地
            response.setHeader("Content-Disposition","attachment;filename=report79.xlsx");

            workbook.write(out);
            out.flush(); // 刷出缓冲区
            out.close();
            workbook.close();

            return null; // 使用IO的形式完成下载、导出Excel的时候，返回null
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
