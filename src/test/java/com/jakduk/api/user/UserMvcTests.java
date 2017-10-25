package com.jakduk.api.user;

import com.jakduk.api.TestMvcConfig;
import com.jakduk.api.common.Constants;
import com.jakduk.api.common.constraint.ExistEmailValidator;
import com.jakduk.api.common.rabbitmq.RabbitMQPublisher;
import com.jakduk.api.common.util.AuthUtils;
import com.jakduk.api.common.util.ObjectMapperUtils;
import com.jakduk.api.configuration.security.JakdukAuthority;
import com.jakduk.api.model.db.FootballClub;
import com.jakduk.api.model.db.FootballClubOrigin;
import com.jakduk.api.model.db.User;
import com.jakduk.api.model.db.UserPicture;
import com.jakduk.api.model.embedded.LocalName;
import com.jakduk.api.restcontroller.UserRestController;
import com.jakduk.api.restcontroller.vo.EmptyJsonResponse;
import com.jakduk.api.restcontroller.vo.board.WriteArticle;
import com.jakduk.api.restcontroller.vo.user.UserForm;
import com.jakduk.api.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.constraints.ResourceBundleConstraintDescriptionResolver;
import org.springframework.restdocs.constraints.ValidatorConstraintResolver;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author pyohwan
 * 16. 6. 30 오후 11:55
 */

@RunWith(SpringRunner.class)
@WebMvcTest(UserRestController.class)
@Import(TestMvcConfig.class)
@AutoConfigureRestDocs(outputDir = "build/snippets")
public class UserMvcTests {

    @Autowired
    private MockMvc mvc;

    @MockBean private RestTemplateBuilder restTemplateBuilder;

    @MockBean private AuthenticationManager authenticationManager;
    @MockBean private UserService userService;
    @MockBean private PasswordEncoder passwordEncoder;
    @MockBean private RabbitMQPublisher rabbitMQPublisher;
    @MockBean private UserDetailsService userDetailsService;

    @Before
    public void setUp(){
    }

    @Test
    @WithMockUser
    public void createJakdukUser() throws Exception {

        FootballClubOrigin footballClubOrigin = FootballClubOrigin.builder()
                .id("54e1d2a58bf86df3fe819871")
                .name("SEONGNAM")
                .clubType(Constants.CLUB_TYPE.FOOTBALL_CLUB)
                .age(Constants.CLUB_AGE.SENIOR)
                .sex(Constants.CLUB_SEX.MEN)
                .build();

        FootballClub footballClub = FootballClub.builder()
                .id("54e1d2c68bf86df3fe819874")
                .origin(footballClubOrigin)
                .active("active")
                .names(Arrays.asList(
                        new LocalName(Locale.KOREAN.getLanguage(), "성남FC", "성남"),
                        new LocalName(Locale.ENGLISH.getLanguage(), "SEONGNAM FC", "SEONGNAM")
                ))
                .build();

        UserPicture userPicture = UserPicture.builder()
                .id("58b3bd86d6d83b06dac29a69")
                .status(Constants.GALLERY_STATUS_TYPE.ENABLE)
                .contentType("image/png")
                .build();

        UserForm form = UserForm.builder()
                .email("example@jakduk.com")
                .username("JakdukUser")
                .password("1111")
                .passwordConfirm("1111")
                .about("안녕하세요.")
                .footballClub(footballClub.getId())
                .userPictureId(userPicture.getId())
                .build();

        User expectUser = User.builder()
                .id("597df86caaf4fc0545d4f3e9")
                .email(form.getEmail())
                .username(form.getUsername())
                .password("841db2bc28e4730906bd82d79e69c80633747570d96ffade7dd77f58270f31a222e129e005cb70d2")
                .providerId(Constants.ACCOUNT_TYPE.JAKDUK)
                .about(form.getAbout())
                .roles(Arrays.asList(JakdukAuthority.ROLE_USER_01.getCode()))
                .supportFC(footballClub)
                .lastLogged(LocalDateTime.now())
                .build();

        when(userService.createJakdukUser(anyString(), anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(expectUser);

        ConstraintDescriptions userConstraints = new ConstraintDescriptions(UserForm.class, new ValidatorConstraintResolver(),
                new ResourceBundleConstraintDescriptionResolver(ResourceBundle.getBundle("ValidationMessages")));

        mvc.perform(post("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapperUtils.writeValueAsString(form)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(ObjectMapperUtils.writeValueAsString(EmptyJsonResponse.newInstance())))
                .andDo(
                        document("create-jakduk-user",
                                requestFields(
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일 주소. " +
                                                userConstraints.descriptionsForProperty("email")),
                                        fieldWithPath("username").type(JsonFieldType.STRING).description("별명. " +
                                                userConstraints.descriptionsForProperty("username")),
                                        fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호. " +
                                                userConstraints.descriptionsForProperty("password")),
                                        fieldWithPath("passwordConfirm").type(JsonFieldType.STRING).description("확인 비밀번호. " +
                                                userConstraints.descriptionsForProperty("passwordConfirm")),
                                        fieldWithPath("footballClub").type(JsonFieldType.STRING).description("(optional) 축구단 ID"),
                                        fieldWithPath("about").type(JsonFieldType.STRING).description("(optional) 자기 소개"),
                                        fieldWithPath("userPictureId").type(JsonFieldType.STRING).description("(optional) 프로필 사진 ID")
                                ),
                                responseHeaders(
                                        headerWithName("Set-Cookie").description("인증 쿠키. value는 JSESSIONID=키값").optional()
                                )
                        ));
    }
}
