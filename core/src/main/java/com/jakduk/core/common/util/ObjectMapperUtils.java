package com.jakduk.core.common.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.util.Map;

/**
 * @author Jang, Pyohwan
 * @since 2016. 12. 2.
 */
public class ObjectMapperUtils {

	private static ObjectMapper objectMapper = new ObjectMapper()
			.setSerializationInclusion(JsonInclude.Include.NON_NULL)
			.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

	public static ObjectMapper getObjectMapper() {
		return objectMapper;
	}

	public static String writeValueAsString(Object obj) throws JsonProcessingException {
		return objectMapper.writeValueAsString(obj);
	}

	public static <T> T convertValue(Map map, Class<T> obj) {
		return objectMapper.convertValue(map, obj);
	}
}
