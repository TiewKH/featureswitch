package com.featureswitch.featureswitch;

import com.featureswitch.featureswitch.entity.FeatureEntity;
import com.featureswitch.featureswitch.exceptions.DataNotFoundException;
import com.featureswitch.featureswitch.repository.FeatureRepository;
import com.featureswitch.featureswitch.service.feature.FeatureService;
import com.featureswitch.featureswitch.service.feature.FeatureServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FeatureServiceTest {

    @Mock
    private FeatureRepository featureRepository;

    private FeatureService featureService;

    @Before
    public void setUp() throws Exception {
        featureService = new FeatureServiceImpl(featureRepository);
    }

    @Test
    public void testGetFeatureByNameSuccessfully() throws Exception {
        FeatureEntity featureEntity = new FeatureEntity(1L, "Test Feature", null);

        when(featureRepository.findByFeatureName(any())).thenReturn(featureEntity);

        FeatureEntity findFeatureEntity = featureService.getFeatureByName(featureEntity.getFeatureName());
        assertThat(findFeatureEntity).isNotNull();
        assertThat(findFeatureEntity).isEqualTo(featureEntity);
    }

    @Test
    public void testGetFeatureByNameWhichDoesNotExist() throws Exception {
        FeatureEntity featureEntity = new FeatureEntity(1L, "Test Feature", null);

        when(featureRepository.findByFeatureName(any())).thenReturn(null);

        assertThrows(DataNotFoundException.class, () -> {
            featureService.getFeatureByName(featureEntity.getFeatureName());
        });
    }
}
