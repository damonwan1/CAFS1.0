package com.as.demo.service;

import com.as.demo.controller.req.ZAllReq;
import com.as.demo.entity.ShowResult;
import com.as.demo.entity.ZoologyHistoryDrive;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 生态历史数据表 服务类
 * </p>
 *
 * @author yule
 * @since 2021-05-18
*/

public interface IZoologyHistoryDriveService extends IService<ZoologyHistoryDrive> {

    List<ZoologyHistoryDrive> all(ZAllReq zAllReq);

}

