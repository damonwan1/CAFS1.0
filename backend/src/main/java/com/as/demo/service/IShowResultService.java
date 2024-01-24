package com.as.demo.service;

import java.time.LocalDateTime;
import java.util.List;


import com.alibaba.fastjson.JSONArray;
import com.as.demo.entity.ShowResult;
import com.as.demo.entity.dto.ErrorBarDto;
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
public interface IShowResultService extends IService<ShowResult> {
    /**
     * 将该文件的所有内容导入到show_result表中
     * @param path  文件所对应的路径
     * @return
     */
    Boolean importCsv(String path);

    Page<ShowResult> page(ShowResult vo, Page page);

    /**
     * 返回showresult表中所有的记录
     * @param vo
     * @return
     */
    List<ShowResult> all(ShowResult vo);

    /**
     * 返回showresult表中所有的记录
     * @param vo
     * @return
     */
    List<ShowResult> all(String site_code, ShowResult vo);

    JSONArray errorbar(ShowResult vo);

    /**
     * 模拟某一天执行
     * @param path
     * @param createTime
     * @return
     */
    Boolean importCsv(String path, LocalDateTime createTime);

    /**
     * 模拟某一天执行
     * @param path
     * @param createTime
     * @return
     */
    Boolean importCsv(String path, LocalDateTime createTime, int days);


    Boolean importStatistics(String path, LocalDateTime createTime);


}
