package com.jakduk.api.restcontroller.vo.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class UserPasswordResetForm {

    @NotEmpty
    private String code;

    @NotEmpty
    private String password;

}
