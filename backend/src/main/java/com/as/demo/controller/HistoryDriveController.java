package com.as.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.as.demo.service.IHistoryDriveService;

/**
 * <p>
 * 历史数据表 前端控制器
 * </p>
 *
 * @author yule
 * @since 2021-05-18
 */
@RestController
@RequestMapping("/history/drive")
public class HistoryDriveController {
    @Autowired
    private IHistoryDriveService iHistoryDriveService;

    @GetMapping("/exportCsv")
    public Boolean exportCsv(@RequestParam String sitecode) {
        if (sitecode == null || sitecode.equals("")) {
            return iHistoryDriveService.exportCsv();
        }
        return iHistoryDriveService.exportCsv(sitecode);
    }


    @PostMapping("/importExcel")
    public Boolean importExcel(@RequestParam("file") MultipartFile multipartFile) throws Exception {
        return iHistoryDriveService.importExcel(multipartFile);
    }
}
