package com.as.demo.controller;

import java.util.List;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.as.demo.entity.ShowResult;
import com.as.demo.entity.dto.ErrorBarDto;
import com.as.demo.entity.dto.FErrorBarDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.as.demo.entity.ForecastResult;
import com.as.demo.service.IForecastResultService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * 预测结果表 前端渲染调用
 * </p>
 *
 * @author yule
 * @since 2021-05-18
 */
@RestController
@RequestMapping("/forecast/result")
public class ForecastResultController {

    @Autowired
    private IForecastResultService iForecastResultService;

    @GetMapping("/importCsv")
    public Boolean importCsv(@RequestParam("path") String path) {
        return iForecastResultService.importCsv(path);
    }

    @GetMapping("/page")
    public Page<ForecastResult> page(ForecastResult vo, Page page) {
        return iForecastResultService.page(vo, page);
    }

    // 对应 预测数据
    @GetMapping("/all")
    public List<ForecastResult> all(@RequestParam("site_code") String site_code, ForecastResult vo) {
        return iForecastResultService.all(site_code, vo);
    }

    // 对应前端误差棒  Ta、PAR 、RH等
    @GetMapping("/errorbar")
    public JSONArray errorbar(@RequestParam("site_code") String site_code,ShowResult vo) {
        if(site_code == null || site_code.equals("")) {
            site_code = "DHF";
        }
        return iForecastResultService.errorbar(vo,site_code);
    }




}
