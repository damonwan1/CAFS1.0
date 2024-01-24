package com.as.demo.service;

import com.alibaba.fastjson.JSONArray;
import com.as.demo.entity.Operation;
import com.as.demo.entity.ShowResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 操作记录表 服务类
 * </p>
 *
 * @author yule
 * @since 2021-05-18
 */
public interface IOperationService extends IService<Operation> {
    /**
     * 获取所有站点信息
     * @param
     * @return
     */
    List<Operation> allstation();
    /**
     * 默认方法：鼎湖山站点流程
     * 执行工作流流程
     * @return
     */
    Boolean say();

    /**
     * 默认方法，鼎湖山站点
     *   遍历start到end日期内的所有数据
     *  如果是真实的，就获取真实的
     *  如果获取不到台站真实数据，拿前3天平均值替代，并用mock字段标注为mock
     *  存储在historyDriver表中
     * @param start
     * @param end
     * @return
     */
    Boolean data(String start, String end);
    /**
     * 根据站点代码执行
     * 执行工作流流程
     * @return
     */
    Boolean say(String site_code);

    /**
     *  根据站点代码执行
     *   遍历start到end日期内的所有数据
     *  如果是真实的，就获取真实的
     *  如果获取不到台站真实数据，拿前3天平均值替代，并用mock字段标注为mock
     *  存储在historyDriver表中
     * @param start
     * @param end
     * @return
     */
    Boolean data(String site_code, String start, String end);

    /**
     * 默认方法，鼎湖山
     * 此接口只用于测试data的合理性，为了防止obs重复注入多次而设置
     *   遍历start到end日期内的所有数据
     *  如果是真实的，就获取真实的
     *  如果获取不到台站真实数据，拿前3天平均值替代，并用mock字段标注为mock
     *  存储在historyDriver表中
     * @param start
     * @param end
     * @param obsfile
     * @param mode
     * @return
     */
    Boolean data(String start, String end, String obsfile, int mode);

    /**
     * 默认方法  只更新站点的 FC——MASS参数，并写入到文件  不是数据库
     * 此接口只用于测试data的合理性，为了防止obs重复注入多次而设置
     *   遍历start到end日期内的所有数据
     *  如果是真实的，就获取真实的
     *  如果获取不到台站真实数据，拿前3天平均值替代，并用mock字段标注为mock
     *  存储在historyDriver表中
     * @param start
     * @param end
     * @param obsfile
     * @return
     */
    Boolean updateFCMASS(String start, String end, String obsfile, String sitecode);

    /**
     * 运行一遍未成功运行的天数的say
     * @param     未运行的日期 2022-2-2类似
     * @return
     */
    Boolean mockNoRunDaySay(LocalDateTime createTime);
    /**
     * 运行一遍未成功运行的天数的say
     * @param     未运行的日期 2022-2-2类似
     * @return
     */
    Boolean mockNoRunDaySay(LocalDateTime createTime,String sitecode);
}
