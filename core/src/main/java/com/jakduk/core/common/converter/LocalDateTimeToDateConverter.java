package com.jakduk.core.common.converter;

import com.jakduk.core.common.util.DateUtils;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * Created by pyohwanjang on 2017. 3. 12..
 */
public class LocalDateTimeToDateConverter implements Converter<LocalDateTime, Date> {

    @Override
    public Date convert(LocalDateTime source) {
        return DateUtils.LocalDateTimeToDate(source);
    }
}
