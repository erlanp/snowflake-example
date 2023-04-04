/*
 * Copyright
 */

package com.github.walle.snowflask.util;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * RandomSet UT
 *
 * @author
 * @date 2023-03-31
 */
public class RandomSetTest {
    private RandomSet randomSet;

    @BeforeEach
    public void before() {
        randomSet = new RandomSet(5);
    }

    /**
     * gen
     *
     * @throws Exception
     */
    @Test
    public void genTest() throws Exception {
        int times = 10;
        long result = randomSet.gen(times);

        Assert.assertTrue(result != 1L);
    }

    /**
     * gen
     *
     * @throws Exception
     */
    @Test
    public void genTwoTest() throws Exception {
        long result = randomSet.gen();

        Assert.assertTrue(result != 1L);
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
            randomSet.gen(10);
        }
        long time2 = System.currentTimeMillis();
        long runTime = (k / (time2 - time) * 1000);
        System.out.println("RandomSet tps = " + runTime);
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
        RandomSet[] setArray = new RandomSet[max];

        for (Integer key = 0; key<max; key++) {
            setArray[key] = new RandomSet(1);
        }

        long time = System.currentTimeMillis();
        int k = 1000000;
        for (int j=0; j<k; j++) {
            for (int key = 0; key < max; key++) {
                if (setArray[key].gen() == -1l) {
                    System.out.println("err-j:" + j);
                }
            }
        }
        long time2 = System.currentTimeMillis();
        long runTime = (k / (time2 - time) * 1000 * max);
        System.out.println("RandomSet tps = " + runTime);
        Assert.assertTrue(runTime != 1L);
    }
}


