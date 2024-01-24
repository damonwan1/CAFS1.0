package com.as.demo.service.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.as.demo.entity.ShowStatistics;
import com.as.demo.mapper.ShowStatisticsMapper;
import com.as.demo.service.IShowStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.as.demo.entity.ShowResult;
import com.as.demo.mapper.ShowResultMapper;
import com.as.demo.service.IShowResultService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.text.csv.CsvReader;
import cn.hutool.core.text.csv.CsvUtil;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 预测结果表 服务实现类
 * </p>
 *
 * @author yule
 * @since 2021-05-18
 */
@Service
public class ShowResultServiceImpl extends ServiceImpl<ShowResultMapper, ShowResult> implements IShowResultService {
    @Autowired
    private IShowResultService iShowResultService;

    @Autowired
    private ShowResultMapper showResultMapper;

    @Autowired
    private ShowStatisticsMapper showStatisticsMapper;

    @Autowired
    private IShowStatisticsService iShowStatisticsService;


    /*
    * 导入正演生成数据到 show-result 表
    * */
    @Override
    public Boolean importCsv(String path) {
        CsvReader reader = CsvUtil.getReader();
        final List<ShowResult> result = reader.read(
                ResourceUtil.getUtf8Reader(path), ShowResult.class);
        result.forEach(ele -> {
            ele.setCreateTime(LocalDateTime.now());
        });
        return iShowResultService.saveBatch(result);
    }

    /*
     * 导入正演生成数据到 show-result 表
     * */
    @Override
    public Boolean importCsv(String path, LocalDateTime createTime) {
        CsvReader reader = CsvUtil.getReader();
        final List<ShowResult> result = reader.read(
                ResourceUtil.getUtf8Reader(path), ShowResult.class);
        result.forEach(ele -> {
            ele.setCreateTime(createTime);
        });
        return iShowResultService.saveBatch(result);
    }



    /*
     * 读取五百轮数据 求出最大最小值
     * */
    @Override
    public Boolean importStatistics(String path, LocalDateTime createTime) {
        CsvReader reader = CsvUtil.getReader();
        final List<ShowResult> result = reader.read(
                ResourceUtil.getUtf8Reader(path), ShowResult.class);
        List<ShowResult> collect = result.stream().filter(s -> s.getYear()!=null && s.getYear() != 0).collect(Collectors.toList());
        Map<String, List<ShowResult>> groupMap = collect.stream().collect(Collectors.groupingBy(
                data -> data.getYear() + "-" + data.getMonth() + "-" + data.getDay()
        ));
        String siteCode=collect.get(0).getSiteCode();
        List<ShowStatistics> resultList=new ArrayList<>();
        groupMap.keySet().forEach(key->{
            OptionalDouble gppMax = groupMap.get(key).stream()
                    .mapToDouble(data -> Double.parseDouble(StringUtils.isEmpty(data.getGpp()) || data.getGpp().equals("nan")?"0":data.getGpp()))
                    .max();
            OptionalDouble gppMin = groupMap.get(key).stream()
                    .mapToDouble(data -> Double.parseDouble(StringUtils.isEmpty(data.getGpp()) || data.getGpp().equals("nan")?"0":data.getGpp()))
                    .min();
            OptionalDouble nppMax = groupMap.get(key).stream()
                    .mapToDouble(data -> Double.parseDouble(StringUtils.isEmpty(data.getNpp()) || data.getNpp().equals("nan")?"0":data.getNpp()))
                    .max();
            OptionalDouble nppMin = groupMap.get(key).stream()
                    .mapToDouble(data -> Double.parseDouble(StringUtils.isEmpty(data.getNpp()) || data.getNpp().equals("nan")?"0":data.getNpp()))
                    .min();
            OptionalDouble nepMax = groupMap.get(key).stream()
                    .mapToDouble(data -> Double.parseDouble(StringUtils.isEmpty(data.getNep()) || data.getNep().equals("nan")?"0":data.getNep()))
                    .max();
            OptionalDouble nepMin = groupMap.get(key).stream()
                    .mapToDouble(data -> Double.parseDouble(StringUtils.isEmpty(data.getNep()) || data.getNep().equals("nan")?"0":data.getNep()))
                    .min();
            ShowStatistics showStatistics=new ShowStatistics();
            showStatistics.setStatisticsDate(key);
            showStatistics.setGppMax(gppMax.getAsDouble());
            showStatistics.setGppMin(gppMin.getAsDouble());
            showStatistics.setNepMax(nepMax.getAsDouble());
            showStatistics.setNepMin(nepMin.getAsDouble());
            showStatistics.setNppMax(nppMax.getAsDouble());
            showStatistics.setNppMin(nppMin.getAsDouble());
            showStatistics.setCreateTime(createTime);
            showStatistics.setSiteCode(siteCode);
            resultList.add(showStatistics);
        });
        return iShowStatisticsService.saveBatch(resultList);
    }
    /*
     * 导入最近days天的数据
     * */
    @Override
    public Boolean importCsv(String path, LocalDateTime createTime, int days) {
        CsvReader reader = CsvUtil.getReader();
        List<ShowResult> result = reader.read(
                ResourceUtil.getUtf8Reader(path), ShowResult.class);
        List<ShowResult> limitResult = new ArrayList<>();
        // 只保留N天预测的数据
        for(int i = result.size() - days; i < result.size(); i++) {
            limitResult.add(result.get(i));
        }
        limitResult.forEach(ele -> {
            ele.setCreateTime(createTime);
        });
        return iShowResultService.saveBatch(limitResult);
    }

    @Override
    public Page<ShowResult> page(ShowResult vo, Page page) {
        return showResultMapper.selectPage(page, Wrappers.<ShowResult>lambdaQuery()
                .eq(vo.getYear() != null, ShowResult::getYear, vo.getYear())
                .orderByDesc(ShowResult::getCreateTime)
        );
    }

    @Override
    public List<ShowResult> all(ShowResult vo) {

        return showResultMapper.selectList(Wrappers.emptyWrapper());

    }

    // 获取最近7天预测出来的数据
    // 虽然目前只用了当天的数据
    @Override
    public List<ShowResult> all(String site_code, ShowResult vo) {
        DateTimeFormatter dfDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String end = dfDate.format(LocalDateTime.now());
        DateTime endDate = DateUtil.offsetDay(DateUtil.parse(end),1);
        DateTime startDate = DateUtil.offsetDay(endDate, -7);
        String start = startDate.year()+"-"+startDate.monthBaseOne()+"-"+startDate.dayOfMonth();
        List<ShowResult> resList = showResultMapper.getLastShowResult(start,endDate.toString(),site_code);
        if (resList != null && resList.size() != 0) {
            return resList;
        }
        resList=showResultMapper.selectList(Wrappers.<ShowResult>lambdaQuery()
                .eq(!site_code.isEmpty(), ShowResult::getSiteCode, site_code));

        /*addDaysToResults(resList,39);
        int totalRecords = resList.size();
        int startIndex = Math.max(totalRecords - 60, 0);
        List<ShowResult> showResultList = resList.subList(startIndex, totalRecords);
        showResultList.forEach(s->{
            s.setCreateTime(LocalDateTime.now());
        });
        showResultMapper.batchInsertShowResults(showResultList);*/
        return resList;
    }


        // 构造函数、getter 和 setter 方法等省略

        public static void addDaysToResults(List<ShowResult> results, int daysToAdd) {
            for (ShowResult result : results) {
                int currentDay = result.getDay();
                int updatedDay = currentDay + daysToAdd;
                result.setDay(updatedDay);

                int maxDaysInMonth = getMaxDaysInMonth(result.getMonth(), result.getYear());
                if (updatedDay > maxDaysInMonth) {
                    int extraDays = updatedDay - maxDaysInMonth;
                    int updatedMonth = result.getMonth() + 1;
                    result.setMonth(updatedMonth);
                    result.setDay(extraDays);
                }
            }
        }

        private static int getMaxDaysInMonth(int month, int year) {
            // 返回指定月份和年份的最大天数，需要根据闰年等情况进行判断和处理
            // 这里只是一个简单的示例，你可能需要根据实际需求进行修改
            if (month == 2 && isLeapYear(year)) {
                return 29;
            } else if (month == 2) {
                return 28;
            } else if (month == 4 || month == 6 || month == 9 || month == 11) {
                return 30;
            } else {
                return 31;
            }
        }

        private static boolean isLeapYear(int year) {
            // 判断是否为闰年，根据实际需求进行实现
            // 这里只是一个简单的示例，你可能需要根据实际需求进行修改
            return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
        }




    private boolean checkShowResultList(List<ShowResult> showResultList){
        int count = 0;
        for (ShowResult result : showResultList) {
                if(result.getNep().equals("nan") || result.getNpp().equals("nan") || result.getGpp().equals("nan")){
                    return false;
                }
                if(result.getNep().equals("0.0") || result.getNpp().equals("0.0") || result.getGpp().equals("0.0")){
                    count++;
                }
        }
        return count <= 30;
    }

    @Override
    public JSONArray errorbar(ShowResult vo) {
        JSONArray jsonArray = new JSONArray();
        DateTimeFormatter dfDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String startDate = dfDate.format(LocalDateTime.now());
        DateTime endDate = DateUtil.offsetDay(DateUtil.parse(startDate),1);
        while (true){

            List<ShowResult> showResultList = showResultMapper.getLastShowResult(startDate,endDate.toString(),vo.getSiteCode());
            List<ShowStatistics> showStatisticsList  = showStatisticsMapper.getLastShowResult(startDate,endDate.toString(),vo.getSiteCode());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            if (CollectionUtils.isEmpty(showResultList)) {
                showResultList=showResultMapper.selectList(Wrappers.<ShowResult>lambdaQuery()
                        .eq(!vo.getSiteCode().isEmpty(), ShowResult::getSiteCode, vo.getSiteCode()).last("order by id desc limit 67"));
                Collections.reverse(showResultList);
                showStatisticsList=showStatisticsMapper.selectList(Wrappers.<ShowStatistics>lambdaQuery()
                        .eq(!vo.getSiteCode().isEmpty(), ShowStatistics::getSiteCode, vo.getSiteCode())
                        .ge(ShowStatistics::getCreateTime,showResultList.get(0).getCreateTime())
                        .last("order by STR_TO_DATE(statistics_Date, '%Y-%m-%d') desc limit 67"));
                Collections.reverse(showStatisticsList);
            }

            //排除存在前一天的任务今天才跑完情况
            Set<LocalDateTime> distinctDates = showStatisticsList.stream()
                    .map(ShowStatistics::getCreateTime)
                    .collect(Collectors.toSet());
            if (distinctDates.size() > 1) {
                DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                DateTimeFormatter dfDate3 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                Optional<LocalDateTime> maxDateOptional = distinctDates.stream().max(LocalDateTime::compareTo);
                showStatisticsList = showStatisticsMapper.selectList(Wrappers.<ShowStatistics>lambdaQuery()
                        .eq(!vo.getSiteCode().isEmpty(), ShowStatistics::getSiteCode, vo.getSiteCode())
                        .eq(ShowStatistics::getCreateTime,formatter1.format(maxDateOptional.get()))
                        .last("order by STR_TO_DATE(statistics_Date, '%Y-%m-%d') desc limit 67"));
                Collections.reverse(showStatisticsList);
            }


            Set<LocalDateTime> distinctDates2 = showResultList.stream()
                    .map(ShowResult::getCreateTime)
                    .collect(Collectors.toSet());
            if(distinctDates2.size()>1){
                DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                Optional<LocalDateTime> maxDateOptional = distinctDates2.stream().max(LocalDateTime::compareTo);
                showResultList=showResultMapper.selectList(Wrappers.<ShowResult>lambdaQuery()
                        .eq(!vo.getSiteCode().isEmpty(), ShowResult::getSiteCode, vo.getSiteCode()).eq(ShowResult::getCreateTime,formatter1.format(maxDateOptional.get())).last("order by id desc limit 67"));
                Collections.reverse(showResultList);
            }




            if(!checkShowResultList(showResultList)){
                startDate = dfDate.format(LocalDate.parse(startDate).minusDays(1));
                endDate = DateUtil.offsetDay(DateUtil.parse(startDate),1);
                continue;
            }


            //查出最近的一次的结果
            //查出最近一次的统计


            //获取日期集合
            List<String> dateList = showResultList.stream().map(s -> s.getYear() + "-" + s.getMonth() + "-" + s.getDay()).collect(Collectors.toList());
            Map<String, ShowStatistics> resultMap = showStatisticsList.stream()
                    .collect(Collectors.toMap(
                            ShowStatistics::getStatisticsDate,
                            statistics -> statistics
                    ));
            List<String> nepList = showResultList.stream().map(result->new BigDecimal(result.getNep()).toPlainString()).collect(Collectors.toList());
            List<String> nppList = showResultList.stream().map(result->new BigDecimal(result.getNpp()).toPlainString()).collect(Collectors.toList());
            List<String> gppList = showResultList.stream().map(result->new BigDecimal(result.getGpp()).toPlainString()).collect(Collectors.toList());

            List<List<String>> dataMap=new ArrayList<>();
            dataMap.add(nepList);
            dataMap.add(nppList);
            dataMap.add(gppList);

            String[] names = new String[] { "Nep", "Npp", "Gpp"};
            for (int i = 0; i < dataMap.size(); i++) {
                List<String> queryList = dataMap.get(i);

                JSONObject jsonObject = new JSONObject();
                String name = names[i];

                ArrayList<List<String>> jsonValue = new ArrayList<>();
                int finalI = i;
                dateList.forEach(s->{
                    List<String> list=new ArrayList<>();
                    ShowStatistics showStatistics = resultMap.get(s);
                    if(showStatistics!=null){

                        if(finalI ==0){
                            double randomValueMax = 0.20;
                            double randomValueMin = 0.17;
                            list.add(String.valueOf(showStatistics.getNepMax()+(showStatistics.getNepMax()*randomValueMax)));
                            list.add(String.valueOf(showStatistics.getNepMax()-(showStatistics.getNepMin()*randomValueMin)));
                        }else if(finalI==1){
                            double randomValueMax = 0.15;
                            double randomValueMin = 0.20;
                            list.add(String.valueOf(showStatistics.getNppMax()+(showStatistics.getNppMax()*randomValueMax)));
                            list.add(String.valueOf(showStatistics.getNppMin()-(showStatistics.getNppMin()*randomValueMin)));
                        }else {
                            double randomValueMax = 0.17;
                            double randomValueMin = 0.13;
                            list.add(String.valueOf(showStatistics.getGppMax()+(showStatistics.getGppMax()*randomValueMax)));
                            list.add(String.valueOf(showStatistics.getGppMin()-(showStatistics.getGppMin()*randomValueMin)));
                        }
                        jsonValue.add(list);
                    }
                });
                jsonObject.put("name", name);
                jsonObject.put("date", dateList);
                jsonObject.put("value", jsonValue);
                jsonObject.put("averageValue", queryList);
                jsonArray.add(jsonObject);
            }
            break;
        }

        return jsonArray;
    }

    private static Comparator<String> createComparator() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return (o1, o2) -> {
            Date nowDate_Date = null;
            Date endDate_Date = null;
            try {
                nowDate_Date = sdf.parse(o1);
                endDate_Date = sdf.parse(o2);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return nowDate_Date.compareTo(endDate_Date);
        };
    }

    public static void main(String[] args) {
        DateTimeFormatter dfDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String startDate = dfDate.format(LocalDateTime.now());
        DateTime endDate = DateUtil.offsetDay(DateUtil.parse(startDate),1);
        System.out.println(startDate);
        System.out.println(endDate);
    }


    //返回数组中与目标值 target 最接近的数值
    private static double findClosestValue(double[] arr, double target) {
        Arrays.sort(arr); // 排序
        int left = 0, right = arr.length - 1;
        double closestValue = arr[0];
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (arr[mid] == target) {
                return arr[mid];
            } else if (arr[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
            if (Math.abs(arr[mid] - target) < Math.abs(closestValue - target)) {
                closestValue = arr[mid];
            }
        }
        return closestValue;
    }
}
