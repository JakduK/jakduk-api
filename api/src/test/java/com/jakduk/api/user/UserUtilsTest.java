package com.jakduk.api.user;

import com.jakduk.api.ApiApplicationTests;
import com.jakduk.api.common.util.UserUtils;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * Created by pyohwanjang on 2017. 2. 24..
 */
public class UserUtilsTest extends ApiApplicationTests {

    @Resource
    private UserUtils userUtils;

    @Test
    public void getDaumProfile() {
        userUtils.getDaumProfile("4d345b8b1efd822db8d10eefbcbd7444ecb2773185a7c71baf4e2122196bc815");
    }

    @Test
    public void getFacebookProfile() {
        userUtils.getFacebookProfile("EAALwXK7RDAIBAOoadFujSGzUzgetp8kopPF2QfQP3gzKkzacmZBZBVuOadYQAwlMA4cdZByX1mTRLnvpgzEYiR1QxIZCsN6mRbHNd7ZA7YhYWGk51js6S4ddkQAUIzNKqYKZCbjRaRMwsyjiqjQuSsZANluuwMJTpfxASZAhMokRNU6OJcUS6mGJ");
    }
}
