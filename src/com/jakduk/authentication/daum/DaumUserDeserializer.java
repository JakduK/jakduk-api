package com.jakduk.authentication.daum;

import java.io.IOException;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 10. 14.
 * @desc     :
 */
public class DaumUserDeserializer extends JsonDeserializer<DaumUser>{

	@Override
	public DaumUser deserialize(JsonParser jp, DeserializationContext ctzt) throws IOException, JsonProcessingException {
		DaumUser user = new DaumUser();
		ObjectCodec oc = jp.getCodec();
		JsonNode node = oc.readTree(jp);
		JsonNode node2 = node.get("result");
		user.setUserid(getNodeString(node2.get("userid")));
		user.setId(node2.get("id").getDecimalValue().toString());
		user.setNickname(getNodeString(node2.get("nickname")));
		user.setImagePath(getNodeString(node2.get("imagePath")));
		user.setBigImagePath(getNodeString(node2.get("bigImagePath")));
		user.setOpenProfile(node2.get("openProfile").getBooleanValue());
		
		return user;
	}
	
	private String getNodeString(JsonNode jsonNode) {
		return jsonNode == null ? "" : jsonNode.getTextValue();
	}
}
