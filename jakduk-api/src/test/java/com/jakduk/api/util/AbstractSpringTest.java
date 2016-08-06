package com.jakduk.api.util;

import com.jakduk.api.configuration.ApiRootConfig;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * @author pyohwan
 * 16. 6. 15 오후 9:31
 */

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ApiRootConfig.class})
@WebAppConfiguration
public class AbstractSpringTest {

    static {
        if (System.getProperty("spring.profiles.active") == null) {
            System.setProperty("spring.profiles.active", "local");
        }
    }
}
