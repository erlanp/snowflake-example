/*
 * Copyright
 */

package com.github.walle.snowflask.service;

import com.github.walle.snowflask.util.BaseSequence;
import com.github.walle.snowflask.util.RandomSet;
import com.github.walle.snowflask.util.Sequence;
import com.github.walle.snowflask.util.Snow;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;

/**
 * IdService UT
 *
 * @author
 * @date 2023-03-11
 */
public class IdServiceTest {
    @InjectMocks
    private IdService idService;

    @Mock
    private Sequence sequence;

    @Mock
    private Snow snow;

    @BeforeEach
    public void before() throws Exception {
        MockitoAnnotations.openMocks(this);
        if (ReflectionTestUtils.getField(idService, "dirPath") == null) {
            ReflectionTestUtils.setField(idService, "dirPath", "./");
        }
        idService.afterPropertiesSet();
    }

    /**
     * buildSequence
     *
     * @throws Exception
     */
    @Test
    public void buildSequenceTest() throws Exception {
        String name = "test1";
        BaseSequence result = idService.buildSequence(name);

        Assert.assertTrue(result != null && idService.buildSequence(name) != null);
    }

    /**
     * buildSnow
     *
     * @throws Exception
     */
    @Test
    public void buildSnowTest() throws Exception {
        String name = "test1";
        Snow result = idService.buildSnow(name);

        Assert.assertTrue(result != null && idService.buildSnow(name) != null);
    }

    /**
     * afterPropertiesSet
     *
     * @throws Exception
     */
    @Test
    public void afterPropertiesSetTest() throws Exception {
        String error = null;
        idService.afterPropertiesSet();

        try {
        } catch (Exception exp) {
            error = Arrays.toString(exp.getStackTrace());
        }
        Assert.assertTrue(error == null);
    }

    /**
     * getSequence
     *
     * @throws Exception
     */
    @Test
    public void getSequenceTest() throws Exception {
        Sequence result = idService.getSequence();

        Assert.assertTrue(result != null);
    }

    /**
     * getRandomSet
     *
     * @throws Exception
     */
    @Test
    public void getRandomSetTest() throws Exception {
        RandomSet result = null;
        for (int i = 0; i < 2; i++) {
            result = idService.getRandomSet();
        }

        Assert.assertTrue(result != null);
    }

    /**
     * getSnow
     *
     * @throws Exception
     */
    @Test
    public void getSnowTest() throws Exception {
        Snow result = idService.getSnow();

        Assert.assertTrue(result != null);
    }
}
