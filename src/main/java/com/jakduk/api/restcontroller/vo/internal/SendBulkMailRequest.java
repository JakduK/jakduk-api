package com.jakduk.api.restcontroller.vo.internal;

import com.jakduk.api.common.Constants;
import lombok.Getter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.Locale;
import java.util.Map;

@Getter
public class SendBulkMailRequest {
    @NotNull
    private Locale locale;
    @NotNull
    private Constants.EMAIL_TYPE type;
    @NotEmpty
    private String templateName;
    @NotEmpty
    private String recipientEmail;
    @NotEmpty
    private String subject;
    private Map<String, Object> body;
}
