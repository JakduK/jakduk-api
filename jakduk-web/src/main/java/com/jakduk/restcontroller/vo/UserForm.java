package com.jakduk.restcontroller.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by pyohwan on 16. 6. 26.
 */

@Data
public class UserForm {

    @NotNull
    @Size(min = 6, max=30)
    private String email;

    @NotNull
    @Size(min = 2, max=20)
    private String username;

    @NotNull
    @Size(min = 4, max=20)
    private String password;

    @NotNull
    @Size(min = 4, max=20)
    private String passwordConfirm;

    private String footballClub;

    private String about;
}
