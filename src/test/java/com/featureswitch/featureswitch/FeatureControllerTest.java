package com.featureswitch.featureswitch;

import com.featureswitch.featureswitch.controller.FeatureController;
import com.featureswitch.featureswitch.exceptions.DataNotFoundException;
import com.featureswitch.featureswitch.service.userfeature.UserFeatureService;

import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.mock.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@WebMvcTest(FeatureController.class)
public class FeatureControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserFeatureService userFeatureService;

    private final String testEmail = "test@gmail.com";
    private final String testFeature = "Test Feature";

    @Test
    public void testGetEnabledUser() throws Exception {
        given(userFeatureService.userHasPermission(testEmail, testFeature)).willReturn(true);
        mockMvc.perform(get("/feature")
                .param("email", testEmail)
                .param("featureName", testFeature)
        ).andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.canAccess").value(true));
    }

    @Test
    public void testGetDisabledUser() throws Exception {
        given(userFeatureService.userHasPermission(testEmail, testFeature)).willReturn(false);
        mockMvc.perform(get("/feature")
                .param("email", testEmail)
                .param("featureName", testFeature)
        ).andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.canAccess").value(false));
    }

    @Test
    public void testDataNotFound() throws Exception {
        given(userFeatureService.userHasPermission(any(), any())).willThrow(DataNotFoundException.class);
        mockMvc.perform(get("/feature")
                .param("email", testEmail)
                .param("featureName", testFeature)
        ).andExpect(status().is(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    public void testBadRequest() throws Exception {
        mockMvc.perform(get("/feature")
                .param("email", testEmail)
        ).andExpect(status().is(HttpStatus.BAD_REQUEST.value()));

        mockMvc.perform(get("/feature")
                .param("featureName", testFeature)
        ).andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }
}
