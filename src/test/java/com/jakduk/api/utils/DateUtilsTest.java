package com.jakduk.api.utils;

import org.junit.Assert;
import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateUtilsTest {

    @Test
    public void dateFormatTest() throws ParseException {
        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.SHORT, Locale.ENGLISH);
        SimpleDateFormat sf = (SimpleDateFormat) df;
        String p1 = sf.toPattern();
        String p2 = sf.toLocalizedPattern();

        Assert.assertTrue("MMM d, yyyy h:mm a".equals(p1));
        Assert.assertTrue("MMM d, yyyy h:mm a".equals(p2));

        DateFormat koreaDateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.KOREA);

        Assert.assertTrue("yy. M. d a h:mm".equals(((SimpleDateFormat)koreaDateFormat).toPattern()));

        LocalDateTime dateTime1 = LocalDateTime.parse("Thu, 5 Jun 2014 05:10:40 GMT", DateTimeFormatter.RFC_1123_DATE_TIME);

        Assert.assertTrue("2014-06-05T05:10:40".equals(dateTime1.toString()));

        LocalDate localDate = LocalDate.of(2017, 8, 7);
        DateTimeFormatter df02 = DateTimeFormatter.ISO_DATE;
        Assert.assertTrue("2017-08-07".equals(localDate.format(df02)));
    }

}
