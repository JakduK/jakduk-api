package com.jakduk.api.auth;

import com.jakduk.api.common.Constants;
import com.jakduk.api.common.util.AuthUtils;
import com.jakduk.api.common.util.ObjectMapperUtils;
import com.jakduk.api.configuration.security.JakdukAuthority;
import com.jakduk.api.restcontroller.AuthRestController;
import com.jakduk.api.restcontroller.vo.EmptyJsonResponse;
import com.jakduk.api.restcontroller.vo.user.AuthUserProfile;
import com.jakduk.api.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest({AuthRestController.class, AuthMvcTests.Controller.class})
@AutoConfigureRestDocs(outputDir = "build/snippets")
public class AuthMvcTests {

    @Autowired
    private MockMvc mvc;

    @MockBean private RestTemplateBuilder restTemplateBuilder;
    @MockBean private UserService userService;
    @MockBean private AuthUtils authUtils;

    @Before
    public void setup() {
    }

    @Test
    @WithMockUser
    public void formLoginTest() throws Exception {
        mvc.perform(
                post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("username", "test07@test.com")
                        .param("password", "1111")
                        .param("remember-me", "true"))
                .andExpect(status().isOk())
                .andExpect(cookie().exists("JESSIONID"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(ObjectMapperUtils.writeValueAsString(EmptyJsonResponse.newInstance())))
                .andDo(
                        document("login-jakduk-user",
                                requestParameters(
                                        parameterWithName("username").description("이메일 주소"),
                                        parameterWithName("password").description("비밀번호"),
                                        parameterWithName("remember-me").description("(optional) 로그인 유지 여부")
                                ),
                                responseHeaders(
                                        headerWithName("Set-Cookie").description("인증 쿠키. value는 JESSIONID=키값").optional()
                                )
                        ));

    }

    @Test
    @WithMockJakdukUser
    public void getMySessionProfileTest() throws Exception {

        AuthUserProfile expectResponse = AuthUtils.getAuthUserProfile();

        mvc.perform(
                get("/api/auth/user")
                        .header("Cookie", "JSESSIONID=3F0E029648484BEAEF6B5C3578164E99")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(ObjectMapperUtils.writeValueAsString(expectResponse)))
                .andDo(
                        document("getMySessionUser",
                                requestHeaders(
                                        headerWithName("Cookie").description("(optional) 인증 쿠키. value는 JESSIONID=키값")
                                ),
                                responseFields(
                                        fieldWithPath("id").type(JsonFieldType.STRING).description("회원 ID"),
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일 주소"),
                                        fieldWithPath("username").type(JsonFieldType.STRING).description("별명"),
                                        fieldWithPath("providerId").type(JsonFieldType.STRING).description("계정 분류 " +
                                                Stream.of(Constants.ACCOUNT_TYPE.values()).map(Enum::name).collect(Collectors.toList())),
                                        fieldWithPath("roles").type(JsonFieldType.ARRAY).description("권한 목록 " +
                                                Stream.of(JakdukAuthority.values()).map(Enum::name).collect(Collectors.toList())),
                                        fieldWithPath("picture").type(JsonFieldType.OBJECT).description("회원 사진"),
                                        fieldWithPath("picture.id").type(JsonFieldType.STRING).description("회원 사진 ID"),
                                        fieldWithPath("picture.sourceType").type(JsonFieldType.STRING).description("계정 분류 " +
                                                Stream.of(Constants.ACCOUNT_TYPE.values()).map(Enum::name).collect(Collectors.toList())),
                                        fieldWithPath("picture.smallPictureUrl").type(JsonFieldType.STRING).description("회원 작은 사진 URL"),
                                        fieldWithPath("picture.largePictureUrl").type(JsonFieldType.STRING).description("회원 큰 사진 URL")
                                )
                        ));

    }

    @RestController
    static class Controller {

        @PostMapping(value = "/api/auth/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        public EmptyJsonResponse formLogin(HttpServletResponse servletResponse) {
            servletResponse.addCookie(new Cookie("JESSIONID", "3F0E029648484BEAEF6B5C3578164E99"));
            return EmptyJsonResponse.newInstance();
        }
    }
}
