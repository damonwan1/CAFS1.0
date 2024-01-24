package com.as.demo.service;

import java.time.LocalDateTime;
import java.util.List;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.as.demo.entity.ForecastResult;
import com.as.demo.entity.ShowResult;
import com.as.demo.entity.dto.ErrorBarDto;
import com.as.demo.entity.dto.FErrorBarDto;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 预测结果表 服务类
 * </p>
 *
 * @author yule
 * @since 2021-05-18
 */
public interface IForecastResultService extends IService<ForecastResult> {
    Boolean importCsv(String path);

    Page<ForecastResult> page(ForecastResult vo, Page page);

    /**
     * 返回forecastresult表中的所有记录
     * @param vo
     * @return
     */
    List<ForecastResult> all(ForecastResult vo);

    /**
     * 返回forecastresult表中的所有记录
     * 一共返回67条数据：60条历史数据+7天预测数据
     * @param vo
     * @return
     */
    List<ForecastResult> all(String site_code, ForecastResult vo);

    JSONArray errorbar(ShowResult vo, String siteCode);

    /**
     *  从csv导入days天的数据到forecast_result表
     * @param path
     * @param days
     * @return
     */
    Boolean importCsv(String path, int days);

    /**
     * 模拟某一天执行
     * @param path
     * @param createTime
     * @return
     */
    Boolean importCsv(String path, LocalDateTime createTime);

    /**
     * 模拟某一天执行  从csv导入days天的数据到forecast_result表
     * @param path
     * @param createTime
     * @return
     */
    Boolean importCsv(String path, LocalDateTime createTime, int days);
}
