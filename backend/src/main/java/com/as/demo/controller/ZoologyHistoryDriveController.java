package com.as.demo.controller;

import com.as.demo.controller.req.ZAllReq;
import com.as.demo.entity.ShowResult;
import com.as.demo.entity.ZoologyHistoryDrive;
import com.as.demo.service.IZoologyHistoryDriveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 历史数据表 前端控制器
 * </p>
 *
 * @author yule
 * @since 2021-05-18
 */
@RestController
@RequestMapping("/zoologHistory/drive")
public class ZoologyHistoryDriveController {
    @Autowired
    private IZoologyHistoryDriveService iZoologyHistoryDriveService;

    // 对应nep gpp npp实际数据
    @GetMapping("/all")
    public List<ZoologyHistoryDrive> all(ZAllReq zAllReq) {
        return iZoologyHistoryDriveService.all(zAllReq);
    }

}
