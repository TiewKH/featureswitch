package com.featureswitch.featureswitch.model;

import lombok.Data;

@Data
public class AddPermissionRequest {
    private String featureName;
    private String email;
    private boolean enable;
}
