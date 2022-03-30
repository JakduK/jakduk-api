package com.jakduk.api.restcontroller.vo.user;

import javax.validation.constraints.NotEmpty;

public class UserPasswordResetForm {

    @NotEmpty
    private String code;

    @NotEmpty
    private String password;

    public String getCode() {
        return code;
    }

    public String getPassword() {
        return password;
    }
}
