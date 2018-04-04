package com.jakduk.api.restcontroller.vo.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class UserPasswordFindResponse {
    private String subject;
    private String message;
}
