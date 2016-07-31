package com.jakduk.common.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.jakduk.common.vo.DaumProfile;
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

    private JsonNode fetchProfile(String url, String accessToken) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<JsonNode> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, JsonNode.class);

        return responseEntity.getBody();
    }

    public DaumProfile getDaumProfile(String accessToken) {

        JsonNode jsonNode = fetchProfile(DAUM_PROFILE_API_URL, accessToken);

        JsonNode resultJson = jsonNode.get("result");
        DaumProfile profile = new DaumProfile();
        profile.setUserId(resultJson.get("userid").asText());
        profile.setId(resultJson.get("id").asInt());
        profile.setNickname(resultJson.get("nickname").asText());
        profile.setImagePath(resultJson.get("imagePath").asText());
        profile.setBigImagePath(resultJson.get("bigImagePath").asText());
        profile.setOpenProfile(resultJson.get("openProfile").asBoolean());

        return profile;
    }

}

