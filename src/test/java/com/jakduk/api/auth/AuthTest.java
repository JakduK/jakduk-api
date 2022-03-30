package com.jakduk.api.auth;

import com.fasterxml.jackson.databind.JsonNode;
import com.jakduk.api.common.Constants;
import com.jakduk.api.common.util.AuthUtils;
import com.jakduk.api.restcontroller.vo.user.SocialProfile;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * @author pyohwan
 *         16. 7. 31 오후 8:29
 */

public class AuthTest {

	@Autowired
	private AuthUtils authUtils;

	@Disabled
	@Test
	public void Facebook_프로필가져오기() {

		String daumApiUrl = "https://graph.facebook.com/v2.8/me?fields=name,email,picture.type(large)&format=json";

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization",
			"Bearer EAAKMWimF8eoBAFNJa8iPk8YADlbItfZA24IoWZC54E1tB4YbbtoSyHyqqyGpotLInyhQkACcry4Tf7AxthIfryZBiYFTJ8ZBw3fG7CKElUFn33ybNZAaK6JmeZBbDz2xXHkhEEpGdG6jhGQCaGVmo67plrDqT5gNl7RyAVdCXS4IZCrqHi4V2b8");
		//headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<>(headers);

		System.out.println("entity=" + entity);

		RestTemplate restTemplate = new RestTemplate();

		//Map<String, String> result = (Map<String, String>) restTemplate.getForObject("https://apis.daum.net/user/v1/show.json?access_token=261fc08c62a4d9625cb2c61dfa382c584cc37432cdb3bd309fd9b6b2db46e095", Map.class);
		ResponseEntity<JsonNode> responseEntity = restTemplate.exchange(daumApiUrl, HttpMethod.GET, entity,
			JsonNode.class);

		System.out.println("responseEntity=" + responseEntity);
		responseEntity.getBody().get("result");

		System.out.println("result=" + responseEntity.getBody().get("result"));

		//System.out.println(userUtils.getDaumProfile("261fc08c62a4d9625cb2c61dfa382c584cc37432cdb3bd309fd9b6b2db46e095"));

	}

	@Disabled
	@Test
	public void getKakaoProfileInfo() {
		SocialProfile socialProfile = authUtils.getSnsProfile(Constants.ACCOUNT_TYPE.KAKAO,
			"AekzZJgcpLOQaxwpa-TySpBqFsszWctZ09dNSgo8BVUAAAFowxN59A");
		assertFalse(socialProfile.getId().isEmpty());
	}

	@Disabled
	@Test
	public void getNaverProfileInfo() {
		SocialProfile socialProfile = authUtils.getSnsProfile(Constants.ACCOUNT_TYPE.NAVER,
			"AAAANlOzqsHQ12T3iHXCXR4EEHBe1AmBgMwEoWlumcmsrkgZBDkRJryVBGVlrzPnIkDTwFlFzgF2t2IVEWT5g4pC9TU=");
		assertFalse(socialProfile.getId().isEmpty());
	}

	@Disabled
	@Test
	public void getGoogleProfileInfo() {
		SocialProfile socialProfile = authUtils.getSnsProfile(Constants.ACCOUNT_TYPE.GOOGLE,
			"ya29.GlupBqsyMWcUC6h5j0UJ68hUz7Lqz9gCVcsm3lw5JOoHW6FAmx4h62Hm01PyDCnVHKhhVW0SyERZUIZV8u5-YZHsAUWpRKslgMvQN5Fc-ynJQFQII8l6nhZNK3-o");
		assertFalse(socialProfile.getId().isEmpty());
	}
}
