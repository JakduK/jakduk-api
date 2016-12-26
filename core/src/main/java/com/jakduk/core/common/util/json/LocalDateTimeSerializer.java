package com.jakduk.core.common.util.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * @author Jang, Pyohwan(1100273)
 * @since 2016. 12. 26.
 */
public class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {

	/**
	 * Method that can be called to ask implementation to serialize
	 * values of type this serializer handles.
	 *
	 * @param value       Value to serialize; can <b>not</b> be null.
	 * @param gen         Generator used to output resulting Json content
	 * @param serializers Provider that can be used to get serializers for
	 */
	@Override public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers)
			throws IOException, JsonProcessingException {

		gen.writeNumber(value.toInstant(ZoneOffset.systemDefault().getRules().getOffset(value)).toEpochMilli());

	}
}
