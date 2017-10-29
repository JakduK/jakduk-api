package com.jakduk.api.restcontroller.vo.user;

import lombok.Getter;
import org.hibernate.validator.constraints.NotEmpty;

@Getter
public class UserPasswordFindForm {

    @NotEmpty
    private String email;

    @NotEmpty
    private String callbackUrl;

}
