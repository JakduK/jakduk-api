package com.jakduk.api.user;

import com.jakduk.api.TestMvcConfig;
import com.jakduk.api.WithMockJakdukUser;
import com.jakduk.api.common.Constants;
import com.jakduk.api.common.rabbitmq.RabbitMQPublisher;
import com.jakduk.api.common.util.JakdukUtils;
import com.jakduk.api.common.util.ObjectMapperUtils;
import com.jakduk.api.configuration.security.JakdukAuthority;
import com.jakduk.api.model.db.FootballClub;
import com.jakduk.api.model.db.FootballClubOrigin;
import com.jakduk.api.model.db.User;
import com.jakduk.api.model.db.UserPicture;
import com.jakduk.api.model.embedded.LocalName;
import com.jakduk.api.model.embedded.UserPictureInfo;
import com.jakduk.api.model.simple.UserOnPasswordUpdate;
import com.jakduk.api.repository.user.UserProfileRepository;
import com.jakduk.api.restcontroller.UserRestController;
import com.jakduk.api.restcontroller.vo.EmptyJsonResponse;
import com.jakduk.api.restcontroller.vo.user.*;
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
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.constraints.ResourceBundleConstraintDescriptionResolver;
import org.springframework.restdocs.constraints.ValidatorConstraintResolver;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    @MockBean private RabbitMQPublisher rabbitMQPublisher;
    @MockBean private UserDetailsService userDetailsService;
    @MockBean private UserProfileRepository userProfileRepository;

    private FootballClub footballClub;
    private UserPicture userPicture;
    private User jakdukUser;

    @Before
    public void setUp(){
        FootballClubOrigin footballClubOrigin = FootballClubOrigin.builder()
                .id("54e1d2a58bf86df3fe819871")
                .name("SEONGNAM")
                .clubType(Constants.CLUB_TYPE.FOOTBALL_CLUB)
                .age(Constants.CLUB_AGE.SENIOR)
                .sex(Constants.CLUB_SEX.MEN)
                .build();

        this.footballClub = FootballClub.builder()
                .id("54e1d2c68bf86df3fe819874")
                .origin(footballClubOrigin)
                .active("active")
                .names(Arrays.asList(
                        new LocalName(Locale.KOREAN.getLanguage(), "성남FC", "성남"),
                        new LocalName(Locale.ENGLISH.getLanguage(), "SEONGNAM FC", "SEONGNAM")
                ))
                .build();

        this.userPicture = UserPicture.builder()
                .id("58b3bd86d6d83b06dac29a69")
                .status(Constants.GALLERY_STATUS_TYPE.ENABLE)
                .contentType("image/png")
                .build();

        jakdukUser = User.builder()
                .id("597df86caaf4fc0545d4f3e9")
                .email("example@jakduk.com")
                .username("JakdukUser")
                .password("841db2bc28e4730906bd82d79e69c80633747570d96ffade7dd77f58270f31a222e129e005cb70d2")
                .providerId(Constants.ACCOUNT_TYPE.JAKDUK)
                .about("안녕하세요.")
                .roles(Arrays.asList(JakdukAuthority.ROLE_USER_01.getCode()))
                .supportFC(footballClub)
                .lastLogged(LocalDateTime.now())
                .build();
    }

    @Test
    @WithMockUser
    public void createJakdukUserTest() throws Exception {

        this.whenCustomValdation();

        UserForm form = UserForm.builder()
                .email(jakdukUser.getEmail())
                .username(jakdukUser.getUsername())
                .password("1111")
                .passwordConfirm("1111")
                .about(jakdukUser.getAbout())
                .footballClub(footballClub.getId())
                .userPictureId(userPicture.getId())
                .build();

        when(userService.createJakdukUser(anyString(), anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(jakdukUser);

        ConstraintDescriptions userConstraints = new ConstraintDescriptions(UserForm.class, new ValidatorConstraintResolver(),
                new ResourceBundleConstraintDescriptionResolver(ResourceBundle.getBundle("ValidationMessages")));

        mvc.perform(post("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
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

    @Test
    @WithMockUser
    public void createSocialUserTest() throws Exception {

        this.whenCustomValdation();

        AttemptSocialUser attemptSocialUser = AttemptSocialUser.builder()
                .username("daumUser01")
                .providerId(Constants.ACCOUNT_TYPE.DAUM)
                .providerUserId("abc123")
                .externalSmallPictureUrl("https://img1.daumcdn.net/thumb/R55x55/?fname=http%3A%2F%2Ftwg.tset.daumcdn.net%2Fprofile%2F6enovyMT1pI0&t=1507478752861")
                .externalLargePictureUrl("https://img1.daumcdn.net/thumb/R158x158/?fname=http%3A%2F%2Ftwg.tset.daumcdn.net%2Fprofile%2F6enovyMT1pI0&t=1507478752861")
                .build();

        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute(Constants.PROVIDER_SIGNIN_ATTEMPT_SESSION_ATTRIBUTE, attemptSocialUser);

        SocialUserForm form = SocialUserForm.builder()
                .email("example@jakduk.com")
                .username("SocialUser")
                .about("안녕하세요.")
                .footballClub(footballClub.getId())
                .userPictureId(userPicture.getId())
                .externalLargePictureUrl("https://img1.daumcdn.net/thumb/R158x158/?fname=http%3A%2F%2Ftwg.tset.daumcdn.net%2Fprofile%2FSjuNejHmr8o0&t=1488000722876")
                .build();

        User expectUser = User.builder()
                .id("597df86caaf4fc0545d4f3e9")
                .email(form.getEmail())
                .username(form.getUsername())
                .password("841db2bc28e4730906bd82d79e69c80633747570d96ffade7dd77f58270f31a222e129e005cb70d2")
                .providerId(attemptSocialUser.getProviderId())
                .providerUserId(attemptSocialUser.getProviderUserId())
                .about(form.getAbout())
                .roles(Arrays.asList(JakdukAuthority.ROLE_USER_02.getCode()))
                .supportFC(footballClub)
                .lastLogged(LocalDateTime.now())
                .build();

        when(userService.createSocialUser(anyString(), anyString(), any(Constants.ACCOUNT_TYPE.class), anyString(),
                anyString(), anyString(), anyString(), anyString()))
                .thenReturn(expectUser);

        ConstraintDescriptions userConstraints = new ConstraintDescriptions(SocialUserForm.class, new ValidatorConstraintResolver(),
                new ResourceBundleConstraintDescriptionResolver(ResourceBundle.getBundle("ValidationMessages")));

        mvc.perform(post("/api/user/social")
                .session(mockHttpSession)
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapperUtils.writeValueAsString(form)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(ObjectMapperUtils.writeValueAsString(EmptyJsonResponse.newInstance())))
                .andDo(
                        document("create-sns-user",
                                requestFields(
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일 주소. " +
                                                userConstraints.descriptionsForProperty("email")),
                                        fieldWithPath("username").type(JsonFieldType.STRING).description("별명. " +
                                                userConstraints.descriptionsForProperty("username")),
                                        fieldWithPath("footballClub").type(JsonFieldType.STRING).description("(optional) 축구단 ID"),
                                        fieldWithPath("about").type(JsonFieldType.STRING).description("(optional) 자기 소개"),
                                        fieldWithPath("userPictureId").type(JsonFieldType.STRING).description("(optional) 프로필 사진 ID"),
                                        fieldWithPath("externalLargePictureUrl").type(JsonFieldType.STRING)
                                                .description("(optional) SNS계정에서 제공하는 회원 큰 사진 URL. userPictureId가 null 이어야 한다.")
                                ),
                                responseHeaders(
                                        headerWithName("Set-Cookie").description("인증 쿠키. value는 JSESSIONID=키값").optional()
                                )
                        ));
    }

    @Test
    @WithMockUser
    public void existEmailTest() throws Exception {

        when(userProfileRepository.findOneByEmail(anyString()))
                .thenReturn(Optional.empty());

        mvc.perform(
                get("/api/user/exist/email")
                        .param("email", "example@jakduk.com")
                        .header("Cookie", "JSESSIONID=3F0E029648484BEAEF6B5C3578164E99")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(ObjectMapperUtils.writeValueAsString(EmptyJsonResponse.newInstance())))
                .andDo(
                        document("exist-email",
                                requestHeaders(
                                        headerWithName("Cookie").description("(optional) 인증 쿠키. value는 JSESSIONID=키값")
                                ),
                                requestParameters(
                                        parameterWithName("email").description("이메일 주소")
                                )
                        ));
    }

    @Test
    @WithMockUser
    public void existUsernameTest() throws Exception {

        when(userProfileRepository.findOneByUsername(anyString()))
                .thenReturn(Optional.empty());

        mvc.perform(
                get("/api/user/exist/username")
                        .param("username", "JakdukUser")
                        .header("Cookie", "JSESSIONID=3F0E029648484BEAEF6B5C3578164E99")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(ObjectMapperUtils.writeValueAsString(EmptyJsonResponse.newInstance())))
                .andDo(
                        document("exist-username",
                                requestHeaders(
                                        headerWithName("Cookie").description("(optional) 인증 쿠키. value는 JSESSIONID=키값")
                                ),
                                requestParameters(
                                        parameterWithName("username").description("별명")
                                )
                        ));
    }

    @Test
    @WithMockJakdukUser
    public void getProfileMeTest() throws Exception {

        String language = JakdukUtils.getLanguageCode();

        UserProfileResponse expectResponse = UserProfileResponse.builder()
                .email(jakdukUser.getEmail())
                .username(jakdukUser.getUsername())
                .about(jakdukUser.getAbout())
                .providerId(jakdukUser.getProviderId())
                .footballClubName(JakdukUtils.getLocalNameOfFootballClub(footballClub, language))
                .picture(
                        new UserPictureInfo(
                                "597a0d53807d710f57420aa5",
                                "https://dev-api.jakduk.com/user/picture/small/597a0d53807d710f57420aa5",
                                "https://dev-api.jakduk.com/user/picture/597a0d53807d710f57420aa5"
                        ))
                .temporaryEmail(false)
                .build();

        when(userService.getProfileMe(anyString(), anyString()))
                .thenReturn(expectResponse);

        mvc.perform(
                get("/api/user/profile/me")
                        .param("username", "JakdukUser")
                        .header("Cookie", "JSESSIONID=3F0E029648484BEAEF6B5C3578164E99")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(ObjectMapperUtils.writeValueAsString(expectResponse)))
                .andDo(
                        document("user-get-profile-me",
                                requestHeaders(
                                        headerWithName("Cookie").description("인증 쿠키. value는 JSESSIONID=키값")
                                ),
                                responseFields(
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일 주소"),
                                        fieldWithPath("username").type(JsonFieldType.STRING).description("별명"),
                                        fieldWithPath("providerId").type(JsonFieldType.STRING).description("계정 분류 " +
                                                Stream.of(Constants.ACCOUNT_TYPE.values()).map(Enum::name).collect(Collectors.toList())),
                                        fieldWithPath("about").type(JsonFieldType.STRING).description("소개"),
                                        fieldWithPath("footballClubName").type(JsonFieldType.OBJECT).description("지지 축구단"),
                                        fieldWithPath("footballClubName.language").type(JsonFieldType.STRING).description("언어"),
                                        fieldWithPath("footballClubName.fullName").type(JsonFieldType.STRING).description("축구단 풀네임"),
                                        fieldWithPath("footballClubName.shortName").type(JsonFieldType.STRING).description("축구단 숏네임"),
                                        fieldWithPath("picture").type(JsonFieldType.OBJECT).description("회원 사진"),
                                        fieldWithPath("picture.id").type(JsonFieldType.STRING).description("회원 사진 ID"),
                                        fieldWithPath("picture.smallPictureUrl").type(JsonFieldType.STRING).description("회원 작은 사진 URL"),
                                        fieldWithPath("picture.largePictureUrl").type(JsonFieldType.STRING).description("회원 큰 사진 URL"),
                                        fieldWithPath("temporaryEmail").type(JsonFieldType.BOOLEAN).description("임시로 발급한 이메일인지 여부")
                                )
                        ));
    }

    @Test
    @WithMockJakdukUser
    public void editProfileMeTest() throws Exception {

        when(userProfileRepository.findByNEIdAndEmail(anyString(), anyString()))
                .thenReturn(Optional.empty());

        when(userProfileRepository.findByNEIdAndUsername(anyString(), anyString()))
                .thenReturn(Optional.empty());

        UserProfileEditForm form = UserProfileEditForm.builder()
                .email(jakdukUser.getEmail())
                .username(jakdukUser.getUsername())
                .about(jakdukUser.getAbout())
                .footballClub(footballClub.getId())
                .userPictureId(userPicture.getId())
                .build();

        when(userService.editUserProfile(anyString(), anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(jakdukUser);

        ConstraintDescriptions userConstraints = new ConstraintDescriptions(UserProfileEditForm.class, new ValidatorConstraintResolver(),
                new ResourceBundleConstraintDescriptionResolver(ResourceBundle.getBundle("ValidationMessages")));

        mvc.perform(
                put("/api/user/profile/me")
                        .header("Cookie", "JSESSIONID=3F0E029648484BEAEF6B5C3578164E99")
                        //.cookie(new Cookie("JSESSIONID", "3F0E029648484BEAEF6B5C3578164E99")) TODO 이걸로 바꾸고 싶다.
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(ObjectMapperUtils.writeValueAsString(form)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(ObjectMapperUtils.writeValueAsString(EmptyJsonResponse.newInstance())))
                .andDo(
                        document("user-edit-profile-me",
                                requestHeaders(
                                        headerWithName("Cookie").description("인증 쿠키. value는 JSESSIONID=키값")
                                ),
                                requestFields(
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일 주소. " +
                                                userConstraints.descriptionsForProperty("email")),
                                        fieldWithPath("username").type(JsonFieldType.STRING).description("별명. " +
                                                userConstraints.descriptionsForProperty("username")),
                                        fieldWithPath("footballClub").type(JsonFieldType.STRING).description("(optional) 축구단 ID"),
                                        fieldWithPath("about").type(JsonFieldType.STRING).description("(optional) 자기 소개"),
                                        fieldWithPath("userPictureId").type(JsonFieldType.STRING).description("(optional) 프로필 사진 ID")
                                )
                        ));
    }

    @Test
    @WithMockJakdukUser
    public void editPasswordTest() throws Exception {

        when(userService.findUserOnPasswordUpdateById(anyString()))
                .thenReturn(new UserOnPasswordUpdate(jakdukUser.getId(), jakdukUser.getPassword()));

        UserPasswordForm form = UserPasswordForm.builder()
                .password("1111")
                .newPassword("1112")
                .newPasswordConfirm("1112")
                .build();

        doNothing().when(userService)
                .updateUserPassword(anyString(), anyString());

        ConstraintDescriptions userConstraints = new ConstraintDescriptions(UserPasswordForm.class, new ValidatorConstraintResolver(),
                new ResourceBundleConstraintDescriptionResolver(ResourceBundle.getBundle("ValidationMessages")));

        mvc.perform(
                put("/api/user/password")
                        .header("Cookie", "JSESSIONID=3F0E029648484BEAEF6B5C3578164E99")
                        //.cookie(new Cookie("JSESSIONID", "3F0E029648484BEAEF6B5C3578164E99")) TODO 이걸로 바꾸고 싶다.
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(ObjectMapperUtils.writeValueAsString(form)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(ObjectMapperUtils.writeValueAsString(EmptyJsonResponse.newInstance())))
                .andDo(
                        document("user-edit-password",
                                requestHeaders(
                                        headerWithName("Cookie").description("인증 쿠키. value는 JSESSIONID=키값")
                                ),
                                requestFields(
                                        fieldWithPath("password").type(JsonFieldType.STRING).description("현재 비밀번호. " +
                                                userConstraints.descriptionsForProperty("password")),
                                        fieldWithPath("newPassword").type(JsonFieldType.STRING).description("새 비밀번호. " +
                                                userConstraints.descriptionsForProperty("newPassword")),
                                        fieldWithPath("newPasswordConfirm").type(JsonFieldType.STRING).description("확인 새 비밀번호. " +
                                                userConstraints.descriptionsForProperty("newPasswordConfirm"))
                                )
                        ));
    }

    @Test
    @WithMockUser
    public void uploadUserPictureTest() throws Exception {

        UserPicture expectUserPicture = UserPicture.builder()
                .id(userPicture.getId())
                .status(Constants.GALLERY_STATUS_TYPE.TEMP)
                .contentType(userPicture.getContentType())
                .build();

        when(userService.uploadUserPicture(anyString(), anyLong(), anyObject()))
                .thenReturn(expectUserPicture);

        byte[] ballBytes = new byte[]{
                (byte) 0x89, (byte) 0x50, (byte) 0x4e, (byte) 0x47, (byte) 0x0d, (byte) 0x0a, (byte) 0x1a, (byte) 0x0a, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x0d, (byte) 0x49, (byte) 0x48, (byte) 0x44, (byte) 0x52, (byte) 0x00, (byte) 0x00
        };

        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "sample.png", MediaType.IMAGE_PNG_VALUE, ballBytes);

        mvc.perform(fileUpload("/api/user/picture")
                .file(mockMultipartFile))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(ObjectMapperUtils.writeValueAsString(expectUserPicture)))
                .andDo(
                        document("user-upload-picture",
                                requestParts(
                                        partWithName("file").description("멀티 파트 객체")
                                ),
                                responseFields(
                                        fieldWithPath("id").type(JsonFieldType.STRING).description("그림 ID"),
                                        fieldWithPath("status").type(JsonFieldType.STRING).description("그림 상태. " +
                                                Stream.of(Constants.GALLERY_STATUS_TYPE.values()).map(Enum::name).collect(Collectors.toList())),
                                        fieldWithPath("contentType").type(JsonFieldType.STRING).description("Content-Type. image/* 만 가능하다.")
                                )
                        ));
    }

    @Test
    @WithMockJakdukUser
    public void deleteUserTest() throws Exception {

        doNothing().when(userService)
                .deleteUser(anyString());

        mvc.perform(
                delete("/api/user")
                        .header("Cookie", "JSESSIONID=3F0E029648484BEAEF6B5C3578164E99")
                        //.cookie(new Cookie("JSESSIONID", "3F0E029648484BEAEF6B5C3578164E99")) TODO 이걸로 바꾸고 싶다.
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(ObjectMapperUtils.writeValueAsString(EmptyJsonResponse.newInstance())))
                .andDo(
                        document("user-delete",
                                requestHeaders(
                                        headerWithName("Cookie").description("인증 쿠키. value는 JSESSIONID=키값")
                                )
                        ));
    }

    @Test
    @WithMockUser
    public void findPasswordTest() throws Exception {

        UserPasswordFindForm form = UserPasswordFindForm.builder()
                .email(jakdukUser.getEmail())
                .callbackUrl("http://dev-wev.jakduk/find/password")
                .build();

        UserPasswordFindResponse expectResponse = UserPasswordFindResponse.builder()
                .subject(form.getEmail())
                .message(JakdukUtils.getMessageSource("user.msg.reset.password.send.email"))
                .build();

        when(userService.sendEmailToResetPassword(anyString(), anyString()))
                .thenReturn(expectResponse);

        ConstraintDescriptions userConstraints = new ConstraintDescriptions(UserPasswordFindForm.class, new ValidatorConstraintResolver(),
                new ResourceBundleConstraintDescriptionResolver(ResourceBundle.getBundle("ValidationMessages")));

        mvc.perform(post("/api/user/password/find")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapperUtils.writeValueAsString(form)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(ObjectMapperUtils.writeValueAsString(expectResponse)))
                .andDo(
                        document("user-find-password",
                                requestFields(
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일 주소. " +
                                                userConstraints.descriptionsForProperty("email")),
                                        fieldWithPath("callbackUrl").type(JsonFieldType.STRING).description("콜백 받을 URL. " +
                                                userConstraints.descriptionsForProperty("callbackUrl"))
                                ),
                                responseFields(
                                        fieldWithPath("subject").type(JsonFieldType.STRING).description("html에서 쓰일 제목. 이메일 주소"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("html에서 쓰일 내용.")
                                )
                        ));
    }

    @Test
    @WithMockUser
    public void resetPasswordTest() throws Exception {

        UserPasswordResetForm form = UserPasswordResetForm.builder()
                .code("16948f83-1af8-4736-9e12-cf57f03a981c")
                .password("1112")
                .build();

        UserPasswordFindResponse expectResponse = UserPasswordFindResponse.builder()
                .subject(jakdukUser.getEmail())
                .message(JakdukUtils.getMessageSource("user.msg.success.change.password"))
                .build();

        when(userService.resetPasswordWithToken(anyString(), anyString()))
                .thenReturn(expectResponse);

        ConstraintDescriptions userConstraints = new ConstraintDescriptions(UserPasswordResetForm.class, new ValidatorConstraintResolver(),
                new ResourceBundleConstraintDescriptionResolver(ResourceBundle.getBundle("ValidationMessages")));

        mvc.perform(post("/api/user/password/reset")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapperUtils.writeValueAsString(form)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(ObjectMapperUtils.writeValueAsString(expectResponse)))
                .andDo(
                        document("user-reset-password",
                                requestFields(
                                        fieldWithPath("code").type(JsonFieldType.STRING).description("임시 발행한 토큰 코드. (5분간 유지) " +
                                                userConstraints.descriptionsForProperty("code")),
                                        fieldWithPath("password").type(JsonFieldType.STRING).description("바꿀 비밀번호 " +
                                                userConstraints.descriptionsForProperty("password"))
                                ),
                                responseFields(
                                        fieldWithPath("subject").type(JsonFieldType.STRING).description("html에서 쓰일 제목. 이메일 주소"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("html에서 쓰일 내용.")
                                )
                        ));
    }

    private void whenCustomValdation() {
        when(userProfileRepository.findOneByEmail(anyString()))
                .thenReturn(Optional.empty());

        when(userProfileRepository.findOneByUsername(anyString()))
                .thenReturn(Optional.empty());
    }

}
