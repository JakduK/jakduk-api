package com.jakduk.api;

import com.jakduk.core.CoreApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Jang, Pyohwan(1100273)
 * @since 2017. 2. 7.
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ApiApplication.class, CoreApplication.class})
public class ApiApplicationTests {

	@Test
	public void contextLoads() {
	}
}
