package com.jakduk.api.common.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

/**
 * Created by pyohwanjang on 2017. 3. 14..
 */
public class DateUtils {

    public static LocalDateTime dateToLocalDateTime(Date date) {
        return Objects.isNull(date) ? null : LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        return Objects.isNull(localDateTime) ? null : Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

}
