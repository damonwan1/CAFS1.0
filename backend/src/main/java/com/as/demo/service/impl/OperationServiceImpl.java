package com.as.demo.service.impl;

import java.io.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;


import cn.hutool.core.date.DateField;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.as.demo.entity.ForecastResult;
import com.as.demo.entity.ZoologyHistoryDrive;
import com.as.demo.mapper.ForecastResultMapper;
import com.as.demo.mapper.ZoologyHistoryDriveMapper;
import com.as.demo.service.*;
import com.as.demo.utils.ErrorCorrectionUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.as.demo.entity.HistoryDrive;
import com.as.demo.entity.Operation;
import com.as.demo.entity.dto.ActivityDto;
import com.as.demo.entity.dto.ActivityItemMeasureDto;
import com.as.demo.mapper.HistoryDriveMapper;
import com.as.demo.mapper.OperationMapper;
import com.as.demo.utils.RemoteExecuteCommandutil;
import com.as.demo.utils.ThirdDataUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 操作记录表 服务实现类
 * </p>
 *
 * @author yule
 * @since 2021-05-18
 */
@Slf4j
@Service
public class OperationServiceImpl extends ServiceImpl<OperationMapper, Operation> implements IOperationService {
    @Value("${prediction_day_number}")
    private int day_number;
    @Autowired
    private RemoteExecuteCommandutil remoteExecuteCommandutil;
    @Autowired
    private IShowResultService iShowResultService;

    @Autowired
    private FileTransferService fileTransferService;

    @Autowired
    private IHistoryDriveService iHistoryDriveService;
    @Autowired
    private IForecastResultService iForecastResultService;

    @Autowired
    private ThirdDataUtil thirdDataUtil;
    @Autowired
    private HistoryDriveMapper historyDriveMapper;

    @Autowired
    private OperationMapper operationMapper;

    @Autowired
    private ZoologyHistoryDriveMapper zoologHistoryDriveMapper;

    @Autowired
    private IZoologyHistoryDriveService iZoologyHistoryDriveService;

    public String initSystem() {
        Assert.isTrue(iHistoryDriveService.exportCsv("DHF"), "export fail");

        return "";
    }


    @Override
    // 返回前端地图所有的结果
    public List<Operation> allstation() {
        //操作表中所有open为1的数据
        List<Operation> operationALL = operationMapper.selectList(
                Wrappers.<Operation>lambdaQuery()
                        .eq(true, Operation::getOpen, "1")
        );
        return operationALL;
    }

    @Override
    public Boolean mockNoRunDaySay(LocalDateTime createTime) {

            //声明需要格式化的格式(日期)
        DateTimeFormatter dfDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        log.info("导出全部历史csv");
        Assert.isTrue(iHistoryDriveService.exportCsv("DHF",dfDate.format(createTime)), "导出全部历史csv失败");
        log.info("上传csv到服务器");
        Assert.isTrue(fileTransferService.uploadFile(
                "/tmp/data-DHF.csv",
                "/root/csv/codefile/data/model_input_DHF_to_2012.csv"),
                "上传csv到服务器失败"
        );
        log.info("运行预测镜像");
        remoteExecuteCommandutil.execute(
                "docker run -d --rm --name prediction  -v /root/csv/codefile/:/root/codefile prediction:latest "
                        + "/root/codefile/run.sh");
        try {
            log.info("等待3秒");
            Thread.sleep(3000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String result = "prediction";
        while (result.contains("prediction")) {
            log.info("判断预测数据csv是否生成");
            result = remoteExecuteCommandutil.execute("docker  ps --filter name=prediction");
            try {
                if (result.contains("prediction")) {
                    log.info("等待10秒,再次判断预测数据csv是否生成");
                    Thread.sleep(10000L);
                } else {
                    log.info("预测数据csv已生成");
                }
            } catch (InterruptedException e) {
                log.info("异常中断");
                e.printStackTrace();
            }
        }
        log.info("下载预测数据csv");
        Assert.isTrue(fileTransferService.downloadFile(
                "/tmp/forecast.csv",
                "/root/csv/codefile/pred_result.csv"
                ),
                "下载预测数据csv失败");
        log.info("导入预测数据");
        Assert.isTrue(iForecastResultService.importCsv("/tmp/forecast.csv", createTime), "导入预测数据失败");
        log.info("移动预测数据csv到/root/xuq/driver.csv");
        remoteExecuteCommandutil.execute("cp -f /root/csv/codefile/pred_result.csv /root/xuq/driver.csv");
        log.info("运行第二个镜像");
        remoteExecuteCommandutil.execute(
                "docker run --name tonghua --rm -v /root/xuq:/root/xuq tonghua:latest python /root/xuq/DALEC-DHF.py");
        String secResult = "tonghua";
        while (secResult.contains("tonghua")) {
            log.info("判断正演数据csv是否生成");
            secResult = remoteExecuteCommandutil.execute("docker  ps --filter name=tonghua");
            try {
                if (secResult.contains("tonghua")) {
                    log.info("等待10秒,再次判断正演数据csv是否生成");
                    Thread.sleep(10000L);
                } else {
                    log.info("正演数据csv已生成");
                }
            } catch (InterruptedException e) {
                log.info("异常中断");
                e.printStackTrace();
            }
        }
        log.info("下载正演数据csv");
        Assert.isTrue(fileTransferService.downloadFile("/tmp/tonghua.csv", "/root/xuq/out.csv"),
                "下载正演数据csv失败");
        log.info("导入正演数据");
        Assert.isTrue(iShowResultService.importCsv("/tmp/tonghua.csv", createTime), "导入正演数据失败");
        log.info("完成");
        return true;
    }

    @Override
    public Boolean mockNoRunDaySay(LocalDateTime createTime, String sitecode) {

        //声明需要格式化的格式(日期)
        DateTimeFormatter dfDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        log.info("导出全部历史csv");
        Assert.isTrue(iHistoryDriveService.exportCsv(sitecode,dfDate.format(createTime)), "导出全部历史csv失败");
        log.info("上传csv到服务器");
        Assert.isTrue(fileTransferService.uploadFile(
                "/tmp/data-"+sitecode+".csv",
                "/root/csv/codefile-"+sitecode+"/model_input_"+sitecode+".csv"),
                "上传csv到服务器失败"
        );
        log.info("运行预测镜像");
        remoteExecuteCommandutil.execute(
                "docker run -d --rm --name prediction-"+sitecode+"  -v /root/csv/codefile-"+sitecode+"/:/root/codefile prediction:latest "
                        + "/root/codefile/run.sh");
        try {
            log.info("等待3秒");
            Thread.sleep(3000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String result = "prediction-"+sitecode;
        while (result.contains("prediction-"+sitecode)) {
            log.info("判断预测数据csv是否生成");
            result = remoteExecuteCommandutil.execute("docker  ps --filter name=prediction-"+sitecode);
            try {
                if (result.contains("prediction-"+sitecode)) {
                    log.info("等待10秒,再次判断预测数据csv是否生成");
                    Thread.sleep(10000L);
                } else {
                    log.info("预测数据csv已生成");
                }
            } catch (InterruptedException e) {
                log.info("异常中断");
                e.printStackTrace();
            }
        }
        log.info("下载预测数据csv");
        Assert.isTrue(fileTransferService.downloadFile(
                "/tmp/forecast.csv",
                "/root/csv/codefile/pred_result.csv"
                ),
                "下载预测数据csv失败");
        log.info("导入预测数据");
        Assert.isTrue(iForecastResultService.importCsv("/root/csv/codefile-"+sitecode+"/pred_result.csv", createTime, 67), "导入预测数据失败");
        log.info("移动预测数据csv到/root/xuq/driver.csv");
        remoteExecuteCommandutil.execute("cp -f /root/csv/codefile-"+sitecode+"/pred_result.csv /root/xuq/"+sitecode+"/driver.csv");
        log.info("运行第二个镜像");
        remoteExecuteCommandutil.execute(
                "docker run --name tonghua-"+sitecode+" --rm -v /root/xuq/"+sitecode+":/root/xuq tonghua:latest python /root/xuq/DALEC-"+sitecode+".py");
        String secResult = "tonghua-"+sitecode;
        while (secResult.contains("tonghua-"+sitecode)) {
            log.info("判断正演数据csv是否生成");
            secResult = remoteExecuteCommandutil.execute("docker  ps --filter name=tonghua-"+sitecode);
            try {
                if (secResult.contains("tonghua-"+sitecode)) {
                    log.info("等待10秒,再次判断正演数据csv是否生成");
                    Thread.sleep(10000L);
                } else {
                    log.info("正演数据csv已生成");
                }
            } catch (InterruptedException e) {
                log.info("异常中断");
                e.printStackTrace();
            }
        }
        log.info("下载正演数据csv");

        log.info("导入正演数据");
        Assert.isTrue(iShowResultService.importCsv("/root/xuq/"+sitecode+"/out.csv", createTime, 90), "导入正演数据失败");
        log.info("完成");
        return true;
    }

    @Override
    // 每天执行一次的工作流流程
    /**
     * 鼎湖山定时任务
     */
    public Boolean say() {
        log.info("导出全部历史csv");
        // 导出到/tmp/data.csv
        Assert.isTrue(iHistoryDriveService.exportCsv("DHF"), "导出全部历史csv失败");
        log.info("上传csv到服务器");
        // ****** 加一个代码路径
        Assert.isTrue(fileTransferService.uploadFile(
                    "/tmp/data-DHF.csv",
                "/root/csv/codefile2/model_input_DHF_to_2012.csv"),
                "上传csv到服务器失败"
        );
        log.info("运行预测镜像");

        // ****** 循环
        remoteExecuteCommandutil.execute(
                "docker run -d --rm --name prediction  -v /root/csv/codefile2/:/root/codefile prediction:latest "
                        + "/root/codefile/run.sh");
        try {
            log.info("等待5秒");
            Thread.sleep(10000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String result = "prediction";
        while (result.contains("prediction")) {
            log.info("判断预测数据c是否生成");
            result = remoteExecuteCommandutil.execute("docker  ps --filter name=prediction");
            try {
                if (result.contains("prediction")) {
                    log.info("等待10秒,再次判断预测数据csv是否生成");
                    Thread.sleep(10000L);
                } else {
                    log.info("预测数据csv已生成");
                }
            } catch (InterruptedException e) {
                log.info("异常中断");
                e.printStackTrace();
            }
        }
        log.info("下载预测数据csv");
//        Assert.isTrue(fileTransferService.downloadFile(
//                "/tmp/forecast.csv",
//                "/root/csv/codefile2/pred_result.csv"
//                ),
//                "下载预测数据csv失败");

        log.info("导入预测数据");
        // 有bug，只导入最新生成的 day_number 天预测即可，不用导入全部数据
        Assert.isTrue(iForecastResultService.importCsv("/root/csv/codefile2/pred_result.csv", 67), "导入预测数据失败");

        // pred_result.csv  卫华生成的这个放到/xuq/driver_DHF.csv     /root/csv/codefile/
        // obs.csv 把第一列先手动填充到前一天，然后自动的把每天的值放进来
        // 直接运行程序,大概要8个小时之类，执行python DHF_Metropolis.py ,生成的文件: simresult_out.csv
        // simresult_out.csv  过去2个月+未来7天
        log.info("移动预测数据csv到/root/shanrui/DHF/driver.csv");
        remoteExecuteCommandutil.execute("cp -f /root/csv/codefile2/pred_result.csv /root/shanrui/DHF/driver_DHF.csv");
        //log.info("obs.csv 填充最新一天的数据");
        //remoteExecuteCommandutil.execute("cp -f /root/tonghua2022/DHF/obs_base.csv /root/tonghua2022/DHF/obs.csv");
        log.info("运行第二个同化镜像");
        // ****** 循环
        remoteExecuteCommandutil.execute(
                "docker run -d --rm --name shanruiDHF -v /root/shanrui/DHF/:/root/xuq/ tonghua:shanrui "
                        + "/root/xuq/run.sh");
        try {
            log.info("等待5秒");
            Thread.sleep(50000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        result = "shanruiDHF";
        while (result.contains("shanruiDHF")) {
            log.info("判断同化数据csv是否生成");
            result = remoteExecuteCommandutil.execute("docker  ps --filter name=shanruiDHF");
            try {
                if (result.contains("shanruiDHF")) {
                    log.info("等待1200秒,再次判断预测数据csv是否生成");
                    Thread.sleep(1200000L);
                } else {
                    log.info("同化数据csv已生成");
                }
            } catch (InterruptedException e) {
                log.info("异常中断");
                e.printStackTrace();
            }
        }
        log.info("下载同化正演数据csv");
//        Assert.isTrue(fileTransferService.downloadFile(
//                "/tmp/fanyan-DHF.csv",
//                "/root/shanrui/DHF/simresult_out.csv"
//                ),
//                "下载预测数据csv失败");


        log.info("导入同化+正演数据");
        Assert.isTrue(iShowResultService.importCsv("/root/shanrui/DHF/simresult_out.csv", LocalDateTime.now()), "导入正演数据失败");
        Assert.isTrue(iShowResultService.importStatistics("/root/shanrui/DHF/DHF_simresult_out_all.csv", LocalDateTime.now()), "导入正演统计数据失败");
        remoteExecuteCommandutil.execute("rm -rf /root/shanrui/DHF/DHF_simresult_out_all_bak.csv");
        remoteExecuteCommandutil.execute("mv /root/shanrui/DHF/DHF_simresult_out_all.csv /root/shanrui/DHF/DHF_simresult_out_all_bak.csv");
        log.info("完成");
//        // 有bug，只导入最新生成的 day_number 天预测即可，不用导入全部数据
//        Assert.isTrue(iForecastResultService.importCsv("/root/shanrui/DHF/simresult_out.csv", 7), "导入预测数据失败");
//        log.info("移动预测数据csv到/root/xuq/DHF/driver.csv");
//        remoteExecuteCommandutil.execute("cp -f /root/shanrui/DHF/simresult_out.csv /root/xuq/DHF/driver.csv");
//        log.info("运行第三个镜像");
//        remoteExecuteCommandutil.execute(
//                "docker run --name tonghua --rm -v /root/xuq/DHF:/root/xuq tonghua:latest python /root/xuq/DALEC-DHF.py");
//        String secResult = "tonghua";
//        while (secResult.contains("tonghua")) {
//            log.info("判断正演数据csv是否生成");
//            secResult = remoteExecuteCommandutil.execute("docker  ps --filter name=tonghua");
//            try {
//                if (secResult.contains("tonghua")) {
//                    log.info("等待10秒,再次判断正演数据csv是否生成");
//                    Thread.sleep(10000L);
//                } else {
//                    log.info("正演数据csv已生成");
//                }
//            } catch (InterruptedException e) {
//                log.info("异常中断");
//                e.printStackTrace();
//            }
//        }
//        log.info("下载正演数据csv");
////        Assert.isTrue(fileTransferService.downloadFile("/tmp/tonghua.csv", "/root/xuq/out.csv"),
////                "下载正演数据csv失败");
//        log.info("导入正演数据");
//        // 每天都执行，会把12年-当天 **所有的**正演完成的数据给存储到show_result表中（评估有没有必要）
//        //到时可以这样，因为其实看一下发现最终正演的数据都是假的
//        Assert.isTrue(iShowResultService.importCsv("/root/xuq/DHF/out.csv", LocalDateTime.now()), "导入正演数据失败");
//        log.info("完成");
        return true;
    }

    //向文件（fileName）中添加一行，这一行的数据是data
    public boolean fileAddItem(File fileName, String data) throws IOException {
        //打开输出文件流
        FileOutputStream fos = new FileOutputStream(fileName,true); // true 表示在后面追加，不加 true 默认表示覆盖原来的数据
        OutputStreamWriter osw = new OutputStreamWriter(fos, "gbk");
        //创建字符串缓存
        BufferedWriter bw = new BufferedWriter(osw);
        StringBuffer str = new StringBuffer();
        //添加要写入的数据
        str.append(data);

        //表示写三行
        bw.write(str.toString());
        //关闭文件流
        bw.flush();
        osw.flush();
        fos.flush();
        return true;
    }


    @Override
    public Boolean data(String start, String end) {
        // 删除对应的数据
        historyDriveMapper.deleteOld(start, end);
        //删除对应的生态数据
        zoologHistoryDriveMapper.deleteOld(start,end);
        DateTime startDate = DateUtil.parse(start);
        DateTime endDate = DateUtil.parse(end);
        long betweenDay = DateUtil.between(startDate, endDate, DateUnit.DAY);
        List<HistoryDrive> list = new ArrayList<>();
        List<ZoologyHistoryDrive> list2 = new ArrayList<>();
        // 遍历start到end日期内的所有数据
        // 如果是真实的，就获取真实的
        // 如果获取不到台站真实数据，拿前3天平均值替代，并用mock字段标注为mock
        // 存储在historyDriver表中
        for (int i = 0; i <= betweenDay; i++) {
            HistoryDrive historyDrive = new HistoryDrive();
            DateTime time = DateUtil.offsetDay(startDate, i);
            historyDrive.setSiteCode("DHF");
            historyDrive.setYear(time.year());
            historyDrive.setMonth(time.monthStartFromOne());
            historyDrive.setDay(time.dayOfMonth());
            historyDrive.setYear(time.year());
            historyDrive.setCreateTime(LocalDateTime.now());
            historyDrive.setMock(false);


            //生态数据实体类
            ZoologyHistoryDrive zoologyHistoryDrive = new ZoologyHistoryDrive();
            zoologyHistoryDrive.setSiteCode("DHF");
            zoologyHistoryDrive.setYear(time.year());
            zoologyHistoryDrive.setMonth(time.monthStartFromOne());
            zoologyHistoryDrive.setDay(time.dayOfMonth());
            zoologyHistoryDrive.setYear(time.year());
            zoologyHistoryDrive.setCreateTime(LocalDateTime.now());
            zoologyHistoryDrive.setMock(false);
            try {
                // 获取该次遍历日期的各项真实数值（TA\RH\PAR\SWC）
                ActivityDto activityConfig = thirdDataUtil.getActivityConfig().get(0);

                activityConfig.getActivityMeasureList().forEach(ele -> {
                    String measureCodeList = ele.getMeasureList().stream().map(ActivityItemMeasureDto::getMeasureCode)
                            .collect(Collectors.joining(","));

                    HashMap<String, String> activityData =
                            thirdDataUtil.getActivityData(measureCodeList, String.valueOf(time.getTime()),
                                    ele.getActivityCode());

                    for (String key : activityData.keySet()) {
                        log.info("key:"+key);
                        log.info("value:"+activityData.get(key));
                        if (key.equals("Ta_Avg")) {
                            // 数值订正
                            if (stringToDouble(activityData.get(key)) >3 && stringToDouble(activityData.get(key)) <35 ) {
                                historyDrive.setTa(activityData.get(key));
                            }
                        } else if (key.equals("RH_Avg")) {
                            if (stringToDouble(activityData.get(key)) >20 && stringToDouble(activityData.get(key)) <100 ) {
                                historyDrive.setRh(activityData.get(key));
                            }
                        } else if (key.equals("PAR_Down_Avg")) {
                            if (stringToDouble(activityData.get(key)) >0 && stringToDouble(activityData.get(key)) <61 ) {
                                historyDrive.setPar(activityData.get(key));
                            }
                        } else if (key.equals("par_Avg")) {
                            if (stringToDouble(activityData.get(key)) >0 && stringToDouble(activityData.get(key)) <60 ) {
                                historyDrive.setPar(activityData.get(key));
                            }
                        }
                        else if (key.equals("VWC_10cm_Avg")) {
                            if (stringToDouble(activityData.get(key)) >0.1 && stringToDouble(activityData.get(key)) <0.4 ) {
                                historyDrive.setSwc(activityData.get(key));
                            }
                        } else if (key.equals("Fc_mass")) {
                            String fcmassvalue="";
                            if (stringToDouble(activityData.get(key)) >-10 && stringToDouble(activityData.get(key)) <10 ) {
                                fcmassvalue = activityData.get(key);
                            } else {
                                fcmassvalue = "-9999";
                            }
                            // 这个值NEE不存数据库，直接存csv中即可
                            remoteExecuteCommandutil.execute("echo "+fcmassvalue + ",-9999,-9999,-9999,-9999,-9999,-9999," + DateUtil.format(time, "MM/dd/yyyy")+" >> /root/shanrui/DHF/obs.csv");
                            //fileAddItem(new File("/root/shanrui/DHF/obs.csv"),activityData.get(key) + ",-9999,-9999,-9999,-9999,-9999,-9999," + DateUtil.format(time, "MM/dd/yyyy"));
                        } else if (key.equals("ts")) {
                            // 时间戳转日期
                            long lt = new Long(activityData.get(key));
                            int days = Math.abs((int) ((time.getTime() - new Date(lt).getTime()) / (1000 * 3600 * 24)));
                            if (days > 2) {
                                historyDrive.setMock(true);
                            }
                        }else if(key.equals("Fc_molar")){
                            //2023/06/07 增加nep实际值
                            BigDecimal bd1 = new BigDecimal(activityData.get(key));
                            if(bd1.compareTo(new BigDecimal(-15))>=0 && bd1.compareTo(new BigDecimal(15))<=0){
                                zoologyHistoryDrive.setNep(bd1.negate().toString());
                            }
                        }


                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            // 如果没取到真实数据，则使用3天平均值来mock数据代替
            // 取3天中的平均值,赋值
            List<HistoryDrive> historyDrives = historyDriveMapper.historySameTime(time.monthStartFromOne(),time.dayOfMonth(),"DHF");

            List<ZoologyHistoryDrive> zoologyHistoryDriveList = zoologHistoryDriveMapper.historySameTime(time.monthStartFromOne(), time.dayOfMonth(), "DHF");


            // 新增需求：对比这一天的数据 如果和前一天一模一样 ，证明是假数据，需要替换历史同期
            // 读取前一天的数据
            HistoryDrive oldHistoryDriver = new HistoryDrive();
            if (list.size() >= 1) {
                oldHistoryDriver = list.get(list.size()-1);
            } else {
                String oldDat = DateUtil.format(DateUtil.offsetDay(time, -1), "yyyy-MM-dd");
                List<HistoryDrive> oldhistoryDrives = historyDriveMapper.listByCode("DHF",oldDat, oldDat);
                if (!CollectionUtils.isEmpty(oldhistoryDrives)) {
                    oldHistoryDriver = oldhistoryDrives.get(0);
                }
            }


            ZoologyHistoryDrive odlZoologyHistoryDrive=new ZoologyHistoryDrive();
            if (list2.size() >= 1) {
                odlZoologyHistoryDrive = list2.get(list.size()-1);
            } else {
                String oldDat = DateUtil.format(DateUtil.offsetDay(time, -1), "yyyy-MM-dd");
                List<ZoologyHistoryDrive> zoologyHistoryDrives = zoologHistoryDriveMapper.listByCode("DHF",oldDat, oldDat);
                if(!CollectionUtils.isEmpty(zoologyHistoryDrives)){
                    odlZoologyHistoryDrive = zoologyHistoryDrives.get(0);
                }
            }

            // 历史同期没有   取近三天
            if (historyDrives == null || historyDrives.size() == 0) {
                String avgStart = DateUtil.format(DateUtil.offsetDay(time, -3), "yyyy-MM-dd");
                String avgEnd = DateUtil.format(time, "yyyy-MM-dd");
                if (list.size() >= 3) {
                    //判断list长度 只保留最后两位
                    for(int l=list.size()-1;l>list.size()-4;l--) {
                        historyDrives.add(list.get(l));
                    }
                } else {
                    historyDrives = historyDriveMapper.listByCode("DHF",avgStart, avgEnd);
                }
            }



            // 历史同期没有   取近三天
            if (zoologyHistoryDriveList == null || zoologyHistoryDriveList.size() == 0) {
                String avgStart = DateUtil.format(DateUtil.offsetDay(time, -3), "yyyy-MM-dd");
                String avgEnd = DateUtil.format(time, "yyyy-MM-dd");
                if (list2.size() >= 3) {
                    //判断list长度 只保留最后两位
                    for(int l=list2.size()-1;l>list2.size()-4;l--) {
                        zoologyHistoryDriveList.add(list2.get(l));
                    }
                } else {
                    zoologyHistoryDriveList = zoologHistoryDriveMapper.listByCode("DHF",avgStart, avgEnd);
                }
            }



            // 根据前3天的均值来mock数据 : Ta\Rh\Par\Swc
            if (historyDrive.getTa() == null || historyDrive.getMock()|| (oldHistoryDriver.getTa()!=null && historyDrive.getTa().substring(0,7).equals(oldHistoryDriver.getTa().substring(0,7)))) {
                historyDrive.setTa(String.valueOf(ErrorCorrectionUtil.lastAvgTa(historyDrives)));
                log.info("Ta is mock");
                historyDrive.setMock(true);
            }
            if (historyDrive.getRh() == null || historyDrive.getMock() || (oldHistoryDriver.getRh()!=null && historyDrive.getRh().substring(0,7).equals(oldHistoryDriver.getRh().substring(0,7)))) {
                historyDrive.setRh(String.valueOf(ErrorCorrectionUtil.lastAvgRH(historyDrives)));

                log.info("Rh is mock");
                historyDrive.setMock(true);
            }
            if (historyDrive.getPar() == null || historyDrive.getMock() || (oldHistoryDriver.getMock()!=null && historyDrive.getPar().substring(0,7).equals(oldHistoryDriver.getPar().substring(0,7)))) {
                historyDrive.setPar(String.valueOf(ErrorCorrectionUtil.lastAvgPAR(historyDrives)));

                log.info("Par is mock");
                historyDrive.setMock(true);
            }
            if (historyDrive.getSwc() == null || historyDrive.getMock() || (oldHistoryDriver.getSwc()!=null && historyDrive.getSwc().substring(0,7).equals(oldHistoryDriver.getSwc().substring(0,7)))) {
                historyDrive.setSwc(String.valueOf(ErrorCorrectionUtil.lastAvgSWC(historyDrives)));
                log.info("Swc is mock");
                historyDrive.setMock(true);
            }
            // 如果还为空，置为0
            if (historyDrive.getSwc() == null || "".equals(historyDrive.getSwc()) ) {
                historyDrive.setSwc("0");
            }
            if (zoologyHistoryDrive.getNep() == null || zoologyHistoryDrive.getMock() || ((((!StringUtils.isEmpty(odlZoologyHistoryDrive.getNep()))&&(odlZoologyHistoryDrive.getNep().length()>=7))&&(odlZoologyHistoryDrive.getNep().substring(0,7).equals(odlZoologyHistoryDrive.getNep().substring(0,7)))))) {
                zoologyHistoryDrive.setNep(String.valueOf(ErrorCorrectionUtil.lastAvgNEP(zoologyHistoryDriveList)));
                log.info("NEP is mock");
                zoologyHistoryDrive.setMock(true);
            }
            // 将记录加入list
            list.add(historyDrive);
            list2.add(zoologyHistoryDrive);
        }
        iHistoryDriveService.saveBatch(list);
        iZoologyHistoryDriveService.saveBatch(list2);
        return true;
    }

    /**
     * String转换成double 保留N位⼩数。
     * @param a
     * @return
     */
    public static double stringToDouble(String a){
        double b = Double.valueOf(a);
        DecimalFormat df = new DecimalFormat("#.000000000");//此为保留1位⼩数，若想保留2位⼩数，则填写#.00  ，以此类推
        String temp = df.format(b);
        b = Double.valueOf(temp);
        return b;
    }


    @Override
    public Boolean data(String start, String end, String obsfile, int mode) {
        // 删除对应的数据
        System.out.println("Enter Function data:");
        historyDriveMapper.deleteOld(start, end);
        DateTime startDate = DateUtil.parse(start);
        DateTime endDate = DateUtil.parse(end);
        long betweenDay = DateUtil.between(startDate, endDate, DateUnit.DAY);
        List<HistoryDrive> list = new ArrayList<>();
        // 遍历start到end日期内的所有数据
        // 如果是真实的，就获取真实的
        // 如果获取不到台站真实数据，拿前3天平均值替代，并用mock字段标注为mock
        // 存储在historyDriver表中
        for (int i = 0; i <= betweenDay; i++) {
            System.out.println("The "+i +"time of New historydriver from Station");
            HistoryDrive historyDrive = new HistoryDrive();
            DateTime time = DateUtil.offsetDay(startDate, i);
            historyDrive.setSiteCode("DHF");
            historyDrive.setYear(time.year());
            historyDrive.setMonth(time.monthStartFromOne());
            historyDrive.setDay(time.dayOfMonth());
            historyDrive.setYear(time.year());
            historyDrive.setCreateTime(LocalDateTime.now());
            historyDrive.setMock(false);
            try {
                // 获取该次遍历日期的各项真实数值（TA\RH\PAR\SWC）
                ActivityDto activityConfig = thirdDataUtil.getActivityConfig().get(0);

                activityConfig.getActivityMeasureList().forEach(ele -> {
                    String measureCodeList = ele.getMeasureList().stream().map(ActivityItemMeasureDto::getMeasureCode)
                            .collect(Collectors.joining(","));
                    log.info("measureCodeList:"+measureCodeList);

                    HashMap<String, String> activityData =
                            thirdDataUtil.getActivityData(measureCodeList, String.valueOf(time.getTime()),
                                    ele.getActivityCode());

                    for (String key : activityData.keySet()) {
                        log.info("key:"+key);
                        log.info("value:"+activityData.get(key));
                        if (key.equals("Ta_Avg")) {
                            // 数值订正
                            if (stringToDouble(activityData.get(key)) >3 && stringToDouble(activityData.get(key)) <35 ) {
                                historyDrive.setTa(activityData.get(key));
                            }
                        } else if (key.equals("RH_Avg")) {
                            if (stringToDouble(activityData.get(key)) >20 && stringToDouble(activityData.get(key)) <100 ) {
                                historyDrive.setRh(activityData.get(key));
                            }
                        } else if (key.equals("PAR_Down_Avg")) {
                            if (stringToDouble(activityData.get(key)) >0 && stringToDouble(activityData.get(key)) <61 ) {
                                historyDrive.setPar(activityData.get(key));
                            }
                        } else if (key.equals("par_Avg")) {
                            if (stringToDouble(activityData.get(key)) >0 && stringToDouble(activityData.get(key)) <60 ) {
                                historyDrive.setPar(activityData.get(key));
                            }
                        }
                        else if (key.equals("VWC_10cm_Avg")) {
                            if (stringToDouble(activityData.get(key)) >0.1 && stringToDouble(activityData.get(key)) <0.4 ) {
                                historyDrive.setSwc(activityData.get(key));
                            }
                        } else if (key.equals("ts")) {
                            // 时间戳转日期
                            long lt = new Long(activityData.get(key));
                            int days = Math.abs((int) ((time.getTime() - new Date(lt).getTime()) / (1000*3600*24)));
                            if (days > 2) {
                                historyDrive.setMock(true);
                            }

                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            log.info("histroyDrive:" + historyDrive.toString());
            // 如果没取到真实数据，则使用3天平均值来mock数据代替
            // 取3天中的平均值,赋值
            // 取历史同期
            List<HistoryDrive> historyDrives = historyDriveMapper.historySameTime(time.monthStartFromOne(),time.dayOfMonth(),"DHF");
            // 新增需求：对比这一天的数据 如果和前一天一模一样 ，证明是假数据，需要替换历史同期
            // 读取前一天的数据
            HistoryDrive oldHistoryDriver = new HistoryDrive();
            if (list.size() >= 1) {
                oldHistoryDriver = list.get(list.size()-1);
            } else {
                String oldDat = DateUtil.format(DateUtil.offsetDay(time, -1), "yyyy-MM-dd");
                List<HistoryDrive> oldhistoryDrives = historyDriveMapper.listByCode("DHF",oldDat, oldDat);
                oldHistoryDriver = oldhistoryDrives.get(0);
            }

            // 历史同期没有   取近三天
            if (historyDrives == null || historyDrives.size() == 0) {
                String avgStart = DateUtil.format(DateUtil.offsetDay(time, -3), "yyyy-MM-dd");
                String avgEnd = DateUtil.format(time, "yyyy-MM-dd");
                if (list.size() >= 3) {
                    //判断list长度 只保留最后两位
                    for(int l=list.size()-1;l>list.size()-4;l--) {
                        historyDrives.add(list.get(l));
                    }
                } else {
                    historyDrives = historyDriveMapper.listByCode("DHF",avgStart, avgEnd);
                }
            }
            log.info("----------------------------------------------------");
            log.info(historyDrive.getPar());
            log.info(oldHistoryDriver.getPar());
            log.info(String.valueOf(ErrorCorrectionUtil.lastAvgPAR(historyDrives)));


            // 根据前3天的均值来mock数据 : Ta\Rh\Par\Swc
            if (historyDrive.getTa() == null || historyDrive.getMock()|| historyDrive.getTa().substring(0,7).equals(oldHistoryDriver.getTa().substring(0,7))) {
                historyDrive.setTa(String.valueOf(ErrorCorrectionUtil.lastAvgTa(historyDrives)));
                log.info("Ta is mock");
                historyDrive.setMock(true);
            }
            if (historyDrive.getRh() == null || historyDrive.getMock() || historyDrive.getRh().substring(0,7).equals(oldHistoryDriver.getRh().substring(0,7))) {
                historyDrive.setRh(String.valueOf(ErrorCorrectionUtil.lastAvgRH(historyDrives)));

                log.info("Rh is mock");
                historyDrive.setMock(true);
            }
            if (historyDrive.getPar() == null || historyDrive.getMock() || historyDrive.getPar().substring(0,7).equals(oldHistoryDriver.getPar().substring(0,7))) {
                historyDrive.setPar(String.valueOf(ErrorCorrectionUtil.lastAvgPAR(historyDrives)));

                log.info("Par is mock");
                historyDrive.setMock(true);
            }
            if (historyDrive.getSwc() == null || historyDrive.getMock() || historyDrive.getSwc().substring(0,7).equals(oldHistoryDriver.getSwc().substring(0,7))) {
                historyDrive.setSwc(String.valueOf(ErrorCorrectionUtil.lastAvgSWC(historyDrives)));
                log.info("Swc is mock");
                historyDrive.setMock(true);
            }
            // 如果还为空，置为0
            if (historyDrive.getSwc() == null || "".equals(historyDrive.getSwc())) {
                historyDrive.setSwc("0");
            }
            // 将记录加入list
            list.add(historyDrive);
        }
        log.info("list size:" +list.size()+"---"+list.get(0).getDay());
        iHistoryDriveService.saveBatch(list);
        return true;
    }



    @Override
    public Boolean updateFCMASS(String start, String end, String obsfile, String sitecode)
    {
        // 删除对应的数据
        System.out.println("Enter Function data:");
        DateTime startDate = DateUtil.parse(start);
        DateTime endDate = DateUtil.parse(end);
        long betweenDay = DateUtil.between(startDate, endDate, DateUnit.DAY);
        List<HistoryDrive> list = new ArrayList<>();
        // 遍历start到end日期内的所有数据
        // 如果是真实的，就获取真实的
        // 如果获取不到台站真实数据，拿前3天平均值替代，并用mock字段标注为mock
        // 存储在historyDriver表中
        for (int i = 0; i <= betweenDay; i++) {
            System.out.println("The "+i +"time of New historydriver from Station");
            HistoryDrive historyDrive = new HistoryDrive();
            DateTime time = DateUtil.offsetDay(startDate, i);
            historyDrive.setSiteCode(sitecode);
            historyDrive.setYear(time.year());
            historyDrive.setMonth(time.monthStartFromOne());
            historyDrive.setDay(time.dayOfMonth());
            historyDrive.setYear(time.year());
            historyDrive.setCreateTime(LocalDateTime.now());
            historyDrive.setMock(false);
            try {
                // 获取该次遍历日期的各项真实数值（TA\RH\PAR\SWC）
                ActivityDto activityConfig = thirdDataUtil.getActivityConfig(sitecode).get(0);

                activityConfig.getActivityMeasureList().forEach(ele -> {
                    String measureCodeList = ele.getMeasureList().stream().map(ActivityItemMeasureDto::getMeasureCode)
                            .collect(Collectors.joining(","));
                    log.info("measureCodeList:"+measureCodeList);

                    HashMap<String, String> activityData =
                            thirdDataUtil.getActivityData(sitecode, measureCodeList, String.valueOf(time.getTime()),
                                    ele.getActivityCode());

                    for (String key : activityData.keySet()) {
                        log.info("key:"+key);
                        log.info("value:"+activityData.get(key));
                        if (key.equals("Fc_mass") || key.equals("cov_Uz_co2_op")) {
                            System.out.println("FCMASS:"+activityData.get(key));
                            String fcmassvalue="";
                            if (stringToDouble(activityData.get(key)) >-10 && stringToDouble(activityData.get(key)) <10 ) {
                                fcmassvalue = activityData.get(key);
                            } else {
                                fcmassvalue = "-9999";
                            }
                            // 这个值NEE不存数据库，直接存csv中即可
                            System.out.println("echo "+fcmassvalue + ",-9999,-9999,-9999,-9999,-9999,-9999," + DateUtil.format(time, "MM/dd/yyyy")+" >> /root/shanrui/"+sitecode+"/"+obsfile);
                            String cmdRes = remoteExecuteCommandutil.execute("echo "+fcmassvalue + ",-9999,-9999,-9999,-9999,-9999,-9999," + DateUtil.format(time, "MM/dd/yyyy")+" >> /root/shanrui/"+sitecode+"/"+obsfile);
                            System.out.println("CMDRES:"+cmdRes);
                            //fileAddItem(new File("/root/shanrui/DHF/"+obsfile),activityData.get(key) + ",-9999,-9999,-9999,-9999,-9999,-9999," + DateUtil.format(time, "MM/dd/yyyy"));
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    @Override
    /**
     * 根据sitecode执行流程
     * 所有函数、路径、镜像名称和sh脚本都增加 **site_code**
     */
    public Boolean say(String site_code) {
        log.info("进入"+site_code+"每日工作流程");
        log.info("导出全部历史csv");
        // 导出到/tmp/data.csv
        Assert.isTrue(iHistoryDriveService.exportCsv(site_code), "导出全部历史csv失败");
        log.info("上传csv到服务器");
        // ****** 加一个代码路径
        Assert.isTrue(fileTransferService.uploadFile(
                "/tmp/data-"+site_code+".csv",
                "/root/csv/codefile-"+site_code+"/model_input_"+site_code+".csv"),
                "上传csv到服务器失败"
        );


        log.info("运行第一个预测镜像："+site_code);
        // ****** 循环
        remoteExecuteCommandutil.execute(
                "docker run -d --rm --name prediction-"+site_code+"  -v /root/csv/codefile-"+site_code+"/:/root/codefile prediction:latest "
                        + "/root/codefile/run.sh");
        try {
            log.info("等待5秒");
            Thread.sleep(10000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String result = "prediction-"+site_code;
        while (result.contains("prediction-"+site_code)) {
            log.info("判断预测数据csv是否生成");
            result = remoteExecuteCommandutil.execute("docker  ps --filter name=prediction-"+site_code);
            try {
                if (result.contains("prediction-"+site_code)) {
                    log.info("等待10秒,再次判断预测数据csv是否生成");
                    Thread.sleep(10000L);
                } else {
                    log.info("预测数据csv已生成");
                }
            } catch (InterruptedException e) {
                log.info("异常中断");
                e.printStackTrace();
            }
        }
        log.info("下载预测数据csv");
//        Assert.isTrue(fileTransferService.downloadFile(
//                "/tmp/forecast-"+site_code+".csv",
//                "/root/codefile/pred_result.csv"
//                ),
//                "下载预测数据csv失败");
        log.info("导入预测数据");
        // 有bug，只导入最新生成的 day_number 天预测即可，不用导入全部数据
        Assert.isTrue(iForecastResultService.importCsv("/root/csv/codefile-"+site_code+"/pred_result.csv", 67), "导入预测数据失败");

        // pred_result.csv  卫华生成的这个放到/xuq/driver_DHF.csv     /root/csv/codefile/
        // obs.csv 把第一列先手动填充到前一天，然后自动的把每天的值放进来
        // 直接运行程序,大概要8个小时之类，执行python DHF_Metropolis.py ,生成的文件: simresult_out.csv
        // simresult_out.csv  过去2个月+未来7天
        log.info("移动预测数据csv到/root/shanrui/"+site_code+"/driver.csv");
        remoteExecuteCommandutil.execute("cp -f /root/csv/codefile-"+site_code+"/pred_result.csv /root/shanrui/"+site_code+"/driver.csv");
        log.info("运行同化镜像");
        // ****** 循环
        remoteExecuteCommandutil.execute(
                "docker run -d --rm --name shanrui-"+site_code+" -v /root/shanrui/"+site_code+"/:/root/xuq/ tonghua:shanrui "
                        + "/root/xuq/run.sh");
        try {
            log.info("等待5秒");
            Thread.sleep(50000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        result = "shanrui-"+site_code;
        while (result.contains("shanrui-"+site_code)) {
            log.info("判断同化数据csv是否生成");
            result = remoteExecuteCommandutil.execute("docker  ps --filter name=shanrui-"+site_code);
            try {
                if (result.contains("shanrui-"+site_code)) {
                    log.info("等待1200秒,再次判断同化数据csv是否生成");
                    Thread.sleep(1200000L);
                } else {
                    log.info("同化数据csv已生成");
                }
            } catch (InterruptedException e) {
                log.info("异常中断");
                e.printStackTrace();
            }
        }
        log.info("下载同化数据csv");
        Assert.isTrue(iShowResultService.importCsv("/root/shanrui/"+site_code+"/"+site_code+"_simresult_out_mean.csv", LocalDateTime.now()), "导入正演数据失败");
        Assert.isTrue(iShowResultService.importStatistics("/root/shanrui/"+site_code+"/"+site_code+"_simresult_out_all.csv", LocalDateTime.now()), "导入正演统计数据失败");
        remoteExecuteCommandutil.execute("rm -rf /root/shanrui/"+site_code+"/"+site_code+"_simresult_out_all_bak.csv");
        remoteExecuteCommandutil.execute("mv /root/shanrui/"+site_code+"/"+site_code+"_simresult_out_all.csv /root/shanrui/"+site_code+"/"+site_code+"_simresult_out_all_bak.csv");
        log.info("完成");


//        log.info("导入预测数据");
//        // 有bug，只导入最新生成的 day_number 天预测即可，不用导入全部数据
//        Assert.isTrue(iForecastResultService.importCsv("/root/shanrui/"+site_code+"/simresult_out.csv", day_number), "导入预测数据失败");
//        log.info("移动预测数据csv到/root/xuq/"+site_code+"/driver.csv");
//        remoteExecuteCommandutil.execute("cp -f /root/shanrui/"+site_code+"/simresult_out.csv /root/xuq/"+site_code+"/driver.csv");
//        log.info("运行第二个镜像");
//        remoteExecuteCommandutil.execute(
//                "docker run -d --rm --name tonghua-"+site_code+"  -v /root/xuq/"+site_code+":/root/xuq tonghua:latest python /root/xuq/DALEC-DHF.py");
//        String secResult = "tonghua-"+site_code;
//        while (secResult.contains("tonghua-"+site_code)) {
//            log.info("判断正演数据csv是否生成");
//            secResult = remoteExecuteCommandutil.execute("docker ps --filter name=tonghua-"+site_code);
//            try {
//                if (secResult.contains("tonghua-"+site_code)) {
//                    log.info("等待10秒,再次判断正演数据csv是否生成");
//                    Thread.sleep(10000L);
//                } else {
//                    log.info("正演数据csv已生成");
//                }
//            } catch (InterruptedException e) {
//                log.info("异常中断");
//                e.printStackTrace();
//            }
//        }
//        log.info("下载正演数据csv");
////        Assert.isTrue(fileTransferService.downloadFile("/tmp/tonghua-"+site_code+".csv", "/root/xuq/out.csv"),
////                "下载正演数据csv失败");
//        log.info("导入正演数据");
//        // 每天都执行，会把12年-当天 **所有的**正演完成的数据给存储到show_result表中（评估有没有必要）
//        //到时可以这样，因为其实看一下发现最终正演的数据都是假的
//        Assert.isTrue(iShowResultService.importCsv("/root/xuq/"+site_code+"/out.csv", LocalDateTime.now()), "导入正演数据失败");
//        log.info("完成");
        return true;
    }

    @Override
    // 删除某一时段的旧数据，重新拉取最新的
    public Boolean data(String site_code, String start, String end) {
        // 删除该站点对应的数据
        historyDriveMapper.deleteOldBySitecode(start, end, site_code);
        DateTime startDate = DateUtil.parse(start);
        DateTime endDate = DateUtil.parse(end);
        long betweenDay = DateUtil.between(startDate, endDate, DateUnit.DAY);
        List<HistoryDrive> list = new ArrayList<>();
        // 遍历start到end日期内的所有数据
        // 如果是真实的，就获取真实的
        // 如果获取不到台站真实数据，拿前3天平均值替代，并用mock字段标注为mock
        // 存储在historyDriver表中
        for (int i = 0; i <= betweenDay; i++) {
            final int[] flas = {0};


            HistoryDrive historyDrive = new HistoryDrive();
            DateTime time = DateUtil.offsetDay(startDate, i);
            historyDrive.setSiteCode(site_code);
            historyDrive.setYear(time.year());
            historyDrive.setMonth(time.monthStartFromOne());
            historyDrive.setDay(time.dayOfMonth());
            historyDrive.setYear(time.year());
            historyDrive.setCreateTime(LocalDateTime.now());
            historyDrive.setMock(false);
            try {
                // 获取该次遍历日期的各项真实数值（TA\RH\PAR\SWC）
                ActivityDto activityConfig = thirdDataUtil.getActivityConfig(site_code).get(0);

                activityConfig.getActivityMeasureList().forEach(ele -> {
                    String measureCodeList = ele.getMeasureList().stream().map(ActivityItemMeasureDto::getMeasureCode)
                            .collect(Collectors.joining(","));

                    HashMap<String, String> activityData =
                            thirdDataUtil.getActivityData(site_code, measureCodeList, String.valueOf(time.getTime()),
                                    ele.getActivityCode());

                    for (String key : activityData.keySet()) {
                        log.info("key:"+key);
                        log.info("value:"+activityData.get(key));
                        if (key.equals("Ta")) {
                            // 数值订正
                            if (stringToDouble(activityData.get(key)) >3 && stringToDouble(activityData.get(key)) <35 ) {
                                historyDrive.setTa(activityData.get(key));
                            }
                        } else if (key.equals("Rh")) {
                            if (stringToDouble(activityData.get(key)) >20 && stringToDouble(activityData.get(key)) <100 ) {
                                historyDrive.setRh(activityData.get(key));
                            }
                        } else if (key.equals("Par")) {
                            if (stringToDouble(activityData.get(key)) >0 && stringToDouble(activityData.get(key)) <61 ) {
                                historyDrive.setPar(activityData.get(key));
                            }
                        } else if (key.equals("par_Avg")) {
                            if (stringToDouble(activityData.get(key)) >0 && stringToDouble(activityData.get(key)) <60 ) {
                                historyDrive.setPar(activityData.get(key));
                            }
                        }
                        else if (key.equals("VWC_10cm_Avg")) {
                            if (stringToDouble(activityData.get(key)) >0.1 && stringToDouble(activityData.get(key)) <0.4 ) {
                                historyDrive.setSwc(activityData.get(key));
                            }
                        } else if (key.equals("Fc_mass")|| key.equals("cov_Uz_co2_op")) {
                            flas[0] =1;
                            String fcmassvalue="";
                            if (stringToDouble(activityData.get(key)) >-10 && stringToDouble(activityData.get(key)) <10 ) {
                                fcmassvalue = activityData.get(key);
                            } else {
                                fcmassvalue = "-9999";
                            }
                            // 这个值NEE不存数据库，直接存csv中即可
                            remoteExecuteCommandutil.execute("echo "+fcmassvalue + ",-9999,-9999,-9999,-9999,-9999,-9999," + DateUtil.format(time, "MM/dd/yyyy")+" >> /root/shanrui/"+site_code+"/obs.csv");
                            //fileAddItem(new File("/root/shanrui/DHF/obs.csv"),activityData.get(key) + ",-9999,-9999,-9999,-9999,-9999,-9999," + DateUtil.format(time, "MM/dd/yyyy"));
                        }
                        else if (key.equals("ts")) {
                            // 时间戳转日期
                            long lt = new Long(activityData.get(key));
                            int days = Math.abs((int) ((time.getTime() - new Date(lt).getTime()) / (1000 * 3600 * 24)));
                            if (days > 2) {
                                historyDrive.setMock(true);
                            }
                        }

                    }
                });
                if(flas[0]==0){
                    remoteExecuteCommandutil.execute("echo "+"-9999" + ",-9999,-9999,-9999,-9999,-9999,-9999," + DateUtil.format(time, "MM/dd/yyyy")+" >> /root/shanrui/"+site_code+"/obs.csv");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            // 如果没取到真实数据，则使用3天平均值来mock数据代替

            // 这里有几种情况处理误差，最终都调用ErrorCorrectionUtil的平均值函数
            //1、取整个list的平均值
            //2、取最近3天的平均值
            //3、取历史同期的同月日的数值平均值   目前选这个！

            // 下面是取最近3天平均值
//            String avgStart = DateUtil.format(DateUtil.offsetDay(time, -3), "yyyy-MM-dd");
//            String avgEnd = DateUtil.format(time, "yyyy-MM-dd");
//            List<HistoryDrive> historyDrives = new ArrayList<>();
//            if (list.size() >= 3) {
//                //判断list长度 只保留最后两位
//                for(int l=list.size()-1;l>list.size()-4;l--) {
//                    historyDrives.add(list.get(l));
//                }
//            } else {
//                historyDrives = historyDriveMapper.listByCode(site_code,avgStart, avgEnd);
//            }

            // 取历史同期
            List<HistoryDrive> historyDrives = historyDriveMapper.historySameTime(time.monthStartFromOne(),time.dayOfMonth(),site_code);
            // 新增需求：对比这一天的数据 如果和前一天一模一样 ，证明是假数据，需要替换历史同期
            // 读取前一天的数据
            HistoryDrive oldHistoryDriver = new HistoryDrive();
            if (list.size() >= 1) {
                oldHistoryDriver = list.get(list.size()-1);
            } else {
                String oldDat = DateUtil.format(DateUtil.offsetDay(time, -1), "yyyy-MM-dd");
                List<HistoryDrive> oldhistoryDrives = historyDriveMapper.listByCode("DHF",oldDat, oldDat);
                if(!CollectionUtils.isEmpty(oldhistoryDrives)){
                    oldHistoryDriver = oldhistoryDrives.get(0);
                }
            }
            // 历史同期没有   取近三天
            if (historyDrives == null || historyDrives.size() == 0) {
                String avgStart = DateUtil.format(DateUtil.offsetDay(time, -3), "yyyy-MM-dd");
                String avgEnd = DateUtil.format(time, "yyyy-MM-dd");
                if (list.size() >= 3) {
                    //判断list长度 只保留最后两位
                    for(int l=list.size()-1;l>list.size()-4;l--) {
                        historyDrives.add(list.get(l));
                    }
                } else {
                    historyDrives = historyDriveMapper.listByCode("DHF",avgStart, avgEnd);
                }
            }



            // 根据前3天的均值来mock数据 : Ta\Rh\Par\Swc
            if (historyDrive.getTa() == null || historyDrive.getMock()|| (oldHistoryDriver.getTa()!=null && historyDrive.getTa().length()>=8 && oldHistoryDriver.getTa().length()>=8  && historyDrive.getTa().substring(0,7).equals(oldHistoryDriver.getTa().substring(0,7)))) {
                historyDrive.setTa(String.valueOf(ErrorCorrectionUtil.lastAvgTa(historyDrives)));
                log.info("Ta is mock");
                historyDrive.setMock(true);
            }
            if (historyDrive.getRh() == null || historyDrive.getMock() || (oldHistoryDriver.getRh()!=null && historyDrive.getRh().length()>=8 && oldHistoryDriver.getRh().length()>=8 && historyDrive.getRh().substring(0,7).equals(oldHistoryDriver.getRh().substring(0,7)))) {
                historyDrive.setRh(String.valueOf(ErrorCorrectionUtil.lastAvgRH(historyDrives)));

                log.info("Rh is mock");
                historyDrive.setMock(true);
            }
            if (historyDrive.getPar() == null || historyDrive.getMock() || (oldHistoryDriver.getPar()!=null && historyDrive.getPar().length()>=8 && oldHistoryDriver.getPar().length()>=8  && historyDrive.getPar().substring(0,7).equals(oldHistoryDriver.getPar().substring(0,7)))) {
                historyDrive.setPar(String.valueOf(ErrorCorrectionUtil.lastAvgPAR(historyDrives)));

                log.info("Par is mock");
                historyDrive.setMock(true);
            }
            if (historyDrive.getSwc() == null || historyDrive.getMock() || (oldHistoryDriver.getSwc()!=null && historyDrive.getSwc().length()>=8 && oldHistoryDriver.getSwc().length()>=8  && historyDrive.getSwc().substring(0,7).equals(oldHistoryDriver.getSwc().substring(0,7)))) {
                historyDrive.setSwc(String.valueOf(ErrorCorrectionUtil.lastAvgSWC(historyDrives)));
                log.info("Swc is mock");
                historyDrive.setMock(true);
            }
            // 如果还为空，置为0
            if (historyDrive.getSwc() == null || "".equals(historyDrive.getSwc())) {
                historyDrive.setSwc("0");
            }
            list.add(historyDrive);
        }
        iHistoryDriveService.saveBatch(list);
        return true;
    }
}
