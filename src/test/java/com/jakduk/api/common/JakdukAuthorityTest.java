package com.jakduk.api.common;

import com.jakduk.api.configuration.security.JakdukAuthority;
import org.junit.Assert;
import org.junit.Test;

public class JakdukAuthorityTest {

    @Test
    public void roleTest01() {

        String roleName = JakdukAuthority.findByCode(10).name();

        Assert.assertTrue("ROLE_USER_01".equals(roleName));
    }

}
