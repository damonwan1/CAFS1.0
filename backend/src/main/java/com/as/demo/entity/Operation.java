package com.as.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 操作记录表
 * </p>
 *
 * @author yule
 * @since 2021-05-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Operation implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 精度
     */
    private String longitude;
    /**
     * 纬度
     */
    private String latitude;
    /**
     * 中文名称：如鼎湖山
     */
    private String title;
    /**
     * 城市地理位置 如肇庆市
     */
    private String city;
    /**
     * 数据中心编码,如DHF
     */


    /**
     * 是否开启 1开启  0关闭
     */
    private String open;
    /**
     * 数据中心编码,如DHF
     */
    private String code;

    /**
     * 创建时间
     */
    private Long createTime;


}
