/*
 * Copyright
 */

package com.github.walle.snowflask.util;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Snow UT
 *
 * @author
 * @date 2023-03-11
 */
public class SnowTest {
    private Snow snow;

    @BeforeEach
    public void before() throws IOException {
        String name = "test1";
        String[] dirPaths = new String[]{"./"};
        Sequence sequence = new Sequence(name, dirPaths);
        snow = new Snow(1, sequence);
    }

    /**
     * gen
     *
     * @throws Exception
     */
    @Test
    public void genTest() throws Exception {
        long result = snow.gen();
        Assertions.assertTrue(result > 1L);

        int times = 10000;

        Set<Long> set = new HashSet<>((int) (times / 0.75));
        for (int i = 0; i < times; i++) {
            set.add(snow.gen());
        }

        long t = System.currentTimeMillis();
        for (int i = 0; i < times; i++) {
            snow.gen();
        }
        long runTime = System.currentTimeMillis() - t;
        Assertions.assertTrue(10L > runTime && set.size() == times);
    }

    /**
     * bench
     *
     * @throws Exception
     */
    @Test
    public void benchTest() throws Exception {
        long time = System.currentTimeMillis();
        int k = 1000000;
        for (int j = 0; j < k; j++) {
            snow.gen();
        }
        long time2 = System.currentTimeMillis();
        long runTime = (k / (time2 - time) * 1000);
        System.out.println("Snow tps = " + runTime);
        Assert.assertTrue(runTime != 1L);
    }

    /**
     * benchTwo
     *
     * @throws Exception
     */
    @Test
    public void benchTwoTest() throws Exception {
        int max = 40;
        Snow[] setArray = new Snow[max];

        for (Integer key = 0; key < max; key++) {
            setArray[key] = new Snow(1, new Sequence("test1" + key, new String[]{"./"}));
        }

        long time = System.currentTimeMillis();
        int k = 1000000;
        for (int j = 0; j < k; j++) {
            for (int key = 0; key < max; key++) {
                if (setArray[key].gen() == -1L) {
                    System.out.println("err-j:" + j);
                }
            }
        }
        long time2 = System.currentTimeMillis();
        long runTime = (k / (time2 - time) * 1000 * max);
        System.out.println("Snow tps = " + runTime);
        Assert.assertTrue(runTime != 1L);
    }
}
