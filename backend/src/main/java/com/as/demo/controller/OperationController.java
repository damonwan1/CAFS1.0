package com.as.demo.controller;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONArray;
import com.as.demo.entity.HistoryDrive;
import com.as.demo.entity.ShowResult;
import com.as.demo.service.IShowResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.as.demo.service.IOperationService;
import com.as.demo.utils.RemoteExecuteCommandutil;
import com.as.demo.utils.Result;

import cn.hutool.http.server.HttpServerRequest;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 操作记录表
 * </p>
 *
 * @author yule
 * @since 2021-05-18
 */
@RestController
@RequestMapping("/operation")
public class OperationController {
    @Autowired


    private IOperationService iOperationService;


    @Autowired
    private IShowResultService iShowResultService;

    @GetMapping("/say")
    public Result say(HttpServerRequest request) {
        return Result.success(iOperationService.say());
    }


    @GetMapping("/say2")
    public String say2() {
        Assert.isTrue(iShowResultService.importStatistics("C:\\Users\\Administrator\\Desktop\\file1.csv", LocalDateTime.now()), "导入正演统计数据失败");
        return "返回成功";
    }

    @GetMapping("/data")
    public Result data(String start, String end) {
        return Result.success(iOperationService.data(start, end));
    }

    @GetMapping("/data1")
    public Result data(String start, String end,String code) {
        return Result.success(iOperationService.data(code, start, end));
    }
    @GetMapping("/data2")
    public Result data(String start, String end,String file,int mode) {
        return Result.success(iOperationService.data(start, end, file, mode));
    }

    @GetMapping("/updateFCMASS")
    public Result updateFCMASS(String start, String end,String file,String sitecode) {
        return Result.success(iOperationService.updateFCMASS(start, end, file, sitecode));
    }

    @GetMapping("/mockRun")
    public Result mockRun(String start, String end) {

        DateTime startDate = DateUtil.parse(start);
        DateTime endDate = DateUtil.parse(end);
        long betweenDay = DateUtil.between(startDate, endDate, DateUnit.DAY);
        // 遍历start到end日期内的所有数据
        for (int i = 0; i <= betweenDay; i++) {
            DateTime time = DateUtil.offsetDay(startDate, i);
            iOperationService.mockNoRunDaySay(time.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        }
        return Result.success("");
    }

    @GetMapping("/mockRunSitecode")
    public Result mockRun(String start, String end, String sitecode) {

        DateTime startDate = DateUtil.parse(start);
        DateTime endDate = DateUtil.parse(end);
        long betweenDay = DateUtil.between(startDate, endDate, DateUnit.DAY);
        // 遍历start到end日期内的所有数据
        for (int i = 0; i <= betweenDay; i++) {
            DateTime time = DateUtil.offsetDay(startDate, i);
            iOperationService.mockNoRunDaySay(time.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),sitecode);
        }
        return Result.success("");
    }

    // 对应前端误差棒  Ta、PAR 、RH等
    @GetMapping("/allstation")
    public Result allstation() {
        try {
            return Result.success(iOperationService.allstation());
        } catch (Exception e) {
            return Result.failure("");
        }
    }
}
