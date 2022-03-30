package com.jakduk.api.common;

import com.jakduk.api.configuration.security.JakdukAuthority;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class JakdukAuthorityTest {

    @Test
    public void roleTest01() {

        String roleName = JakdukAuthority.findByCode(10).name();

        assertTrue("ROLE_USER_01".equals(roleName));
    }

}
