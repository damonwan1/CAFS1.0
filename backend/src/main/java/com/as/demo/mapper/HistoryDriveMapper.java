package com.as.demo.mapper;

import java.util.List;


import com.as.demo.entity.HistoryDrive;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 历史数据表 Mapper 接口
 * </p>
 *
 * @author yule
 * @since 2021-05-18
 */
public interface HistoryDriveMapper extends BaseMapper<HistoryDrive> {

    /**
     *  查询start到end日期内的所有驱动数据
     * @param start
     * @param end
     */
    List<HistoryDrive> listByCode(String site_code, String start, String end);

    /**
     *  查询start到end日期内的所有驱动数据
     * @param start
     * @param end
     */
    List<HistoryDrive> list( String start, String end);


    /**
     * 删除 history_drive表的start到end期间的数据
     * @param start
     * @param end
     */
    void deleteOld(String start, String end);

    /**
     * 删除 history_drive表的start到end期间的数据, sitecode
     * @param start
     * @param end
     */
    void deleteOldBySitecode(String start, String end, String site_code);

    /**
     * 查询历史同期数据
     * @param month
     * @param day
     * sitecode
     */
    List<HistoryDrive> historySameTime(int month, int day, String site_code);

    /**
     * 查询  history_drive表的start到end期间的数据, sitecode
     * @param start
     * @param end
     */
    List<HistoryDrive> selectFromStartToEnd(String start, String end, String site_code);
}
