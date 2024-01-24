package com.as.demo.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.as.demo.entity.ShowResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.as.demo.entity.HistoryDrive;
import com.as.demo.mapper.HistoryDriveMapper;
import com.as.demo.service.IHistoryDriveService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.afterturn.easypoi.csv.CsvExportUtil;
import cn.afterturn.easypoi.csv.entity.CsvExportParams;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;

/**
 * <p>
 * 历史数据表 服务实现类
 * </p>
 *
 * @author yule
 * @since 2021-05-18
 */
@Service
public class HistoryDriveServiceImpl extends ServiceImpl<HistoryDriveMapper, HistoryDrive>
        implements IHistoryDriveService {
    @Autowired
    private HistoryDriveMapper historyDriveMapper;

    @Autowired
    private IHistoryDriveService iHistoryDriveService;


    // 获取最近7天预测出来的数据
    // 虽然目前只用了当天的数据
    @Override
    public List<HistoryDrive> getLatest60(String site_code, String end) {

        DateTimeFormatter dfDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        //String end = dfDate.format(LocalDateTime.now());
        DateTime endDate = DateUtil.offsetDay(DateUtil.parse(end),-1);
        DateTime startDate = DateUtil.offsetDay(endDate, -61);
        return historyDriveMapper.selectFromStartToEnd(startDate.year()+"-"+startDate.monthStartFromOne()+"-"+startDate.dayOfMonth(), endDate.year()+"-"+endDate.monthStartFromOne()+"-"+endDate.dayOfMonth(), site_code);

    }

    /*
    * 从历史驱动表中: historyDriver
    * 数据库搜索2012-2025年数据，并存储为 /tmp/data-site_code.csv
    * */
    @Override
    public Boolean exportCsv(String site_code) {
        System.out.println("enter :exportCsv(String site_code)");
        String start = "2005-01-01";
        String end = "2025-06-20";
        List<HistoryDrive> historyDrives = historyDriveMapper.listByCode(site_code, start, end);
        System.out.println("historyDrives size:"+historyDrives.size());
        File savefile = new File("/tmp/");
        if (!savefile.exists()) {
            System.out.println("Create /tmp/ directory");
            savefile.mkdirs();
        }
        FileOutputStream fos;
        try {
            System.out.println("Create File :"+"/tmp/data-"+site_code+".csv");
            fos = new FileOutputStream("/tmp/data-"+site_code+".csv");
            CsvExportParams params = new CsvExportParams(CsvExportParams.UTF8);
            CsvExportUtil.exportCsv(params, HistoryDrive.class, historyDrives, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public Boolean exportCsv(String site_code, String end) {
        System.out.println("enter :exportCsv(String site_code)");
        String start = "2005-01-01";
        //String end = "2025-06-20";
        List<HistoryDrive> historyDrives = historyDriveMapper.listByCode(site_code, start, end);
        System.out.println("historyDrives size:"+historyDrives.size());
        File savefile = new File("/tmp/");
        if (!savefile.exists()) {
            System.out.println("Create /tmp/ directory");
            savefile.mkdirs();
        }
        FileOutputStream fos;
        try {
            System.out.println("Create File :"+"/tmp/data-"+site_code+".csv");
            fos = new FileOutputStream("/tmp/data-"+site_code+".csv");
            CsvExportParams params = new CsvExportParams(CsvExportParams.UTF8);
            CsvExportUtil.exportCsv(params, HistoryDrive.class, historyDrives, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public Boolean exportCsv() {
        System.out.println("enter :exportCsv()");
        String start = "2005-01-01";
        String end = "2023-06-20";
        List<HistoryDrive> historyDrives = historyDriveMapper.list(start, end);
        System.out.println("historyDrives size:"+historyDrives.size());
        File savefile = new File("/tmp/");
        if (!savefile.exists()) {
            System.out.println("Create /tmp/ directory");
            savefile.mkdirs();
        }
        FileOutputStream fos;
        try {
            System.out.println("Create File :"+"/tmp/data.csv");
            fos = new FileOutputStream("/tmp/data.csv");
            CsvExportParams params = new CsvExportParams(CsvExportParams.UTF8);
            CsvExportUtil.exportCsv(params, HistoryDrive.class, historyDrives, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public Boolean importExcel(MultipartFile multipartFile) throws Exception {
        ImportParams params = new ImportParams();
        params.setHeadRows(1);
        // params.setTitleRows(0);
        List<HistoryDrive> result = ExcelImportUtil.importExcel(multipartFile.getInputStream(),
                HistoryDrive.class, params);
        result.forEach(ele -> {
            ele.setCreateTime(LocalDateTime.now());
        });
        return iHistoryDriveService.saveBatch(result);
    }

}
