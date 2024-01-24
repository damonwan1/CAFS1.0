package com.as.demo.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.hutool.core.annotation.Alias;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;

public class ShowStatistics {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String siteCode;

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    /**
     * Year
     */
    private String statisticsDate;

    /**
     * NEP
     */
    private Double nepMin;

    private Double nepMax;

    /**
     * GPP
     */
    private Double gppMax;

    private Double gppMin;

    /**
     * NPP
     */
    private Double nppMax;

    private Double nppMin;

    private LocalDateTime createTime;

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatisticsDate() {
        return statisticsDate;
    }

    public void setStatisticsDate(String statisticsDate) {
        this.statisticsDate = statisticsDate;
    }

    public Double getNepMin() {
        return nepMin;
    }

    public void setNepMin(Double nepMin) {
        this.nepMin = nepMin;
    }

    public Double getNepMax() {
        return nepMax;
    }

    public void setNepMax(Double nepMax) {
        this.nepMax = nepMax;
    }

    public Double getGppMax() {
        return gppMax;
    }

    public void setGppMax(Double gppMax) {
        this.gppMax = gppMax;
    }

    public Double getGppMin() {
        return gppMin;
    }

    public void setGppMin(Double gppMin) {
        this.gppMin = gppMin;
    }

    public Double getNppMax() {
        return nppMax;
    }

    public void setNppMax(Double nppMax) {
        this.nppMax = nppMax;
    }

    public Double getNppMin() {
        return nppMin;
    }

    public void setNppMin(Double nppMin) {
        this.nppMin = nppMin;
    }


    @Override
    public String toString() {
        return "ShowStatistics{" +
                "id=" + id +
                ", statisticsDate='" + statisticsDate + '\'' +
                ", nepMin=" + nepMin +
                ", nepMax=" + nepMax +
                ", gppMax=" + gppMax +
                ", gppMin=" + gppMin +
                ", nppMax=" + nppMax +
                ", nppMin=" + nppMin +
                ", createTime=" + createTime +
                '}';
    }
}
