package com.jakduk.api.common.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.jakduk.api.common.vo.SocialProfile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @author pyohwan
 *         16. 7. 31 오후 9:04
 */

@Component
public class UserUtils {

    private final String DAUM_PROFILE_API_URL = "https://apis.daum.net/user/v1/show.json";
    private final String FACEBOOK_PROFILE_API_URL = "https://graph.facebook.com/v2.7/me?fields=id,name,email&format=json";

    private JsonNode fetchProfile(String url, String accessToken) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<JsonNode> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, JsonNode.class);

        return responseEntity.getBody();
    }

    public SocialProfile getDaumProfile(String accessToken) {

        JsonNode jsonNode = fetchProfile(DAUM_PROFILE_API_URL, accessToken);

        JsonNode resultJson = jsonNode.get("result");
        SocialProfile profile = new SocialProfile();
        profile.setId(resultJson.get("userid").asText());
        profile.setNickname(resultJson.get("nickname").asText());

        return profile;
    }

    public SocialProfile getFacebookProfile(String accessToken) {

        JsonNode jsonNode = fetchProfile(FACEBOOK_PROFILE_API_URL, accessToken);

        SocialProfile profile = new SocialProfile();
        profile.setId(jsonNode.get("id").asText());
        profile.setNickname(jsonNode.get("name").asText());
        profile.setEmail(jsonNode.get("email").asText());

        return profile;
    }

}

