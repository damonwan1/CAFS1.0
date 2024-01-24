package com.as.demo.mapper;

import cn.hutool.core.date.DateTime;
import com.as.demo.entity.HistoryDrive;
import com.as.demo.entity.ShowResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 预测结果表 Mapper 接口
 * </p>
 *
 * @author yule
 * @since 2021-05-18
 */
public interface ShowResultMapper extends BaseMapper<ShowResult> {

    /**
     * 查询历史同期数据
     * @param start
     * @param end
     */
    List<ShowResult> getLastShowResult(String start, String end, String site_code);

    void batchInsertShowResults(List<ShowResult> list);
}
