package com.as.demo.entity.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.hutool.core.annotation.Alias;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FErrorBarDto {

    private Long id;
    /**
     * Site_code
     */
    private String siteCode;

    /**
     * Year
     */
    private Integer year;

    /**
     * Month
     */
    private Integer month;

    /**
     * Day
     */
    private Integer day;

    /**
     * TA
     */
    private String ta_min;
    /**
     * TA
     */
    private String ta_max;
    /**
     * TA
     */
    private String ta_avg;

    /**
     * PAR
     */
    private String par_min;
    /**
     * PAR
     */
    private String par_max;
    /**
     * PAR
     */
    private String par_avg;

    /**
     * Rh
     */
    private String rh_min;
    /**
     * Rh
     */
    private String rh_max;
    /**
     * Rh
     */
    private String rh_avg;

    /**
     * SWC
     */
    private String swc_min;
    /**
     * SWC
     */
    private String swc_max;
    /**
     * SWC
     */
    private String swc_avg;

    /**
     * VPD
     */
    private String vpd_min;
    /**
     * VPD
     */
    private String vpd_max;
    /**
     * VPD
     */
    private String vpd_avg;



    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}
