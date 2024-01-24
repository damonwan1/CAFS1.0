package com.as.demo.mapper;

import com.as.demo.entity.ZoologyHistoryDrive;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 生态历史数据表（真实） Mapper 接口
 * </p>
 *
 * @author yule
 * @since 2021-05-18
 */
public interface ZoologyHistoryDriveMapper extends BaseMapper<ZoologyHistoryDrive> {

    /**
     *  查询start到end日期内的所有驱动数据
     * @param start
     * @param end
     */
    List<ZoologyHistoryDrive> listByCode(String site_code, String start, String end);

    /**
     *  查询start到end日期内的所有驱动数据
     * @param start
     * @param end
     */
    List<ZoologyHistoryDrive> list( String start, String end);


    /**
     * 删除 zoology_history_drive表的start到end期间的数据
     * @param start
     * @param end
     */
    void deleteOld(String start, String end);

    /**
     * 删除 zoology_history_drive表的start到end期间的数据, sitecode
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
    List<ZoologyHistoryDrive> historySameTime(int month, int day, String site_code);

    /**
     * 查询  zoology_history_drive表的start到end期间的数据, sitecode
     * @param start
     * @param end
     */
    List<ZoologyHistoryDrive> selectFromStartToEnd(@Param("startDate") String startDate, @Param("endDate")String endDate, @Param("site_code")String site_code);


}
