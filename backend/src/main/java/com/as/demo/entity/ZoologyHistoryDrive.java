package com.as.demo.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

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
public class ZoologyHistoryDrive implements Serializable {

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
     * NEP
     */
    @Excel(name = "NEP")
    private String nep;

    /**
     * GPP
     */
    @Excel(name = "GPP")
    private String gpp;

    /**
     * NPP
     */
    @Excel(name = "NPP")
    private String npp;


    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    private Boolean mock;


    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(this.getYear()+"-"+this.getMonth()+"-"+this.getDay());
        sb.append("NEP:"+this.getNep());
        sb.append("GPP:"+this.getGpp());
        sb.append("NPP:"+this.getNpp());
        return sb.toString();
    }
}
