package com.featureswitch.featureswitch.service.feature;

import com.featureswitch.featureswitch.entity.FeatureEntity;
import com.featureswitch.featureswitch.repository.FeatureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeatureServiceImpl implements FeatureService {
    private final FeatureRepository featureRepository;

    @Override
    public FeatureEntity getFeatureByName(String featureName) {
        return featureRepository.findByFeatureName(featureName);
    }

}
