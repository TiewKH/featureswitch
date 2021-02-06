package com.featureswitch.featureswitch.service.feature;

import com.featureswitch.featureswitch.entity.FeatureEntity;
import com.featureswitch.featureswitch.exceptions.DataNotFoundException;
import com.featureswitch.featureswitch.repository.FeatureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeatureServiceImpl implements FeatureService {
    private final FeatureRepository featureRepository;

    @Override
    public FeatureEntity getFeatureByName(String featureName) throws DataNotFoundException {
        FeatureEntity featureEntity = featureRepository.findByFeatureName(featureName);
        if (featureEntity == null) {
            throw new DataNotFoundException("No feature found with the name: " + featureName);
        }

        return featureEntity;
    }

}
