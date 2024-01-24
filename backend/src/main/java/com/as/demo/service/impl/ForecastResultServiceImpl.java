package com.as.demo.service.impl;

import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.as.demo.entity.HistoryDrive;
import com.as.demo.entity.dto.ErrorBarDto;
import com.as.demo.entity.dto.FErrorBarDto;
import com.as.demo.mapper.HistoryDriveMapper;
import com.as.demo.service.IHistoryDriveService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com. mysql.cj.xdevapi.JsonArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.as.demo.entity.ForecastResult;
import com.as.demo.entity.ShowResult;
import com.as.demo.mapper.ForecastResultMapper;
import com.as.demo.service.IForecastResultService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.text.csv.CsvReader;
import cn.hutool.core.text.csv.CsvUtil;

/**
 * <p>
 * 预测结果表 服务实现类
 * </p>
 *
 * @author yule
 * @since 2021-05-18
 */
@Service
public class ForecastResultServiceImpl extends ServiceImpl<ForecastResultMapper, ForecastResult>
        implements IForecastResultService {
    @Autowired
    private IForecastResultService iForecastResultService;
    @Autowired
    private ForecastResultMapper forecastResultMapper;
    @Autowired
    private HistoryDriveMapper historyDriveMapper;

    @Autowired
    private IHistoryDriveService iHistoryDriveService;


    /*
    * 导入csv全部数据到forecast表
    * */
    @Override
    public Boolean importCsv(String path) {
        CsvReader reader = CsvUtil.getReader();
        final List<ForecastResult> result = reader.read(
                ResourceUtil.getUtf8Reader(path), ForecastResult.class);
        result.forEach(ele -> {
            ele.setCreateTime(LocalDateTime.now());
        });
        return iForecastResultService.saveBatch(result);
    }

    /*
     * 模拟某一天导入
     * */
    @Override
    public Boolean importCsv(String path, LocalDateTime createTime) {
        CsvReader reader = CsvUtil.getReader();
        final List<ForecastResult> result = reader.read(
                ResourceUtil.getUtf8Reader(path), ForecastResult.class);
        result.forEach(ele -> {
            ele.setCreateTime(createTime);
        });
        return iForecastResultService.saveBatch(result);
    }

    /*
     * 导入csv到forecast表
     * days代表最近N天
     * */
    @Override
    public Boolean importCsv(String path,LocalDateTime createTime, int days) {
        CsvReader reader = CsvUtil.getReader();
        List<ForecastResult> result = reader.read(
                ResourceUtil.getUtf8Reader(path), ForecastResult.class);
        List<ForecastResult> limitResult = new ArrayList<>();
        // 只保留N天预测的数据
        for(int i = result.size() - days; i < result.size(); i++) {
            limitResult.add(result.get(i));
        }
        limitResult.forEach(ele -> {
            ele.setCreateTime(createTime);
        });
        return iForecastResultService.saveBatch(limitResult);
    }

    /*
     * 导入csv到forecast表
     * days代表最近N天
     * */
    @Override
    public Boolean importCsv(String path, int days) {
        CsvReader reader = CsvUtil.getReader();
        List<ForecastResult> result = reader.read(
                ResourceUtil.getUtf8Reader(path), ForecastResult.class);
        List<ForecastResult> limitResult = new ArrayList<>();
        // 只保留N天预测的数据
        for(int i = result.size() - days; i < result.size(); i++) {
            limitResult.add(result.get(i));
        }
        limitResult.forEach(ele -> {
            ele.setCreateTime(LocalDateTime.now());
        });
        return iForecastResultService.saveBatch(limitResult);
    }

    @Override
    public Page<ForecastResult> page(ForecastResult vo, Page page) {
        return forecastResultMapper.selectPage(page, Wrappers.<ForecastResult>lambdaQuery()
                .eq(vo.getYear() != null, ForecastResult::getYear, vo.getYear())
                .orderByDesc(ForecastResult::getCreateTime)
        );
    }

    @Override
    public List<ForecastResult> all(ForecastResult vo) {
        return forecastResultMapper.selectList(Wrappers.emptyWrapper());
    }

    @Override
    public List<ForecastResult> all(String site_code, ForecastResult vo) {
        List<ForecastResult> all = forecastResultMapper.selectList(Wrappers.<ForecastResult>lambdaQuery()
                .eq(!site_code.isEmpty(), ForecastResult::getSiteCode, site_code)
                .orderBy(true,false,ForecastResult::getId)
                .last("limit 67"));


        Collections.reverse(all);

        return all;

//
//        List<ForecastResult> last7 = forecastResultMapper.selectList(Wrappers.<ForecastResult>lambdaQuery()
//                .eq(!site_code.isEmpty(), ForecastResult::getSiteCode, site_code)
//                        .orderBy(true,false,ForecastResult::getId)
//                        .last("limit 7")
//                );
//        Collections.reverse(last7);
//        List<ForecastResult> all = new ArrayList<>();
//
//        // 翻转数组，7天正序
//        if (last7 != null && last7.get(0) != null) {
//            String timeLimit = last7.get(0).getYear() + "-" + last7.get(0).getMonth() + "-" + last7.get(0).getDay();
//            List<HistoryDrive> historyDriveList = iHistoryDriveService.getLatest60(site_code, timeLimit);
//            for (int i = 0 ; i < historyDriveList.size(); i++) {
//                ForecastResult fr = new ForecastResult();
//                fr.setCreateTime(historyDriveList.get(i).getCreateTime());
//                fr.setDay(historyDriveList.get(i).getDay());
//                fr.setId(historyDriveList.get(i).getId());
//                fr.setMonth(historyDriveList.get(i).getMonth());
//                fr.setPar(historyDriveList.get(i).getPar());
//                fr.setRh(historyDriveList.get(i).getRh());
//                fr.setSiteCode(historyDriveList.get(i).getSiteCode());
//                fr.setSwc(historyDriveList.get(i).getSwc());
//                fr.setTa(historyDriveList.get(i).getTa());
//                fr.setVpd(historyDriveList.get(i).getSwc());
//                fr.setYear(historyDriveList.get(i).getYear());
//            }
//
//        }
//        // 查询前60天历史数据
//        return null;

    }

    @Override
    public JSONArray errorbar(ShowResult vo, String siteCode) {
        // 求出七天预测的最大值、最小值 和 平均值
        //预测表中所有数据
        //List<ForecastResult> forecastListALL = forecastResultMapper.selectList(Wrappers.emptyWrapper());
        QueryWrapper<ForecastResult> queryWrapper = new QueryWrapper();
        queryWrapper.eq("site_code", siteCode);//
        List<ForecastResult> forecastListALL = forecastResultMapper.selectList(queryWrapper );
        //筛选出预测数据
        List<ForecastResult> forecastList = new ArrayList<ForecastResult>();
        //筛选出实际数据
        Map<String,ForecastResult> actualMap= new HashMap<>();
        DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        //筛选出所有预测的数据，即年月日大于创建时间
        //年月日大于创建时间 代表这些数据是预测出来的 否则都是原始真实数据
        for (ForecastResult fr : forecastListALL) {
            String time1 = fr.getYear() + "-" + ((fr.getMonth() < 10) ? ("0" + fr.getMonth()) : fr.getMonth())  +"-" + ((fr.getDay() < 10) ? ("0" + fr.getDay()) : fr.getDay());
            if (LocalDate.parse(time1, dtf2).isAfter(fr.getCreateTime().toLocalDate())) {
                forecastList.add(fr);
            }else {
                //修改需求（拿到最接近实际值的预测值） 取实际值
                actualMap.put(fr.getYear() + "-" + fr.getMonth() +"-" + fr.getDay(),fr);
            }
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // key 为 日期，  value为 预测的list
        Map<String, List<String>> queryTa = new TreeMap<>((Comparator<String>) (o1, o2) -> {
         //指定排序器按照降序排列
            Date nowDate_Date = null;
            Date endDate_Date = null;
            try {
                nowDate_Date = sdf.parse(o1);
                endDate_Date = sdf.parse(o2);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return nowDate_Date.compareTo(endDate_Date);
        });
        Map<String, List<String>> queryPar = new TreeMap<>((Comparator<String>) (o1, o2) -> {
         //指定排序器按照降序排列
            Date nowDate_Date = null;
            Date endDate_Date = null;
            try {
                nowDate_Date = sdf.parse(o1);
                endDate_Date = sdf.parse(o2);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return nowDate_Date.compareTo(endDate_Date);
        });
        Map<String, List<String>> queryRh = new TreeMap<>((Comparator<String>) (o1, o2) -> {
            //指定排序器按照降序排列
            Date nowDate_Date = null;
            Date endDate_Date = null;
            try {
                nowDate_Date = sdf.parse(o1);
                endDate_Date = sdf.parse(o2);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return nowDate_Date.compareTo(endDate_Date);
        });
        Map<String, List<String>> querySwc = new TreeMap<>((Comparator<String>) (o1, o2) -> {
            //指定排序器按照降序排列
            Date nowDate_Date = null;
            Date endDate_Date = null;
            try {
                nowDate_Date = sdf.parse(o1);
                endDate_Date = sdf.parse(o2);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return nowDate_Date.compareTo(endDate_Date);
        });
        Map<String, List<String>> queryVpd = new TreeMap<>((Comparator<String>) (o1, o2) -> {
            //指定排序器按照降序排列
            Date nowDate_Date = null;
            Date endDate_Date = null;
            try {
                nowDate_Date = sdf.parse(o1);
                endDate_Date = sdf.parse(o2);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return nowDate_Date.compareTo(endDate_Date);
        });
        //构造json返回值类型
        for (ForecastResult fr : forecastList) {
            // 只筛选60天内的预测结果
            if (LocalDateTime.now().isBefore(fr.getCreateTime().plusDays(66))) {
                if (queryTa.get(fr.getYear() + "-" + fr.getMonth() +"-" + fr.getDay()) != null) {
                    queryPar.get(fr.getYear() + "-" + fr.getMonth() +"-" + fr.getDay()).add(fr.getPar());
                    queryRh.get(fr.getYear() + "-" + fr.getMonth() +"-" + fr.getDay()).add(fr.getRh());
                    querySwc.get(fr.getYear() + "-" + fr.getMonth() +"-" + fr.getDay()).add(fr.getSwc());
                    queryVpd.get(fr.getYear() + "-" + fr.getMonth() +"-" + fr.getDay()).add(fr.getVpd());
                    queryTa.get(fr.getYear() + "-" + fr.getMonth() +"-" + fr.getDay()).add(fr.getTa());
                } else {
                    List<String> listTa = new ArrayList<>();
                    List<String> listPar = new ArrayList<>();
                    List<String> listRh = new ArrayList<>();
                    List<String> listSwc = new ArrayList<>();
                    List<String> listVpd = new ArrayList<>();
                    listTa.add(fr.getTa());
                    listPar.add(fr.getPar());
                    listRh.add(fr.getRh());
                    listSwc.add(fr.getSwc());
                    listVpd.add(fr.getVpd());
                    queryTa.put(fr.getYear() + "-" + fr.getMonth() +"-" + fr.getDay(), listTa);
                    queryPar.put(fr.getYear() + "-" + fr.getMonth() +"-" + fr.getDay(), listPar);
                    queryRh.put(fr.getYear() + "-" + fr.getMonth() +"-" + fr.getDay(), listRh);
                    querySwc.put(fr.getYear() + "-" + fr.getMonth() +"-" + fr.getDay(), listSwc);
                    queryVpd.put(fr.getYear() + "-" + fr.getMonth() +"-" + fr.getDay(), listVpd);
                }
            }
        }
        // 向晖需要的返回类型 构造  ,如何确定需求，可以优化为一个对象直接 json化，暂时这样
        //构建Ta
        JSONObject jsonObjectTa = new JSONObject();
        ArrayList<String> jsonDateTa = new ArrayList<>();
        ArrayList<List<String>> jsonValueTa = new ArrayList<>();
        ArrayList<String> jsonAverageTa = new ArrayList<>();
        jsonObjectTa.put("name", "Ta");
        for (String key: queryTa.keySet()) {
//            if (queryTa.get(key).size() > 5) {
//                continue;
//            }
            jsonDateTa.add(key);
            jsonValueTa.add(queryTa.get(key));
            if(null!=actualMap.get(key)){
                double[] array = queryTa.get(key).stream().mapToDouble(Double::parseDouble).toArray();
                jsonAverageTa.add( String.valueOf(findClosestValue(array,Double.parseDouble(actualMap.get(key).getTa()))));
            }else {
                double sum = 0;
                double count = 0;
                for (int i = 0; i < queryTa.get(key).size(); i++) {
                    sum += Double.parseDouble(queryTa.get(key).get(i));
                    count++;
                }
                jsonAverageTa.add( (sum / count) + "");
            }

        }
        jsonObjectTa.put("date", jsonDateTa);
        jsonObjectTa.put("value", jsonValueTa);
        jsonObjectTa.put("averageValue", jsonAverageTa);
        //Par
        JSONObject jsonObjectPar = new JSONObject();
        ArrayList<String> jsonDatePar = new ArrayList<>();
        ArrayList<List<String>> jsonValuePar = new ArrayList<>();
        ArrayList<String> jsonAveragePar = new ArrayList<>();
        jsonObjectPar.put("name", "Par");
        for (String key: queryPar.keySet()) {
//            if (queryPar.get(key).size() > 5) {
//                continue;
//            }
            jsonDatePar.add(key);
            jsonValuePar.add(queryPar.get(key));
            //修改需求  不要平均值  取与实际值最接近的预测值
            if(null!=actualMap.get(key)){
                double[] array = queryPar.get(key).stream().mapToDouble(Double::parseDouble).toArray();
                jsonAveragePar.add( String.valueOf(findClosestValue(array,Double.parseDouble(actualMap.get(key).getPar()))));
            }else {
                double sum = 0;
                double count = 0;
                for (int i = 0; i < queryPar.get(key).size(); i++) {
                    sum += Double.parseDouble(queryPar.get(key).get(i));
                    count++;
                }
                jsonAveragePar.add( (sum / count) + "");
            }
        }
        jsonObjectPar.put("date", jsonDatePar);
        jsonObjectPar.put("value", jsonValuePar);
        jsonObjectPar.put("averageValue", jsonAveragePar);
        //Rh
        JSONObject jsonObjectRh = new JSONObject();
        ArrayList<String> jsonDateRh = new ArrayList<>();
        ArrayList<List<String>> jsonValueRh = new ArrayList<>();
        ArrayList<String> jsonAverageRh = new ArrayList<>();
        jsonObjectRh.put("name", "Rh");
        for (String key: queryRh.keySet()) {
//            if (queryRh.get(key).size() > 5) {
//                continue;
//            }
            jsonDateRh.add(key);
            jsonValueRh.add(queryRh.get(key));
            //修改需求  不要平均值  取与实际值最接近的预测值
            if(null!=actualMap.get(key)){
                double[] array = queryRh.get(key).stream().mapToDouble(Double::parseDouble).toArray();
                jsonAverageRh.add( String.valueOf(findClosestValue(array,Double.parseDouble(actualMap.get(key).getRh()))));
            }else {
                double sum = 0;
                double count = 0;
                for (int i = 0; i < queryRh.get(key).size(); i++) {
                    sum += Double.parseDouble(queryRh.get(key).get(i));
                    count++;
                }
                jsonAverageRh.add( (sum / count) + "");
            }
        }
        jsonObjectRh.put("date", jsonDateRh);
        jsonObjectRh.put("value", jsonValueRh);
        jsonObjectRh.put("averageValue", jsonAverageRh);
        //Swc
        JSONObject jsonObjectSwc = new JSONObject();
        ArrayList<String> jsonDateSwc = new ArrayList<>();
        ArrayList<List<String>> jsonValueSwc = new ArrayList<>();
        ArrayList<String> jsonAverageSwc = new ArrayList<>();
        jsonObjectSwc.put("name", "Swc");
        for (String key: querySwc.keySet()) {
//            if (querySwc.get(key).size() > 5) {
//                continue;
//            }
            jsonDateSwc.add(key);
            jsonValueSwc.add(querySwc.get(key));
            //修改需求  不要平均值  取与实际值最接近的预测值
            if(null!=actualMap.get(key)){
                double[] array = querySwc.get(key).stream().mapToDouble(Double::parseDouble).toArray();
                jsonAverageSwc.add( String.valueOf(findClosestValue(array,Double.parseDouble(actualMap.get(key).getSwc()))));
            }else {
                double sum = 0;
                double count = 0;
                for (int i = 0; i < querySwc.get(key).size(); i++) {
                    sum += Double.parseDouble(querySwc.get(key).get(i));
                    count++;
                }
                jsonAverageSwc.add( (sum / count) + "");
            }
        }
        jsonObjectSwc.put("date", jsonDateSwc);
        jsonObjectSwc.put("value", jsonValueSwc);
        jsonObjectSwc.put("averageValue", jsonAverageSwc);
        //Vpd
        JSONObject jsonObjectVpd = new JSONObject();
        ArrayList<String> jsonDateVpd = new ArrayList<>();
        ArrayList<List<String>> jsonValueVpd = new ArrayList<>();
        ArrayList<Double> jsonAverageVpd = new ArrayList<>();
        jsonObjectVpd.put("name", "Vpd");
        for (String key: queryVpd.keySet()) {
//            if (queryVpd.get(key).size() > 5) {
//                continue;
//            }
            jsonDateVpd.add(key);
            jsonValueVpd.add(queryVpd.get(key));
            //修改需求  不要平均值  取与实际值最接近的预测值
            if(null!=actualMap.get(key)){
                double[] array = queryVpd.get(key).stream().mapToDouble(Double::parseDouble).toArray();
                jsonAverageVpd.add( findClosestValue(array,Double.parseDouble(actualMap.get(key).getVpd())));
            }else {
                double sum = 0;
                double count = 0;
                for (int i = 0; i < queryVpd.get(key).size(); i++) {
                    sum += Double.parseDouble(queryVpd.get(key).get(i));
                    count++;
                }
                jsonAverageVpd.add( (sum / count));
            }
        }
        jsonObjectVpd.put("date", jsonDateVpd);
        jsonObjectVpd.put("value", jsonValueVpd);
        jsonObjectVpd.put("averageValue", jsonAverageVpd);
        // 最终结果
        JSONArray jsonObject = new JSONArray();
        jsonObject.add(jsonObjectTa);
        jsonObject.add(jsonObjectPar);
        jsonObject.add(jsonObjectRh);
        jsonObject.add(jsonObjectSwc);
        jsonObject.add(jsonObjectVpd);
        return jsonObject;
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
