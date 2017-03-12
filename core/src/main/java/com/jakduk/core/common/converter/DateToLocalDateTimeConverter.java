package com.jakduk.core.common.converter;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.springframework.core.convert.converter.Converter;


import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

/**
 * Created by pyohwanjang on 2017. 3. 12..
 */
public class DateToLocalDateTimeConverter implements Converter<Date, LocalDateTime> {

    @Override
    public LocalDateTime convert(Date source) {
        return Objects.isNull(source) ? null : LocalDateTime.ofInstant(source.toInstant(), ZoneId.systemDefault());
    }

}
