package com.jakduk.api.model.rabbitmq;

import com.jakduk.api.common.JakdukConst;
import lombok.Builder;
import lombok.Getter;

import java.util.Locale;
import java.util.Map;

/**
 * Created by pyohwanjang on 2017. 6. 17..
 */

@Builder
@Getter
public class EmailPayload {

    private Locale locale;
    private JakdukConst.EMAIL_TYPE type;
    private String recipientEmail;
    private String subject;
    private Map<String, String> extra;
    private Map<String, Object> body;

}
