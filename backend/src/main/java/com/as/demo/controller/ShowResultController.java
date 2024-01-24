package com.as.demo.controller;


import java.time.LocalDateTime;
import java.util.List;


import com.alibaba.fastjson.JSONArray;
import com.as.demo.entity.dto.ErrorBarDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.as.demo.entity.ShowResult;
import com.as.demo.service.IForecastResultService;
import com.as.demo.service.IShowResultService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * 预测结果表,
 * </p>
 *
 * @author yule
 * @since 2021-05-18
 */
@RestController
@RequestMapping("/show/result")
public class ShowResultController {
    @Autowired
    private IShowResultService iShowResultService;

    @GetMapping("/importCsv")
    public Boolean importCsv(@RequestParam("path") String path) {
        return iShowResultService.importCsv(path);
    }


    @GetMapping("/page")
    public Page<ShowResult> page(ShowResult vo, Page page) {
        return iShowResultService.page(vo, page);
    }

    // 对应实际数据
    @GetMapping("/all")
    public List<ShowResult> all(@RequestParam("site_code") String site_code, ShowResult vo) {
        return iShowResultService.all(site_code, vo);
    }

    // NEP \GPP等误差棒
    @GetMapping("/errorbar")
    public JSONArray errorbar(ShowResult vo) {
        return iShowResultService.errorbar(vo);
    }


    // NEP \GPP等误差棒
    @GetMapping("/errorbar1")
    public Boolean errorbar1(ShowResult vo) {
        return iShowResultService.importStatistics("E:\\111.csv", LocalDateTime.now());
    }


}
