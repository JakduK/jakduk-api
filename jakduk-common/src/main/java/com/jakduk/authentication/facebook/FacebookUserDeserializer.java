package com.jakduk.authentication.facebook;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

	

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 7. 27.
 * @desc     :
 */
public class FacebookUserDeserializer extends JsonDeserializer<FacebookUser>{

	@Override
	public FacebookUser deserialize(JsonParser jp, DeserializationContext ctzt) throws IOException, JsonProcessingException {
		FacebookUser user = new FacebookUser();
		ObjectCodec oc = jp.getCodec();
		JsonNode node = oc.readTree(jp);
		user.setId(getNodeString(node.get("id")));
		user.setName(getNodeString(node.get("name")));
		user.setUsername(getNodeString(node.get("username")));
		user.setBirthday(getNodeString(node.get("birthday")));
		user.setEmail(getNodeString(node.get("email")));
		user.setGender(getNodeString(node.get("gender")));
		user.setLink(getNodeString(node.get("link")));
		user.setLocale(getNodeString(node.get("locale")));
		user.setBio(getNodeString(node.get("bio")));
		return user;
	}

	private String getNodeString(JsonNode jsonNode) {
		return jsonNode == null ? "" : jsonNode.asText();
	}

}
