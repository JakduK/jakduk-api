package com.jakduk.api.user;

import com.jakduk.api.common.rabbitmq.RabbitMQPublisher;
import com.jakduk.api.common.util.ObjectMapperUtils;
import com.jakduk.api.model.db.User;
import com.jakduk.api.restcontroller.UserRestController;
import com.jakduk.api.restcontroller.vo.user.UserForm;
import com.jakduk.api.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author pyohwan
 * 16. 6. 30 오후 11:55
 */

@RunWith(SpringRunner.class)
@WebMvcTest(UserRestController.class)
@WithMockUser
public class UserControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean private AuthenticationManager authenticationManager;
    @MockBean private UserService userService;
    @MockBean private PasswordEncoder passwordEncoder;
    @MockBean private RabbitMQPublisher rabbitMQPublisher;
    @MockBean private UserDetailsService userDetailsService;

    @Before
    public void setUp(){
    }

    @Test
    public void createJakdukUser() throws Exception {
        UserForm form = UserForm.builder()
                .email("example@jakduk.com")
                .username("JakdukUser")
                .password("1111")
                .passwordConfirm("1111")
                .build();

        User expectUser = User.builder()
                .id("abcdef")
                .email(form.getEmail())
                .username(form.getUsername())
                .build();

        given(userService.addJakdukUser(form.getEmail(), form.getUsername(), passwordEncoder.encode(form.getPassword()), null, null, null))
                .willReturn(expectUser);

        mvc.perform(post("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapperUtils.writeValueAsString(form)))
                .andExpect(status().isOk());
    }
}
