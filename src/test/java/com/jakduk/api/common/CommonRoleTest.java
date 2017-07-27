package com.jakduk.api.common;

import org.junit.Assert;
import org.junit.Test;

public class CommonRoleTest {

    @Test
    public void roleTest01() {

        String roleName = CommonRole.findByCode(10).name();

        Assert.assertTrue("ROLE_USER_01".equals(roleName));
    }

}
