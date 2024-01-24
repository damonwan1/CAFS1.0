package com.as.demo.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.as.demo.entity.dto.ActivityDto;
import com.as.demo.entity.dto.LoginDto;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ThirdDataUtil {
    public String api;

    @Value("${api}")
    public void setApi(String value) {
        api = value;
    }

    public String getToken() {
        if (LocalCache.get("token") == null) {
            Map<String, String> data = new HashMap();
            data.put("appId", "###");
            data.put("random", "###");
            data.put("sign", "###");
            String url = api + "/login";
            log.info("开始请求:{}", url);
            String result = HttpUtil.post(api + "/login", JSON.toJSONString(data));
            log.info("响应:{}", result);
            Result<LoginDto> res = JSON.parseObject(result, new TypeReference<Result<LoginDto>>() {
            });
            LocalCache.put("token", res.getData().getAccessToken());
        }
        return LocalCache.get("token");
    }

    /**
     * 默认获取鼎湖山的activity配置
     * @return
     */
    public List<ActivityDto> getActivityConfig() {
        String url = api + "/prediction/getActivityConfig";
        log.info("开始请求:{}", url);
        String result = HttpRequest.get(url)
                .header("token", getToken())
                .header("stationCode", "DHF")
                .execute().body();
        log.info("响应:{}", result);
        Result<List<ActivityDto>> res = JSON.parseObject(result, new TypeReference<Result<List<ActivityDto>>>() {
        });
        return res.getData();
    }

    /**
     * 根据site_code访问
     * @param site_code
     * @return
     */
    public List<ActivityDto> getActivityConfig(String site_code) {
        String url = api + "/prediction/getActivityConfig";
        log.info("开始请求:{}", url);
        String result = HttpRequest.get(url)
                .header("token", getToken())
                .header("stationCode", site_code)
                .execute().body();
        log.info("响应:{}", result);
        Result<List<ActivityDto>> res = JSON.parseObject(result, new TypeReference<Result<List<ActivityDto>>>() {
        });
        return res.getData();
    }

    /**
     * 默认获取鼎湖山的数据
     * @param measureCodeList
     * @param time
     * @param activityCode
     * @return
     */
    public HashMap<String, String> getActivityData(String measureCodeList, String time, String activityCode) {
        String url =
                String.format(api + "/prediction/getActivityData?measureCodeList=%s&time=%s&activityCode=%s",
                        measureCodeList, time,
                        activityCode);
        log.info("开始请求:{}", url);
        String result = HttpRequest.get(url)
                .header("token", getToken())
                .header("stationCode", "DHF")
                .execute().body();
        log.info("响应:{}", result);
        Result<HashMap<String, String>> res =
                JSON.parseObject(result, new TypeReference<Result<HashMap<String, String>>>() {
                });
        return res.getData();
    }

    /**
     * 根据site_code获取站点数据
     * @param measureCodeList
     * @param time
     * @param activityCode
     * @param site_code
     * @return
     */
    public HashMap<String, String> getActivityData(String site_code, String measureCodeList, String time, String activityCode) {
        String url =
                String.format(api + "/prediction/getActivityData?measureCodeList=%s&time=%s&activityCode=%s",
                        measureCodeList, time,
                        activityCode);
        log.info("开始请求:{}", url);
        String result = HttpRequest.get(url)
                .header("token", getToken())
                .header("stationCode", site_code)
                .execute().body();
        log.info("响应:{}", result);
        Result<HashMap<String, String>> res =
                JSON.parseObject(result, new TypeReference<Result<HashMap<String, String>>>() {
                });
        return res.getData();
    }
}
