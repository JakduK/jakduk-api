package com.jakduk.api.common.converter;

import com.jakduk.api.common.util.DateUtils;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * Created by pyohwanjang on 2017. 3. 12..
 */
public class LocalDateTimeToDateConverter implements Converter<LocalDateTime, Date> {

	@Override
	public Date convert(LocalDateTime source) {
		return DateUtils.localDateTimeToDate(source);
	}
}
