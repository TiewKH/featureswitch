package com.featureswitch.featureswitch.controller;

import com.featureswitch.featureswitch.exceptions.DataNotFoundException;
import com.featureswitch.featureswitch.model.AddPermissionRequest;
import com.featureswitch.featureswitch.model.GetPermissionResponse;
import com.featureswitch.featureswitch.service.userfeature.UserFeatureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/feature")
@Slf4j
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
    public ResponseEntity<Void> addPermission(@RequestBody AddPermissionRequest addPermissionRequest) throws DataNotFoundException {
        log.debug(addPermissionRequest.toString());
        userFeatureService.updatePermissionByUserEmailAndFeatureName(addPermissionRequest.getEmail(), addPermissionRequest.getFeatureName(), addPermissionRequest.isEnable());
        return ResponseEntity.ok().build();
    }
}
