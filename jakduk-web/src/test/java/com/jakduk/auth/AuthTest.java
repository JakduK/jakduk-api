package com.jakduk.auth;

import com.fasterxml.jackson.databind.JsonNode;
import com.jakduk.common.util.UserUtils;
import com.jakduk.util.AbstractSpringTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * @author pyohwan
 *         16. 7. 31 오후 8:29
 */
public class AuthTest extends AbstractSpringTest {

    @Autowired
    private UserUtils userUtils;

    @Test
    public void DAUM_프로필가져오기() {

        String daumApiUrl = "https://apis.daum.net/user/v1/show.json";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer 261fc08c62a4d9625cb2c61dfa382c584cc37432cdb3bd309fd9b6b2db46e095");
        //headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        System.out.println("entity=" + entity);

        RestTemplate restTemplate = new RestTemplate();

        //Map<String, String> result = (Map<String, String>) restTemplate.getForObject("https://apis.daum.net/user/v1/show.json?access_token=261fc08c62a4d9625cb2c61dfa382c584cc37432cdb3bd309fd9b6b2db46e095", Map.class);
        ResponseEntity<JsonNode> responseEntity = restTemplate.exchange(daumApiUrl, HttpMethod.GET, entity, JsonNode.class);

        responseEntity.getBody().get("result");

        System.out.println("result=" + responseEntity.getBody().get("result"));

        System.out.println(userUtils.getDaumProfile("261fc08c62a4d9625cb2c61dfa382c584cc37432cdb3bd309fd9b6b2db46e095"));

    }

    @Test
    public void Facebook_프로필가져오기() {

        String daumApiUrl = "https://graph.facebook.com/v2.7/me?fields=id,name,email&format=json";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer EAALwXK7RDAIBABRLhRHZB8DV9GXKSLfSlvZBGkjXbVziSQRuPTqpc2eAvZBXcd9XM130euKDAF9wiZCCBroeZCT3PUpedn9U8WzZAY5q4rKCyQSRUkcGtON0aS95r46s1a2i4OTYMXDE5F8yEZBiw3X20VWXZBz33VjjMMG3hbNbtguVYjlZBJWXq");
        //headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        System.out.println("entity=" + entity);

        RestTemplate restTemplate = new RestTemplate();

        //Map<String, String> result = (Map<String, String>) restTemplate.getForObject("https://apis.daum.net/user/v1/show.json?access_token=261fc08c62a4d9625cb2c61dfa382c584cc37432cdb3bd309fd9b6b2db46e095", Map.class);
        ResponseEntity<JsonNode> responseEntity = restTemplate.exchange(daumApiUrl, HttpMethod.GET, entity, JsonNode.class);

        System.out.println("responseEntity=" + responseEntity);
        responseEntity.getBody().get("result");

        System.out.println("result=" + responseEntity.getBody().get("result"));

        //System.out.println(userUtils.getDaumProfile("261fc08c62a4d9625cb2c61dfa382c584cc37432cdb3bd309fd9b6b2db46e095"));

    }
}
