package com.as.demo.mapper;

import com.as.demo.entity.ShowResult;
import com.as.demo.entity.ShowStatistics;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 生态500轮统计
 * </p>
 *
 * @author yule
 * @since 2021-05-18
 */
public interface ShowStatisticsMapper extends BaseMapper<ShowStatistics> {
    List<ShowStatistics> getLastShowResult(String start, String end, String site_code);
}
