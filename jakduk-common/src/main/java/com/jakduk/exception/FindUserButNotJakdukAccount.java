package com.jakduk.exception;

import com.jakduk.common.CommonConst;
import lombok.Getter;
import org.springframework.security.authentication.BadCredentialsException;

/**
 * Created by pyohwan on 16. 5. 15.
 */

// Email에 해당하는 회원은 있지만 JakduK 계정으로 가입하지 않았음.
@Getter
public class FindUserButNotJakdukAccount extends BadCredentialsException {

    private CommonConst.ACCOUNT_TYPE providerId;

    public FindUserButNotJakdukAccount(String msg, CommonConst.ACCOUNT_TYPE providerId) {
        super(msg);
        this.providerId = providerId;
    }
}
