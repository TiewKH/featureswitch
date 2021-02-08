package com.featureswitch.featureswitch.model;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class AddPermissionRequest {
    @NonNull
    private String featureName;

    @NonNull
    private String email;

    @NonNull
    private Boolean enable;
}
