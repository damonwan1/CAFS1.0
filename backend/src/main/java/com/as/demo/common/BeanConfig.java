package com.as.demo.common;

import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

/**
 * @Author Damon
 * @description
 */

@Component
public class BeanConfig {


    @Bean
    public ThreadPoolTaskScheduler testExecutor() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler  = new ThreadPoolTaskScheduler();
        // 配置线程池大小
        threadPoolTaskScheduler.setPoolSize(20);
        // 设置线程名
        threadPoolTaskScheduler.setThreadNamePrefix("task-scheduling-");
        // 设置等待任务在关机时完成
        //    threadPoolTaskScheduler.setWaitForTasksToCompleteOnShutdown(true);
        // 设置等待终止时间
        // threadPoolTaskScheduler.setAwaitTerminationSeconds(60);
        return threadPoolTaskScheduler;
    }
}