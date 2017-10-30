package com.jakduk.api.model.rabbitmq;

import com.jakduk.api.common.Constants;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Locale;
import java.util.Map;

/**
 * Created by pyohwanjang on 2017. 6. 17..
 */

@Builder
@Getter
@ToString
public class EmailPayload {

    private Locale locale;
    private Constants.EMAIL_TYPE type;
    private String recipientEmail;
    private String subject;
    private Map<String, String> extra;
    private Map<String, String> body;

}
