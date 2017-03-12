package com.jakduk.core.common.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * @author Jang, Pyohwan
 * @since 2016. 12. 26.
 */
public class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {

	@Override public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers)
			throws IOException, JsonProcessingException {

		gen.writeNumber(value.toInstant(ZoneOffset.systemDefault().getRules().getOffset(value)).toEpochMilli());

	}
}
