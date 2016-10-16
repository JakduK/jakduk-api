package com.jakduk.core.util;

import com.jakduk.core.configuration.CoreRootConfig;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created by pyohwan on 16. 9. 11.
 */

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CoreRootConfig.class})
@SpringBootTest
public class AbstractSpringTest {

    static {
        if (System.getProperty("spring.profiles.active") == null) {
            System.setProperty("spring.profiles.active", "local");
        }
    }
}
