package com.jakduk.api;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Jang, Pyohwan(1100273)
 * @since 2017. 2. 7.
 */

@Ignore
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(inheritProfiles = false, resolver = CoreTestActiveProfileResolver.class)
public class CoreApplicationTests {
}
