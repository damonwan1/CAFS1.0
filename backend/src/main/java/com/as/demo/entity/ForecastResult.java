package com.as.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.Year;


import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.hutool.core.annotation.Alias;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 预测结果表
 * </p>
 *
 * @author yule
 * @since 2021-05-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ForecastResult implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * Site_code
     */
    @Alias("Site_code")
    @Excel(name = "Site_code")
    private String siteCode;
    /**
     * Year
     */
    @Alias("Year")
    @Excel(name = "Year")
    private Integer year;

    /**
     * Month
     */
    @Alias("Month")
    @Excel(name = "Month")
    private Integer month;

    /**
     * Day
     */
    @Alias("Day")
    @Excel(name = "Day")
    private Integer day;

    /**
     * Ta
     */
    @Alias("Ta")
    @Excel(name = "Ta(℃)")
    private String ta;
    /**
     * PAR
     */
    @Alias("PAR")
    @Excel(name = "PAR(mol/day)")
    private String par;

    /**
     * RH
     */
    @Alias("RH")
    @Excel(name = "RH(%)")
    private String rh;

    /**
     * SWC
     */
    @Alias("SWC")
    @Excel(name = "SWC")
    private String swc;

    /**
     * SWC
     */
    @Alias("VPD")
    @Excel(name = "VPD")
    private String vpd;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}
