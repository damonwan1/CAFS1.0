package com.as.demo.common;

import cn.hutool.core.date.DateUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JobsMonitor {

    class MonitorJob {
        public String Gpu;
        public String Time;

        public MonitorJob(String gpu, String time) {
            Gpu = gpu;
            Time = time;
        }

        public String getTime() {
            return Time;
        }

        public void setGpu(String gpu) {
            Gpu = gpu;
        }

        public void setTime(String time) {
            Time = time;
        }

        public String getGpu() {

            return Gpu;
        }
    }
    public static void main(String[] args) {
        // 时间戳转日期
        long lt = new Long("1659195000000");
        int days = Math.abs((int) ((DateUtil.parse("2022-07-18").getTime() - new Date(lt).getTime()) / (1000*3600*24)));

//        List<MonitorJob> jobsqueue = new ArrayList<MonitorJob>();
//        JobsMonitor jm = new JobsMonitor();
//        jm.ss(jobsqueue);
//        //jm.supercomputerProcess(jobsqueue);
//        jm.virtualProcess(jobsqueue);
    }

    public  void ss(List<MonitorJob> jobsqueue) {
        File csv = new File("C:\\Users\\wanme\\Desktop\\data1.csv");  // CSV文件路径
        BufferedReader br = null;
        try
        {
            br = new BufferedReader(new FileReader(csv));
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        String line = "";
        String everyLine = "";
        try {
            List<String> allString = new ArrayList<>();
            int i = 0;
            while ((line = br.readLine()) != null && i < 200)  //读取到的内容给line变量
            {
                everyLine = line;
                String[] tmps = everyLine.split(",");
                jobsqueue.add(new MonitorJob(tmps[0], tmps[1]));
                System.out.println(everyLine);
                allString.add(everyLine);
                i++;
            }

            System.out.println("csv表格中所有行数："+allString.size());
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    // 基于虚拟化调度的作业模拟，共80块卡
    public void virtualProcess(List<MonitorJob> jobsqueue) {
        int max_GPU = 80;
        int total_GPU = 0;
        double total_time = 0;
        List<MonitorJob> runningqueue = new ArrayList<MonitorJob>();
        //只要排队队列有任务
        while (jobsqueue.size() > 0) {
            // ***********GPU 占用数
            //System.out.println(total_GPU);
            // *********** 弹性扩容曲线
            System.out.println(runningqueue.size());
            // 如果排队队列第一个任务GPU够
            if (total_GPU + Integer.parseInt(jobsqueue.get(0).getGpu()) < max_GPU) {
                total_GPU += Integer.parseInt(jobsqueue.get(0).getGpu());
                runningqueue.add(jobsqueue.get(0));
                jobsqueue.remove(0);
            }
            // 如果第一个GPU任务不够，那么等待运行队列中时间最短的任务跑完
            else {
                // 找出最短时间的任务下标
                int minIndex = 0;
                double minTime = Double.parseDouble(runningqueue.get(0).getTime());
                for (int i = 1; i < runningqueue.size(); i++) {
                    if (Double.parseDouble(runningqueue.get(i).getTime()) < minTime) {
                        minIndex = i;
                        minTime = Double.parseDouble(runningqueue.get(i).getTime());
                    }
                }
                // 对此任务进行出队
                total_time += minTime;
                total_GPU -= Integer.parseInt(runningqueue.get(minIndex).getGpu());
                runningqueue.remove(minIndex);
                // 对剩余的运行队列所有任务执行 共减 操作
                for (MonitorJob tmp : runningqueue) {
                    tmp.setTime((Double.parseDouble(tmp.getTime()) - minTime) + "");
                }
            }
        }
        System.out.println("-------------------------------");
        System.out.println("time 1 :" + total_time );
        // 当排队队列没有任务了，直接加上剩余运行最长任务
        int maxIndex = 0;
        double maxTime = Double.parseDouble(runningqueue.get(0).getTime());
        for (int i = 1; i < runningqueue.size(); i++) {
            if (Double.parseDouble(runningqueue.get(i).getTime()) > maxTime) {
                maxIndex = i;
                maxTime = Double.parseDouble(runningqueue.get(i).getTime());
            }
        }
        total_time += maxTime;
        System.out.println("time 2 :" + total_time );

    }


    // 基于超算集群的模拟：10个节点，每个节点8块卡，80块卡
    public void supercomputerProcess(List<MonitorJob> jobsqueue) {
        int total_GPU = 0;
        int max_Num = 10;
        int total_time = 0;
        List<MonitorJob> runningqueue = new ArrayList<MonitorJob>();
        //只要排队队列有任务
        while (jobsqueue.size() > 0) {
            //********GPU 占用数据
            //System.out.println(total_GPU);
            // *********** 弹性扩容曲线
            System.out.println(runningqueue.size());
            // 如果有空闲节点
            if (runningqueue.size() < max_Num) {
                total_GPU += Integer.parseInt(jobsqueue.get(0).getGpu());
                runningqueue.add(jobsqueue.get(0));
                jobsqueue.remove(0);
            }
            // 如果 节点任务不够，那么等待运行队列中时间最短的任务跑完
            else {
                // 找出最短时间的任务下标
                int minIndex = 0;
                double minTime = Double.parseDouble(runningqueue.get(0).getTime());
                for (int i = 1; i < runningqueue.size(); i++) {
                    if (Double.parseDouble(runningqueue.get(i).getTime()) < minTime) {
                        minIndex = i;
                        minTime = Double.parseDouble(runningqueue.get(i).getTime());
                    }
                }
                //对此任务进行出队
                total_time += minTime;
                total_GPU -= Integer.parseInt(runningqueue.get(minIndex).getGpu());
                runningqueue.remove(minIndex);
                // 对剩余的运行队列所有任务执行 共减 操作
                for (MonitorJob tmp : runningqueue) {
                    tmp.setTime((Double.parseDouble(tmp.getTime()) - minTime) + "");
                }
            }
        }
        System.out.println("-------------------------------");
        System.out.println("time 1 :" + total_time );
        // 当排队队列没有任务了，直接加上剩余运行最长任务
        int maxIndex = 0;
        double maxTime = Double.parseDouble(runningqueue.get(0).getTime());
        for (int i = 1; i < runningqueue.size(); i++) {
            if (Double.parseDouble(runningqueue.get(i).getTime()) > maxTime) {
                maxIndex = i;
                maxTime = Double.parseDouble(runningqueue.get(i).getTime());
            }
        }
        total_time += maxTime;
        System.out.println("time 2 :" + total_time );

    }



}
