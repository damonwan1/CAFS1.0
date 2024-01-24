package com.as.demo.service.impl;

import com.as.demo.controller.req.ZAllReq;
import com.as.demo.entity.ShowResult;
import com.as.demo.entity.ZoologyHistoryDrive;
import com.as.demo.mapper.ZoologyHistoryDriveMapper;
import com.as.demo.service.IZoologyHistoryDriveService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 生态历史数据表 服务类
 * </p>
 *
 * @author yule
 * @since 2021-05-18
 */
@Service
public class ZoologyHistoryDriveServiceImpl extends ServiceImpl<ZoologyHistoryDriveMapper,ZoologyHistoryDrive> implements IZoologyHistoryDriveService{

    @Autowired
    private ZoologyHistoryDriveMapper zoologyHistoryDriveMapper;

    @Override
    public List<ZoologyHistoryDrive> all(ZAllReq zAllReq) {
        List<ZoologyHistoryDrive> all = zoologyHistoryDriveMapper.selectFromStartToEnd(zAllReq.getStartDate(),zAllReq.getEndDate(),zAllReq.getSite_code());
        return all;
    }
}