package com.jakduk.api.restcontroller.user.vo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author pyohwan
 *         16. 7. 28 오후 10:23
 */

@ApiModel(value = "인증 토큰")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AuthenticationResponse implements Serializable {

    private String token;
}
