package com.as.demo.entity.dto;

import java.util.List;


import lombok.Data;

@Data
public class ActivityDto {
    private String stationCode;
    private String stationName;
    private List<ActivityItemDto> activityMeasureList;
}
