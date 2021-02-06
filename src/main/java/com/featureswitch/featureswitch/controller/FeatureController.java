package com.featureswitch.featureswitch.controller;

import com.featureswitch.featureswitch.dto.FeatureDto;
import com.featureswitch.featureswitch.dto.GetPermissionResponse;
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
    public ResponseEntity<GetPermissionResponse> getPermission(@RequestParam String email, @RequestParam String featureName) {
        boolean isEnabled = userFeatureService.userIsEnabled(email, featureName);
        GetPermissionResponse getPermissionResponse = new GetPermissionResponse();
        getPermissionResponse.setEnabled(isEnabled);
        return ResponseEntity.ok(getPermissionResponse);
    }

    @PostMapping
    public ResponseEntity<Void> addPermission(@RequestBody FeatureDto featureDto) {
        log.debug(featureDto.toString());
        if (userFeatureService.updateByUserEmailAndFeatureName(
                featureDto.getEmail(), featureDto.getFeatureName(), featureDto.isEnable()) != null) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(304).build();
        }
    }
}
