package com.jakduk.authentication.daum;

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
 * @date     : 2014. 10. 14.
 * @desc     :
 */
public class DaumUserDeserializer extends JsonDeserializer<DaumUser>{
	
	@Override
	public DaumUser deserialize(JsonParser jp, DeserializationContext ctzt) throws IOException, JsonProcessingException {		
		ObjectCodec oc = jp.getCodec();
		JsonNode node = oc.readTree(jp);
		JsonNode node2 = node.get("result");
		DaumUser user = new DaumUser();
		user.setUserid(getNodeString(node2.get("userid")));
		user.setId(node2.get("id").asText());
		user.setNickname(getNodeString(node2.get("nickname")));
		user.setImagePath(getNodeString(node2.get("imagePath")));
		user.setBigImagePath(getNodeString(node2.get("bigImagePath")));
		user.setOpenProfile(node2.get("openProfile").asBoolean());

		return user;
	}

	private String getNodeString(JsonNode jsonNode) {
		return jsonNode == null ? "" : jsonNode.asText();
	}
}
