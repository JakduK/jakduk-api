package com.jakduk.api.user;

import com.jakduk.api.restcontroller.UserRestController;
import com.jakduk.api.restcontroller.vo.user.SocialUserForm;
import com.jakduk.api.util.AbstractMvcTest;
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
 * @author pyohwan
 * 16. 6. 30 오후 11:55
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

    @Test
    public void createSnsUser() throws Exception {
        SocialUserForm form = SocialUserForm.builder()
                .email("example@jakduk.com")
                .username("JakdukUser")
                .build();

        mockMvc.perform(post("/api/user/social")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(form)))
                .andExpect(status().isOk());
    }
}
