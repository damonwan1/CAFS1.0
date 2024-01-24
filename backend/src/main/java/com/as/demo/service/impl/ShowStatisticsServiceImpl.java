package com.as.demo.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.text.csv.CsvReader;
import cn.hutool.core.text.csv.CsvUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.as.demo.entity.ShowResult;
import com.as.demo.entity.ShowStatistics;
import com.as.demo.mapper.ShowResultMapper;
import com.as.demo.mapper.ShowStatisticsMapper;
import com.as.demo.service.IShowResultService;
import com.as.demo.service.IShowStatisticsService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 预测结果表 服务实现类
 * </p>
 *
 * @author yule
 * @since 2021-05-18
 */
@Service
public class ShowStatisticsServiceImpl extends ServiceImpl<ShowStatisticsMapper, ShowStatistics> implements IShowStatisticsService {


}
