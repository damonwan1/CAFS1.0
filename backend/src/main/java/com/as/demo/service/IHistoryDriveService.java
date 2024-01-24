package com.as.demo.service;

import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;


import com.as.demo.entity.ShowResult;
import org.springframework.web.multipart.MultipartFile;

import com.as.demo.entity.HistoryDrive;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 历史数据表 服务类
 * </p>
 *
 * @author yule
 * @since 2021-05-18
 */
public interface IHistoryDriveService extends IService<HistoryDrive> {
    /**
     * * 从历史驱动表中: historyDriver
     * 数据库搜索2012-2025年的站点数据，并存储为 /tmp/data.csv
     *
     * @return
     */
    Boolean exportCsv(String site_code);

    Boolean exportCsv(String site_code, String end);

    Boolean exportCsv();

    Boolean importExcel(MultipartFile multipartFile) throws Exception;

    List<HistoryDrive> getLatest60(String site_code, String endtime);


}