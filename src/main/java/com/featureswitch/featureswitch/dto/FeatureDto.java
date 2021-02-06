package com.featureswitch.featureswitch.dto;

import lombok.Data;

@Data
public class FeatureDto {
    private String featureName;
    private String email;
    private boolean enable;
}
