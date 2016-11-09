package com.jakduk.core.util;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by pyohwan on 16. 9. 11.
 */

@Ignore
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles({"core-staging", "core-prod", "core-default"}) // profiles 설정 안하면, core-local 적용
public class AbstractSpringTest {

}
