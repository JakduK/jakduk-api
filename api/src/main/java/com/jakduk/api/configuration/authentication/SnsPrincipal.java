package com.jakduk.api.configuration.authentication;

import com.jakduk.core.common.CoreConst;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by pyohwanjang on 2017. 4. 30..
 */

@AllArgsConstructor
@Getter
public class SnsPrincipal {

    private CoreConst.ACCOUNT_TYPE providerId;
    private String accessToken;

}
