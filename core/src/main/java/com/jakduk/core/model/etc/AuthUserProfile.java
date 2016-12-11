package com.jakduk.core.model.etc;

import com.jakduk.core.common.CoreConst;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author pyohwan
 *         16. 7. 14 오전 12:21
 */

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AuthUserProfile {

    private String id;

    private String email;

    private String username;

    private CoreConst.ACCOUNT_TYPE providerId;

    private List<String> roles;
}
