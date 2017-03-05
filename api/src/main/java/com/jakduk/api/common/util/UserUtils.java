package com.jakduk.api.common.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.jakduk.api.common.vo.AuthUserProfile;
import com.jakduk.api.common.vo.SocialProfile;
import com.jakduk.api.configuration.authentication.user.JakdukUserDetails;
import com.jakduk.api.configuration.authentication.user.SocialUserDetails;
import com.jakduk.core.common.CommonRole;
import com.jakduk.core.common.CoreConst;
import com.jakduk.core.model.embedded.CommonWriter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author pyohwan
 *         16. 7. 31 오후 9:04
 */

@Component
public class UserUtils {

    @Value("${api.server.url}")
    private String apiServerUrl;

    @Value("${api.user.picture.large.url.path}")
    private String apiUserPictureLargeUrlPath;

    @Value("${api.user.picture.small.url.path}")
    private String apiUserPictureSmallUrlPath;

    private final String DAUM_PROFILE_API_URL = "https://apis.daum.net/user/v1/show.json";
    private final String FACEBOOK_PROFILE_API_URL = "https://graph.facebook.com/v2.8/me?fields=name,email,picture.type(large)&format=json";
    private final String FACEBOOK_PROFILE_THUMBNAIL_API_URL = "https://graph.facebook.com/v2.8/me?fields=picture.type(small)&format=json";

    /**
     * 손님인지 검사.
     */
    public static Boolean isAnonymousUser() {
        Boolean result = false;

        if (! SecurityContextHolder.getContext().getAuthentication().isAuthenticated())
            result = true;

        if (! result) {
            Collection<? extends GrantedAuthority> authorises = SecurityContextHolder.getContext().getAuthentication().getAuthorities();

            for (GrantedAuthority authority : authorises) {
                if ("ROLE_ANONYMOUS".equals(authority.getAuthority())) {
                    result = true;
                    break;
                }
            }
        }

        return result;
    }

    /**
     * 로그인 중인 회원이 관리자인지 검사.
     * @return 관리자 이면 true
     */
    public static Boolean isAdmin() {
        Boolean result = false;

        if (! SecurityContextHolder.getContext().getAuthentication().isAuthenticated())
            return false;

        Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();

        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals("ROLE_ROOT")) {
                result = true;
                break;
            }
        }

        return result;
    }

    /**
     * 로그인 중인 회원이 USER 권한인지 검사.
     * @return 회원이면 true
     */
    public static Boolean isUser() {
        Boolean result = false;

        if (! SecurityContextHolder.getContext().getAuthentication().isAuthenticated())
            return false;

        Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();

        for (GrantedAuthority grantedAuthority : authorities) {
            String authority = grantedAuthority.getAuthority();
            if (authority.equals("ROLE_USER_01") || authority.equals("ROLE_USER_02") || authority.equals("ROLE_USER_03")) {
                result = true;
                break;
            }
        }

        return result;
    }

    /**
     * 로그인 중인 회원이 이메일 기반인지 검사.
     * @return 이메일 기반이면 true, 아니면 false
     */
    public static Boolean isJakdukUser() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof JakdukUserDetails;
    }

    /**
     * 로그인 중인 회원이 소셜 기반인지 검사.
     *
     * @return 이메일 기반이면 true, 아니면 false
     */
    public static Boolean isSocialUser() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof SocialUserDetails;
    }

    /**
     * 로그인 중인 회원 정보를 가져온다.
     */
    public static AuthUserProfile getAuthUserProfile() {
        AuthUserProfile authUserProfile = null;

        if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof SocialUserDetails) {
                SocialUserDetails userDetail = (SocialUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

                Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();

                List<String> roles = authorities.stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList());

                authUserProfile = AuthUserProfile.builder()
                        .id(userDetail.getId())
                        .email(userDetail.getUserId())
                        .username(userDetail.getUsername())
                        .providerId(userDetail.getProviderId())
                        .picture(userDetail.getPicture())
                        .roles(roles)
                        .build();

            } else if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof JakdukUserDetails) {
                JakdukUserDetails userDetail = (JakdukUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

                Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();

                List<String> roles = authorities.stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList());

                authUserProfile = AuthUserProfile.builder()
                        .id(userDetail.getId())
                        .email(userDetail.getUsername())
                        .username(userDetail.getNickname())
                        .providerId(userDetail.getProviderId())
                        .picture(userDetail.getPicture())
                        .roles(roles)
                        .build();
            }
        }

        return authUserProfile;
    }

    /**
     * CommonWriter를 가져온다.
     */
    public static CommonWriter getCommonWriter() {
        AuthUserProfile authUserProfile = getAuthUserProfile();

        if (Objects.nonNull(authUserProfile)) {
            return CommonWriter.builder()
                    .userId(authUserProfile.getId())
                    .username(authUserProfile.getUsername())
                    .providerId(authUserProfile.getProviderId())
                    .picture(authUserProfile.getPicture())
                    .build();
        } else {
            return null;
        }
    }

    public static Collection<? extends GrantedAuthority> getAuthorities(List<Integer> roles) {
        return getGrantedAuthorities(getRoles(roles));
    }

    /**
     * Daum 프로필 가져오기
     *
     * @param accessToken accessToken
     */
    public SocialProfile getDaumProfile(String accessToken) {

        JsonNode jsonNode = fetchProfile(DAUM_PROFILE_API_URL, accessToken);

        JsonNode resultNode = jsonNode.get("result");

        SocialProfile profile = SocialProfile.builder()
                .id(resultNode.get("userid").asText())
                .nickname(resultNode.get("nickname").asText())
                .build();

        if (resultNode.has("imagePath")) {
            String imagePath = resultNode.get("imagePath").asText();
            profile.setSmallPictureUrl(imagePath);
        }

        if (resultNode.has("bigImagePath")) {
            String bigImagePath = resultNode.get("bigImagePath").asText();
            profile.setLargePictureUrl(bigImagePath);
        }

        return profile;
    }

    /**
     * Facebook 프로필 가져오기
     *
     * @param accessToken accessToken
     */
    public SocialProfile getFacebookProfile(String accessToken) {

        JsonNode jsonNode = fetchProfile(FACEBOOK_PROFILE_API_URL, accessToken);

        SocialProfile profile = SocialProfile.builder()
                .id(jsonNode.get("id").asText())
                .nickname(jsonNode.get("name").asText())
                .email(jsonNode.get("email").asText())
                .build();

        if (jsonNode.has("picture")) {
            String largePictureUrl = jsonNode.get("picture").get("data").get("url").asText();
            profile.setLargePictureUrl(largePictureUrl);

            JsonNode jsonNodeThumbnail = fetchProfile(FACEBOOK_PROFILE_THUMBNAIL_API_URL, accessToken);
            String smallPictureUrl = jsonNodeThumbnail.get("picture").get("data").get("url").asText();
            profile.setSmallPictureUrl(smallPictureUrl);
        }

        return profile;
    }

    /**
     * 회원 프로필 이미지 URL을 생성한다.
     *
     * @param sizeType size 타입
     * @param id UserImage의 ID
     */
    public String generateUserPictureUrl(CoreConst.IMAGE_SIZE_TYPE sizeType, String id) {

        if (StringUtils.isBlank(id))
            return null;

        String pictureUrl = null;

        switch (sizeType) {
            case LARGE:
                pictureUrl = String.format("%s/%s/%s", apiServerUrl, apiUserPictureLargeUrlPath, id);
                break;
            case SMALL:
                pictureUrl = String.format("%s/%s/%s", apiServerUrl, apiUserPictureSmallUrlPath, id);
                break;
        }

        return pictureUrl;
    }

    private static List<String> getRoles(List<Integer> roles) {
        List<String> newRoles = new ArrayList<>();

        if (roles != null) {
            for (Integer roleNumber : roles) {
                String roleName = CommonRole.getRoleName(roleNumber);
                if (!roleName.isEmpty()) {
                    newRoles.add(roleName);
                }
            }
        }

        return newRoles;
    }

    private static List<GrantedAuthority> getGrantedAuthorities(List<String> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }

        return authorities;
    }

    /**
     * accessToken에 해당하는 프로필 정보를 가져온다.
     *
     * @param url 요청할 URL
     * @param accessToken accessToken
     */
    private JsonNode fetchProfile(String url, String accessToken) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<JsonNode> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, JsonNode.class);

        return responseEntity.getBody();
    }

}

