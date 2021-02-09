package com.featureswitch.featureswitch.controller;

import com.featureswitch.featureswitch.exceptions.DataNotFoundException;
import com.featureswitch.featureswitch.exceptions.AddFailedException;
import com.featureswitch.featureswitch.model.AddPermissionRequest;
import com.featureswitch.featureswitch.model.GetPermissionResponse;
import com.featureswitch.featureswitch.service.userfeature.UserFeatureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/feature")
public class FeatureController {

    private final UserFeatureService userFeatureService;

    @GetMapping
    public ResponseEntity<GetPermissionResponse> getPermission(@RequestParam String email, @RequestParam String featureName) throws DataNotFoundException {
        boolean isEnabled = userFeatureService.userHasPermission(email, featureName);
        GetPermissionResponse getPermissionResponse = new GetPermissionResponse();
        getPermissionResponse.setCanAccess(isEnabled);
        return ResponseEntity.ok(getPermissionResponse);
    }

    @PostMapping
    public ResponseEntity<Void> addPermission(@Validated @RequestBody AddPermissionRequest addPermissionRequest) throws AddFailedException {
        userFeatureService.addPermissionByUserEmailAndFeatureName(addPermissionRequest.getEmail(), addPermissionRequest.getFeatureName(), addPermissionRequest.getEnable().booleanValue());
        return ResponseEntity.ok().build();
    }
}
