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
public class UserPasswordFindForm {

    @NotEmpty
    private String email;

    @NotEmpty
    private String callbackUrl;

}
