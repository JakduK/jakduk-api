package com.jakduk.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jakduk.restcontroller.UserRestController;
import com.jakduk.restcontroller.vo.UserProfileForm;
import com.jakduk.util.AbstractMvcTest;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by pyohwan on 16. 6. 30.
 */

@RunWith(MockitoJUnitRunner.class)
@Transactional
public class UserControllerTest extends AbstractMvcTest<UserRestController> {

    @InjectMocks
    private UserRestController sut;

    @Before
    public void setUp() {
        initMvc(sut);
    }

    @Ignore
    @Test
    public void 소셜기반회원가입() throws Exception {
        UserProfileForm form = new UserProfileForm();

        mockMvc.perform(post("/api/user/facebook")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(form)))
                .andExpect(status().isOk());
    }
}
