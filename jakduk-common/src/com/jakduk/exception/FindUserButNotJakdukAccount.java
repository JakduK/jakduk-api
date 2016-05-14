package com.jakduk.exception;

import com.jakduk.common.CommonConst;
import lombok.Getter;
import org.springframework.security.authentication.BadCredentialsException;

/**
 * Created by pyohwan on 16. 5. 15.
 */

@Getter
public class FindUserButNotJakdukAccount extends BadCredentialsException {

    private CommonConst.ACCOUNT_TYPE providerId;

    public FindUserButNotJakdukAccount(String msg, CommonConst.ACCOUNT_TYPE providerId) {
        super(msg);
        this.providerId = providerId;
    }
}
