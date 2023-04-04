/*
 * Copyright
 */

package com.github.walle.snowflask.configuration;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.concurrent.Executor;

/**
 * ExecutorConfiguration UT
 *
 * @authorff
 */
public class ExecutorConfigurationTest {
    @InjectMocks
    private ExecutorConfiguration executorConfiguration;

    @BeforeEach
    public void before() {
        MockitoAnnotations.openMocks(this);
        if (ReflectionTestUtils.getField(executorConfiguration, "namePrefix") == null) {
            ReflectionTestUtils.setField(executorConfiguration, "namePrefix", "1");
        }

        ReflectionTestUtils.setField(executorConfiguration, "corePoolSize", 4);
        ReflectionTestUtils.setField(executorConfiguration, "maxPoolSize", 16);
        ReflectionTestUtils.setField(executorConfiguration, "queueCapacity", 16);
        ReflectionTestUtils.setField(executorConfiguration, "keepAliveSeconds", 60);
    }

    /**
     * taskExector
     *
     * @throws Exception
     */
    @Test
    public void taskExectorTest() throws Exception {
        Executor result = executorConfiguration.taskExector();

        Assert.assertTrue(result != null);
    }
}
