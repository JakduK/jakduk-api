package com.jakduk.util;

import com.jakduk.configuration.RootConfig;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created by pyohwan on 16. 6. 15.
 */

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RootConfig.class})
@WebAppConfiguration
public class AbstractSpringTest {

    static {
        if (System.getProperty("spring.profiles.active") == null) {
            System.setProperty("spring.profiles.active", "local");
        }
    }
}
