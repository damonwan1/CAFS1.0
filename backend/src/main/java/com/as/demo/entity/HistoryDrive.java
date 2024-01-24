package com.as.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.Year;


import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 历史数据表
 * </p>
 *
 * @author yule
 * @since 2021-05-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class HistoryDrive implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * Site_code
     */
    @Excel(name = "Site_code")
    private String siteCode;

    /**
     * Year
     */
    @Excel(name = "Year")
    private Integer year;

    /**
     * Month
     */
    @Excel(name = "Month")
    private Integer month;

    /**
     * Day
     */
    @Excel(name = "Day")
    private Integer day;

    /**
     * Ta
     */
    @Excel(name = "Ta")
    private String ta;

    /**
     * PAR
     */
    @Excel(name = "PAR")
    private String par;

    /**
     * RH
     */
    @Excel(name = "RH")
    private String rh;

    /**
     * SWC
     */
    @Excel(name = "SWC")
    private String swc;

    private Boolean mock;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;


    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(this.getYear()+"-"+this.getMonth()+"-"+this.getDay());
        sb.append("Par:"+this.getPar());
        sb.append("Rh:"+this.getRh());
        sb.append("Swc:"+this.getSwc());
        sb.append("Ta:"+this.getTa());
        return sb.toString();
    }
}
