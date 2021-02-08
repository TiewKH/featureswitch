package com.featureswitch.featureswitch;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.featureswitch.featureswitch.controller.FeatureController;
import com.featureswitch.featureswitch.entity.UserFeatureEntity;
import com.featureswitch.featureswitch.exceptions.AddFailedException;
import com.featureswitch.featureswitch.exceptions.DataNotFoundException;
import com.featureswitch.featureswitch.model.AddPermissionRequest;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

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

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testGetPermissionPermissionEnabledUser() throws Exception {
        given(userFeatureService.userHasPermission(testEmail, testFeature)).willReturn(true);
        mockMvc.perform(get("/feature")
                .param("email", testEmail)
                .param("featureName", testFeature)
        ).andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.canAccess").value(true));
    }

    @Test
    public void testGetPermissionDisabledUser() throws Exception {
        given(userFeatureService.userHasPermission(testEmail, testFeature)).willReturn(false);
        mockMvc.perform(get("/feature")
                .param("email", testEmail)
                .param("featureName", testFeature)
        ).andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.canAccess").value(false));
    }

    @Test
    public void testGetPermissionDataNotFound() throws Exception {
        given(userFeatureService.userHasPermission(any(), any())).willThrow(DataNotFoundException.class);
        mockMvc.perform(get("/feature")
                .param("email", testEmail)
                .param("featureName", testFeature)
        ).andExpect(status().is(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    public void testGetPermissionBadRequest() throws Exception {
        mockMvc.perform(get("/feature")
                .param("email", testEmail)
        ).andExpect(status().is(HttpStatus.BAD_REQUEST.value()));

        mockMvc.perform(get("/feature")
                .param("featureName", testFeature)
        ).andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    public void testAddPermissionAddSuccess() throws Exception {
        AddPermissionRequest addPermissionRequest = new AddPermissionRequest(testEmail, testFeature, Boolean.TRUE);

        UserFeatureEntity userFeatureEntity = new UserFeatureEntity();
        given(userFeatureService.addPermissionByUserEmailAndFeatureName(testEmail, testFeature, true))
                .willReturn(userFeatureEntity);

        mockMvc.perform(post("/feature")
                .contentType("application/json")
                .content(asJsonString(addPermissionRequest))
        ).andExpect(status().is(HttpStatus.OK.value())).andDo(print());
    }

    @Test
    public void testAddPermissionAddFailed() throws Exception {
        AddPermissionRequest addPermissionRequest = new AddPermissionRequest(testEmail, testFeature, Boolean.TRUE);

        given(userFeatureService.addPermissionByUserEmailAndFeatureName(any(), any(), anyBoolean())).willThrow(AddFailedException.class);

        System.out.println(asJsonString(addPermissionRequest));
        mockMvc.perform(post("/feature")
                .contentType("application/json")
                .content(asJsonString(addPermissionRequest))
        ).andExpect(status().is(HttpStatus.NOT_MODIFIED.value())).andDo(print());
    }

    @Test
    public void testAddPermissionBadRequest() throws Exception {
        String jsonAddPermissionRequestFailure = "{\"featureName\":\"test@gmail.com\",\"email\":\"Test Feature\",\"enableWrongFieldName\":true}";

        mockMvc.perform(post("/feature")
                .contentType("application/json")
                .content(jsonAddPermissionRequestFailure)
        ).andExpect(status().is(HttpStatus.BAD_REQUEST.value())).andDo(print());
    }
}
