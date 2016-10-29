package com.jakduk.api.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jakduk.api.configuration.ApiRootConfig;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * Created by pyohwan on 16. 6. 30.
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public abstract class AbstractMvcTest<T> {

    @SuppressWarnings("unused")
    @Autowired
    private WebApplicationContext webApplicationContext;
    protected MockMvc mockMvc;

    protected ObjectMapper objectMapper = new ObjectMapper();

    protected void initMvc(T t) {
        mockMvc = MockMvcBuilders.standaloneSetup(t).build();
    }
}
