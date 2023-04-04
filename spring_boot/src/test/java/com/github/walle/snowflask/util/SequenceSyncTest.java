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
 * Sequence UT
 *
 * @author
 */
public class SequenceSyncTest {
    private Sequence sequence;

    @BeforeEach
    public void before() throws IOException {
        String name = "test1";
        String[] dirPaths = new String[]{"./"};
        sequence = new Sequence(name, dirPaths);
    }

    /**
     * incr
     *
     * @throws Exception
     */
    @Test
    public void incrTest() throws Exception {
        long result = sequence.incr();

        Assertions.assertTrue(result > 0L);
    }

    /**
     * curr
     *
     * @throws Exception
     */
    @Test
    public void currTest() throws Exception {
        long result = sequence.curr();

        Assertions.assertTrue(result >= 0L);
    }

    /**
     * 测试性能
     *
     * @throws Exception
     */
    @Test
    public void incrTwoTest() throws Exception {
        Set<Long> set = new HashSet<>((int)(4000 / 0.75));
        myIncr(set);
        myIncr(set);
        Assertions.assertTrue(set.size() == 4000);

        String name = "test2";
        String[] dirPaths = new String[]{"./"};
        Sequence sequence2 = new Sequence(name, dirPaths);

        long t = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            sequence2.incr();
        }
        long runTime = System.currentTimeMillis() - t;
        Assertions.assertTrue(40L > runTime);
    }

    private void myIncr(Set<Long> set) throws Exception {
        String name = "test3";
        String[] dirPaths = new String[]{"./"};
        Sequence sequence2 = new Sequence(name, dirPaths);
        for (short i = 1; i <= 5; i++) {
            for (int j = 1; j <= 400; j++) {
                set.add(sequence2.incr(i));
            }
        }
    }

    /**
     * benchTwo
     *
     * @throws Exception
     */
    @Test
    public void benchTwoTest() throws Exception {
        int max = 40;
        Sequence[] setArray = new Sequence[max];

        for (Integer key = 0; key < max; key++) {
            setArray[key] = new Sequence("test1" + key, new String[]{"./"});
        }

        long time = System.currentTimeMillis();
        int k = 200000;
        for (int j = 0; j < k; j++) {
            for (int key = 0; key < max; key++) {
                if (setArray[key].incr() == -1L) {
                    System.out.println("err-j:" + j);
                }
            }
        }
        long time2 = System.currentTimeMillis();
        long runTime = (k / (time2 - time) * 1000 * max);
        System.out.println("Sequence tps = " + runTime);
        Assert.assertTrue(runTime != 1L);
    }
}
