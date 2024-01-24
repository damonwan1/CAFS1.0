package com.as.demo.utils;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.as.demo.entity.HistoryDrive;
import com.as.demo.entity.ZoologyHistoryDrive;

import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

public class ErrorCorrectionUtil {

    /**
     * 输入list，返回该Ta的平均值
     * @param historyDrives
     * @return
     */
    public static double lastAvgTa(List<HistoryDrive> historyDrives){
        DoubleSummaryStatistics sum = historyDrives.stream().collect(
                Collectors.summarizingDouble(e -> Double.parseDouble(e.getTa())));
        return sum.getAverage();
    };

    /**
     * 输入list，返回该RH的平均值
     * @param historyDrives
     * @return
     */
    public static double lastAvgRH(List<HistoryDrive> historyDrives){
        DoubleSummaryStatistics sum = historyDrives.stream().collect(
                Collectors.summarizingDouble(e -> Double.parseDouble(e.getRh())));
        return sum.getAverage();
    };

    /**
     * 输入list，返回该PAR的平均值
     * @param historyDrives
     * @return
     */
    public static double lastAvgPAR(List<HistoryDrive> historyDrives){
        DoubleSummaryStatistics sum = historyDrives.stream().collect(
                Collectors.summarizingDouble(e -> Double.parseDouble(e.getPar())));
        return sum.getAverage();
    };

    /**
     * 输入list，返回该SWC的平均值
     * @param historyDrives
     * @return
     */
    public static double lastAvgSWC(List<HistoryDrive> historyDrives){
        DoubleSummaryStatistics sum = historyDrives.stream().collect(
                Collectors.summarizingDouble(e -> Double.parseDouble(e.getSwc())));
        return sum.getAverage();
    };



    /**
     * 输入list，返回该NEE的平均值
     * @param historyDrives
     * @return
     */
    public static double correctionNEE(List<HistoryDrive> historyDrives){
        return -9999;
    };



    /**
     * 验证Ta是否符合标准
     * @param value
     * @return
     */
    public static boolean validTa(double value){
        if (value >3 && value <35 ) {
            return true;
        }
        return false;
    }

    /**
     * 验证RH是否符合标准
     * @param value
     * @return
     */
    public static boolean validRH(double value){
        if (value >20 && value <100 ) {
            return true;
        }
        return false;
    }

    /**
     * 验证PAR是否符合标准
     * @param value
     * @return
     */
    public static boolean validPAR(double value){
        if (value >0 && value <61 ) {
            return true;
        }
        return false;
    }

    /**
     * 验证SWC是否符合标准
     * @param value
     * @return
     */
    public static boolean validSWC(double value){
        if (value >=0.1 && value <= 0.4 ) {
            return true;
        }
        return false;
    }

    /**
     * 验证NEE是否符合标准
     * @param value
     * @return
     */
    public static boolean validNEE(double value){
        if (value >-10 && value <10 ) {
            return true;
        }
        return false;
    }

    /**
     * 输入list，返回该NEP的平均值
     * @param zoologyHistoryDrives
     * @return
     */
    public static double lastAvgNEP(List<ZoologyHistoryDrive> zoologyHistoryDrives){
        DoubleSummaryStatistics sum = zoologyHistoryDrives.stream().collect(
        Collectors.summarizingDouble(e -> Double.parseDouble(e.getNep())));
        return sum.getAverage();
    };


}
