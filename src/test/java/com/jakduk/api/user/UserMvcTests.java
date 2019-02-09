package com.jakduk.api.user;

import com.jakduk.api.TestMvcConfig;
import com.jakduk.api.mock.WithMockJakdukUser;
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
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.constraints.ResourceBundleConstraintDescriptionResolver;
import org.springframework.restdocs.constraints.ValidatorConstraintResolver;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.Cookie;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
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
        FootballClubOrigin footballClubOrigin = new FootballClubOrigin();
        footballClubOrigin.setId("54e1d2a58bf86df3fe819871");
        footballClubOrigin.setName("SEONGNAM");
        footballClubOrigin.setClubType(Constants.CLUB_TYPE.FOOTBALL_CLUB);
        footballClubOrigin.setAge(Constants.CLUB_AGE.SENIOR);
        footballClubOrigin.setSex(Constants.CLUB_SEX.MEN);

        footballClub = new FootballClub();
        footballClub.setId("54e1d2c68bf86df3fe819874");
        footballClub.setOrigin(footballClubOrigin);
        footballClub.setActive("active");
        footballClub.setNames(
                Arrays.asList(
                        new LocalName(Locale.KOREAN.getLanguage(), "성남FC", "성남"),
                        new LocalName(Locale.ENGLISH.getLanguage(), "SEONGNAM FC", "SEONGNAM"))
        );

        userPicture = new UserPicture();
        userPicture.setId("58b3bd86d6d83b06dac29a69");
        userPicture.setStatus(Constants.GALLERY_STATUS_TYPE.ENABLE);
        userPicture.setContentType("image/png");

        jakdukUser = new User();
        jakdukUser.setId("597df86caaf4fc0545d4f3e9");
        jakdukUser.setEmail("example@jakduk.com");
        jakdukUser.setUsername("JakdukUser");
        jakdukUser.setPassword("841db2bc28e4730906bd82d79e69c80633747570d96ffade7dd77f58270f31a222e129e005cb70d2");
        jakdukUser.setProviderId(Constants.ACCOUNT_TYPE.JAKDUK);
        jakdukUser.setAbout("안녕하세요.");
        jakdukUser.setRoles(Arrays.asList(JakdukAuthority.ROLE_USER_01.getCode()));
        jakdukUser.setSupportFC(footballClub);
        jakdukUser.setLastLogged(LocalDateTime.now());
    }

    @Test
    @WithMockUser
    public void createJakdukUserTest() throws Exception {

        this.whenCustomValdation();

        UserForm form = new UserForm();
        form.setEmail(jakdukUser.getEmail());
        form.setUsername(jakdukUser.getUsername());
        form.setPassword("1111");
        form.setPasswordConfirm("1111");
        form.setAbout(jakdukUser.getAbout());
        form.setFootballClub(footballClub.getId());
        form.setUserPictureId(userPicture.getId());

        when(userService.createJakdukUser(anyString(), anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(jakdukUser);

        ConstraintDescriptions userConstraints = new ConstraintDescriptions(UserForm.class, new ValidatorConstraintResolver(),
                new ResourceBundleConstraintDescriptionResolver(ResourceBundle.getBundle("ValidationMessages")));

        mvc.perform(
                post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf())
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

        AttemptSocialUser attemptSocialUser = new AttemptSocialUser();
        attemptSocialUser.setUsername("daumUser01");
        attemptSocialUser.setProviderId(Constants.ACCOUNT_TYPE.FACEBOOK);
        attemptSocialUser.setProviderUserId("abc123");
        attemptSocialUser.setExternalSmallPictureUrl("https://img1.daumcdn.net/thumb/R55x55/?fname=http%3A%2F%2Ftwg.tset.daumcdn.net%2Fprofile%2F6enovyMT1pI0&t=1507478752861");
        attemptSocialUser.setExternalLargePictureUrl("https://img1.daumcdn.net/thumb/R158x158/?fname=http%3A%2F%2Ftwg.tset.daumcdn.net%2Fprofile%2F6enovyMT1pI0&t=1507478752861");

        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute(Constants.PROVIDER_SIGNIN_ATTEMPT_SESSION_ATTRIBUTE, attemptSocialUser);

        SocialUserForm form = new SocialUserForm();
        form.setEmail("example@jakduk.com");
        form.setUsername("SocialUser");
        form.setAbout("안녕하세요.");
        form.setFootballClub(footballClub.getId());
        form.setUserPictureId(userPicture.getId());
        form.setExternalLargePictureUrl("https://img1.daumcdn.net/thumb/R158x158/?fname=http%3A%2F%2Ftwg.tset.daumcdn.net%2Fprofile%2FSjuNejHmr8o0&t=1488000722876");

        User expectUser = new User();
        expectUser.setId("597df86caaf4fc0545d4f3e9");
        expectUser.setEmail(form.getEmail());
        expectUser.setUsername(form.getUsername());
        expectUser.setPassword("841db2bc28e4730906bd82d79e69c80633747570d96ffade7dd77f58270f31a222e129e005cb70d2");
        expectUser.setProviderId(attemptSocialUser.getProviderId());
        expectUser.setProviderUserId(attemptSocialUser.getProviderUserId());
        expectUser.setAbout(form.getAbout());
        expectUser.setRoles(Arrays.asList(JakdukAuthority.ROLE_USER_02.getCode()));
        expectUser.setSupportFC(footballClub);
        expectUser.setLastLogged(LocalDateTime.now());

        when(userService.createSocialUser(anyString(), anyString(), any(Constants.ACCOUNT_TYPE.class), anyString(),
                anyString(), anyString(), anyString(), anyString()))
                .thenReturn(expectUser);

        ConstraintDescriptions userConstraints = new ConstraintDescriptions(SocialUserForm.class, new ValidatorConstraintResolver(),
                new ResourceBundleConstraintDescriptionResolver(ResourceBundle.getBundle("ValidationMessages")));

        mvc.perform(
                post("/api/user/social")
                        .session(mockHttpSession)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
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

        UserProfileResponse expectResponse = new UserProfileResponse();
        expectResponse.setEmail(jakdukUser.getEmail());
        expectResponse.setUsername(jakdukUser.getUsername());
        expectResponse.setAbout(jakdukUser.getAbout());
        expectResponse.setProviderId(jakdukUser.getProviderId());
        expectResponse.setFootballClubName(JakdukUtils.getLocalNameOfFootballClub(footballClub, language));
        expectResponse.setPicture(
                        new UserPictureInfo(
                                "597a0d53807d710f57420aa5",
                                "https://dev-api.jakduk.com/user/picture/small/597a0d53807d710f57420aa5",
                                "https://dev-api.jakduk.com/user/picture/597a0d53807d710f57420aa5"
                        ));
        expectResponse.setTemporaryEmail(false);

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

        Map<String, Object> form = new HashMap<String, Object>() {{
            put("email", jakdukUser.getEmail());
            put("username", jakdukUser.getUsername());
            put("about", jakdukUser.getAbout());
            put("footballClub", footballClub.getId());
            put("userPictureId", userPicture.getId());
        }};

        when(userService.editUserProfile(anyString(), anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(jakdukUser);

        ConstraintDescriptions userConstraints = new ConstraintDescriptions(UserProfileEditForm.class, new ValidatorConstraintResolver(),
                new ResourceBundleConstraintDescriptionResolver(ResourceBundle.getBundle("ValidationMessages")));

        mvc.perform(
                put("/api/user/profile/me")
                        .cookie(new Cookie("JSESSIONID", "3F0E029648484BEAEF6B5C3578164E99"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .content(ObjectMapperUtils.writeValueAsString(form)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(ObjectMapperUtils.writeValueAsString(EmptyJsonResponse.newInstance())))
                .andDo(
                        document("user-edit-profile-me",
                                requestHeaders(
                                        headerWithName("Cookie").optional().description("인증 쿠키. value는 JSESSIONID=키값")
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

        Map<String, Object> form = new HashMap<String, Object>() {{
            put("password", "1111");
            put("newPassword", "1112");
            put("newPasswordConfirm", "1112");
        }};

        doNothing().when(userService)
                .updateUserPassword(anyString(), anyString());

        ConstraintDescriptions userConstraints = new ConstraintDescriptions(UserPasswordForm.class, new ValidatorConstraintResolver(),
                new ResourceBundleConstraintDescriptionResolver(ResourceBundle.getBundle("ValidationMessages")));

        mvc.perform(
                put("/api/user/password")
                        .cookie(new Cookie("JSESSIONID", "3F0E029648484BEAEF6B5C3578164E99"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .content(ObjectMapperUtils.writeValueAsString(form)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(ObjectMapperUtils.writeValueAsString(EmptyJsonResponse.newInstance())))
                .andDo(
                        document("user-edit-password",
                                requestHeaders(
                                        headerWithName("Cookie").optional().description("인증 쿠키. value는 JSESSIONID=키값")
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

        UserPicture expectUserPicture = new UserPicture();
        expectUserPicture.setId(userPicture.getId());
        expectUserPicture.setStatus(Constants.GALLERY_STATUS_TYPE.TEMP);
        expectUserPicture.setContentType(userPicture.getContentType());

        when(userService.uploadUserPicture(anyString(), anyLong(), anyObject()))
                .thenReturn(expectUserPicture);

        byte[] ballBytes = new byte[]{
                (byte) 0x89, (byte) 0x50, (byte) 0x4e, (byte) 0x47, (byte) 0x0d, (byte) 0x0a, (byte) 0x1a, (byte) 0x0a, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x0d, (byte) 0x49, (byte) 0x48, (byte) 0x44, (byte) 0x52, (byte) 0x00, (byte) 0x00
        };

        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "sample.png", MediaType.IMAGE_PNG_VALUE, ballBytes);

        mvc.perform(
                fileUpload("/api/user/picture")
                        .file(mockMultipartFile)
                        .with(csrf()))
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
                        .cookie(new Cookie("JSESSIONID", "3F0E029648484BEAEF6B5C3578164E99"))
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(ObjectMapperUtils.writeValueAsString(EmptyJsonResponse.newInstance())))
                .andDo(
                        document("user-delete",
                                requestHeaders(
                                        headerWithName("Cookie").optional().description("인증 쿠키. value는 JSESSIONID=키값")
                                )
                        ));
    }

    @Test
    @WithMockUser
    public void findPasswordTest() throws Exception {

        Map<String, Object> form = new HashMap<String, Object>() {{
            put("email", jakdukUser.getEmail());
            put("callbackUrl", "http://dev-wev.jakduk/find/password");
        }};

        UserPasswordFindResponse expectResponse = new UserPasswordFindResponse(form.get("email").toString(),
                JakdukUtils.getMessageSource("user.msg.reset.password.send.email"));

        when(userService.sendEmailToResetPassword(anyString(), anyString()))
                .thenReturn(expectResponse);

        ConstraintDescriptions userConstraints = new ConstraintDescriptions(UserPasswordFindForm.class, new ValidatorConstraintResolver(),
                new ResourceBundleConstraintDescriptionResolver(ResourceBundle.getBundle("ValidationMessages")));

        mvc.perform(
                post("/api/user/password/find")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
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
                                responseFields(this.getPasswordFindDescriptor())
                        ));
    }

    @Test
    @WithMockUser
    public void resetPasswordTest() throws Exception {

        Map<String, Object> form = new HashMap<String, Object>() {{
            put("code", "16948f83-1af8-4736-9e12-cf57f03a981c");
            put("password", "1112");
        }};

        UserPasswordFindResponse expectResponse = new UserPasswordFindResponse(jakdukUser.getEmail(),
                JakdukUtils.getMessageSource("user.msg.success.change.password"));

        when(userService.resetPasswordWithToken(anyString(), anyString()))
                .thenReturn(expectResponse);

        ConstraintDescriptions userConstraints = new ConstraintDescriptions(UserPasswordResetForm.class, new ValidatorConstraintResolver(),
                new ResourceBundleConstraintDescriptionResolver(ResourceBundle.getBundle("ValidationMessages")));

        mvc.perform(
                post("/api/user/password/reset")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
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
                                responseFields(this.getPasswordFindDescriptor())
                        ));
    }

    @Test
    @WithMockUser
    public void getResetPasswordTokenTest() throws Exception {

        String code = "16948f83-1af8-4736-9e12-cf57f03a981c";

        UserPasswordFindResponse expectResponse = new UserPasswordFindResponse(jakdukUser.getEmail(), code);

        when(userService.validPasswordTokenCode(anyString()))
                .thenReturn(expectResponse);

        mvc.perform(
                get("/api/user/password/reset/{code}", code)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(ObjectMapperUtils.writeValueAsString(expectResponse)))
                .andDo(
                        document("user-valid-password-token-code",
                                pathParameters(
                                        parameterWithName("code").description("토큰 코드")
                                ),
                                responseFields(this.getPasswordFindDescriptor())
                        ));
    }

    private void whenCustomValdation() {
        when(userProfileRepository.findOneByEmail(anyString()))
                .thenReturn(Optional.empty());

        when(userProfileRepository.findOneByUsername(anyString()))
                .thenReturn(Optional.empty());
    }

    private FieldDescriptor[] getPasswordFindDescriptor() {
        return new FieldDescriptor[] {
                fieldWithPath("subject").type(JsonFieldType.STRING).description("html에서 쓰일 제목. 이메일 주소"),
                fieldWithPath("message").type(JsonFieldType.STRING).description("html에서 쓰일 내용.")
        };
    }

}
