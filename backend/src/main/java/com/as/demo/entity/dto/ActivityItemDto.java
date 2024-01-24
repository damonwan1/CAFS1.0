package com.as.demo.entity.dto;

import java.util.List;


import lombok.Data;

@Data
public class ActivityItemDto {
    private String activityCode;
    private String activityName;
    private List<ActivityItemMeasureDto> measureList;
}
