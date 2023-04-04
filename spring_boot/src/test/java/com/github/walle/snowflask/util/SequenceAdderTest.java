/*
 * Copyright
 */

package com.github.walle.snowflask.util;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * SequenceAdder UT
 *
 * @author
 */
public class SequenceAdderTest {
    private SequenceAdder sequenceAdder;

    @BeforeEach
    public void before() throws IOException {
        String name = "test6";
        String[] dirPaths = new String[]{"./"};
        sequenceAdder = new SequenceAdder(name, dirPaths);
    }

    /**
     * incr
     *
     * @throws Exception
     */
    @Test
    public void incrTest() throws Exception {
        long result = sequenceAdder.incr();

        Assertions.assertTrue(result > 0L);
    }

    /**
     * curr
     *
     * @throws Exception
     */
    @Test
    public void currTest() throws Exception {
        long result = sequenceAdder.curr();

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
        SequenceAdder sequence2 = new SequenceAdder(name, dirPaths);

        long t = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            sequence2.incr();
        }
        long runTime = System.currentTimeMillis() - t;
        Assertions.assertTrue(30L > runTime);
    }

    private void myIncr(Set<Long> set) throws Exception {
        String name = "test3";
        String[] dirPaths = new String[]{"./"};
        SequenceAdder sequence2 = new SequenceAdder(name, dirPaths);
        for (short i = 1; i <= 5; i++) {
            for (int j = 1; j <= 400; j++) {
                set.add(sequence2.incr(i));
            }
        }
    }
}
