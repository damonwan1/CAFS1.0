package com.as.demo.common;

import java.time.LocalDateTime;


import com.as.demo.service.IShowResultService;
import com.as.demo.service.impl.ShowResultServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.as.demo.service.IOperationService;

import cn.hutool.core.date.DateUtil;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.util.Assert;

@Configuration
@EnableScheduling
//@EnableAsync
public class StaticScheduleTask {
    @Autowired
    private IOperationService iOperationService;


    //Springboot @Scheduled 默认是单线程的， 也就是说当我们定义了多个定时任务时，如果有本应该是相同时间触发的定时任务， 会进行排队，
    // 如果某个定时任务执行时间过长， 就会导致其他定时并未按照设置时间来触发执行。
    // 异步任务可以部分解决这个问题，
    // 在定时任务上再加上@Asyns注解，定时任务就会被作为异步任务多线程执行
    //但是异步任务同样存在一个问题，如果任务执行顺序对结果没有影响，则可以用异步任务解决。
    // 但是如果同样的任务必须同步执行（例如定时扫表数据进行相关处理），
    // 但又不希望不同的定时任务之间也排队等待，则可以通过一下方式解决。

    @Bean
    public ThreadPoolTaskScheduler testExecutor() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler  = new ThreadPoolTaskScheduler();
        // 配置线程池大小
        threadPoolTaskScheduler.setPoolSize(10);
        // 设置线程名
        threadPoolTaskScheduler.setThreadNamePrefix("task-scheduling-");
        // 设置等待任务在关机时完成
        //    threadPoolTaskScheduler.setWaitForTasksToCompleteOnShutdown(true);
        // 设置等待终止时间
        // threadPoolTaskScheduler.setAwaitTerminationSeconds(60);
        return threadPoolTaskScheduler;
    }

    // 鼎湖山的流程
    @Scheduled(cron = "1 0 0 * * ?")
    private void configureTasks() {
        // 获取昨天的日期
        String yesterday = DateUtil.format(DateUtil.yesterday(), "yyyy-MM-dd");
        // 从台站获取昨天的最新数据
        iOperationService.data(yesterday, yesterday);

        // 跑流程
        iOperationService.say();

    }

     //千烟洲流程
    @Scheduled(cron = "1 10 0 * * ?")
    private void configureTasks2() {
        // 获取昨天的日期
        String yesterday = DateUtil.format(DateUtil.yesterday(), "yyyy-MM-dd");
        // 从台站获取昨天的最新数据
        iOperationService.data("QYA",yesterday,yesterday);

        // 跑流程
        iOperationService.say("QYA");
    }

    //会同流程
    @Scheduled(cron = "1 20 0 * * ?")
    private void configureTasks3() {
        // 获取昨天的日期
        String yesterday = DateUtil.format(DateUtil.yesterday(), "yyyy-MM-dd");
        // 从台站获取昨天的最新数据
        iOperationService.data("HTF",yesterday,yesterday);

        // 跑流程
        iOperationService.say("HTF");
    }

    //尖峰岭流程
    @Scheduled(cron = "1 30 0 * * ?")
    private void configureTasks4() {
        // 获取昨天的日期
        String yesterday = DateUtil.format(DateUtil.yesterday(), "yyyy-MM-dd");
        // 从台站获取昨天的最新数据
        iOperationService.data("JFF",yesterday,yesterday);

        // 跑流程
        iOperationService.say("JFF");
    }
}