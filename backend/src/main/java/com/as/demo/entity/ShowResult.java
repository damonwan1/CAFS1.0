package com.as.demo.entity;

import java.io.Serializable;
import java.time.LocalDateTime;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

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
public class ShowResult implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * Site_code
     */
    @Alias("Site_code")
    private String siteCode;

    /**
     * Year
     */
    @Alias("Year")
    private Integer year;

    /**
     * Month
     */
    @Alias("Month")
    private Integer month;

    /**
     * Day
     */
    @Alias("Day")
    private Integer day;
    /**
     * GPP
     */
    @Alias("gppm")
    private String gpp;

    /**
     * NPP
     */
    @Alias("NPP")
    private String npp;

    /**
     * NEP
     */
    @Alias("NEP")
    private String nep;
    /**
     * Ra
     */
    @Alias("Ra")
    private String ra;

    /**
     * Rh
     */
    @Alias("Rh")
    private String rh;

    /**
     * Cf
     */
    @Alias("Cf")
    private String cf;

    /**
     * Cw
     */
    @Alias("Cw")
    private String cw;

    /**
     * Cr
     */
    @Alias("Cr")
    private String cr;
    /**
     * Cveg
     */
    @Alias("Cveg")
    private String cveg;

    /**
     * Csom
     */
    @Alias("Csom")
    private String csom;

    /**
     * Af
     */
    @Alias("Af")
    private String af;
    /**
     * Aw
     */
    @Alias("Aw")
    private String aw;

    /**
     * Ar
     */
    @Alias("Ar")
    private String ar;

    /**
     * Tveg
     */
    @Alias("Tveg")
    private String tveg;

    /**
     * Tsoil
     */
    @Alias("Tsom")
    private String tsoil;

    /**
     * SWC
     */
    private String swc;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}
