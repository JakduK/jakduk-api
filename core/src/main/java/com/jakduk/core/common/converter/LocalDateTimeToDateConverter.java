package com.jakduk.core.common.converter;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Objects;

/**
 * Created by pyohwanjang on 2017. 3. 12..
 */
public class LocalDateTimeToDateConverter implements Converter<LocalDateTime, Date> {

    @Override
    public Date convert(LocalDateTime source) {
        return Objects.isNull(source) ? null : Date.from(source.atZone(ZoneId.systemDefault()).toInstant());
    }
}
