/*
 * Copyright
 */

package com.github.walle.snowflask.configuration;

import com.github.walle.snowflask.util.BaseSequence;
import com.github.walle.snowflask.util.Sequence;
import com.github.walle.snowflask.util.SequenceAdder;
import com.github.walle.snowflask.util.SequenceAtomic;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.function.Function;

/**
 * ExecutorConfiguration UT
 *
 * @author
 */
@SpringBootTest
@ExtendWith(SpringExtension.class)
public class ExecutorConfigurationITTests {

    @Resource(name = "taskExecutor")
    ThreadPoolTaskExecutor applicationTaskExecutor;

    /**
     * id产生是否重复
     *
     * @throws Exception
     */
    @Test
    public void taskSetTest() throws Exception {
        ConcurrentHashMap<Long, BaseSequence> map = new ConcurrentHashMap((int) (10 * .75));
        ConcurrentHashMap<Long, ConcurrentHashMap<Long, Integer>> mapSet = new ConcurrentHashMap((int) (10 / .75));
        for (long i = 0L; i < 10L; i += 1L) {
            map.put(i, new SequenceAtomic(String.valueOf(i), new String[]{"./"}));
            mapSet.put(i, new ConcurrentHashMap((int) (20000 / .75)));
        }

        List<Future<Long>> list = new ArrayList<>(20);
        long t = System.currentTimeMillis();
        for (long i = 0L; i < 10L; i += 1L) {
            for (long j = 0L; j < 2L; j += 1L) {
                long finalI = i;
                list.add(applicationTaskExecutor.submit(() -> {
                    BaseSequence seq = map.get(finalI);
                    ConcurrentHashMap<Long, Integer> localSet = mapSet.get(finalI);
                    for (int k = 0; k < 100000; k++) {
                        localSet.put(seq.incr(), k);
                    }
                    return 1L;
                }));
            }
        }
        for (Future<Long> future : list) {
            future.get();
        }
        long t2 = System.currentTimeMillis() - t;
        boolean same = true;
        for (Map value : mapSet.values()) {
            if (value.size() != 200000) {
                same = false;
            }
        }
        long min = 4294967295L;
        long max = 0L;
        ConcurrentHashMap<Long, Integer> zeroMap = mapSet.get(0L);
        for (Map.Entry<Long, Integer> entry : zeroMap.entrySet()) {
            min = Math.min(min, entry.getKey());
            max = Math.max(max, entry.getKey());
        }
        Assert.assertTrue(t2 > 0 && same);
    }

    /**
     * taskExector
     *
     * @throws Exception
     */
    @Test
    public void taskTest() throws Exception {
        long adder = sequence(this::sequenceAdderBuild);
        long atomic = sequence(this::sequenceAtomicBuild);
        long sequence = sequence(this::sequenceBuild);
        Assert.assertTrue(atomic > 0 && sequence > 0 && adder > 0);
    }

    private BaseSequence sequenceBuild(long i) {
        try {
            return new Sequence(String.valueOf(i), new String[]{"./"});
        } catch (IOException exp) {
            exp.printStackTrace();
            return null;
        }
    }

    private BaseSequence sequenceAdderBuild(long i) {
        try {
            return new SequenceAdder(String.valueOf(i), new String[]{"./"});
        } catch (IOException exp) {
            exp.printStackTrace();
            return null;
        }
    }

    private BaseSequence sequenceAtomicBuild(long i) {
        try {
            return new SequenceAtomic(String.valueOf(i), new String[]{"./"});
        } catch (IOException exp) {
            exp.printStackTrace();
            return null;
        }
    }

    private long sequence(Function<Long, BaseSequence> sequenceBuild) throws Exception {
        ConcurrentHashMap<Long, BaseSequence> map = new ConcurrentHashMap((int) (10 * .75));
        for (long i = 0L; i < 10L; i += 1L) {
            BaseSequence seq = sequenceBuild.apply(i);
            if (seq != null) {
                map.put(i, sequenceBuild.apply(i));
            }
        }
        List<Future<Long>> list = new ArrayList<>(20);

        long t = System.currentTimeMillis();
        for (long i = 0L; i < 10L; i += 1L) {
            for (long j = 0L; j < 2L; j += 1L) {
                long finalI = i;
                list.add(applicationTaskExecutor.submit(() -> {
                    BaseSequence seq = map.get(finalI);
                    for (int k = 0; k < 10000; k++) {
                        seq.incr();
                    }
                    return 1L;
                }));
            }
        }
        for (Future<Long> future : list) {
            future.get();
        }
        return System.currentTimeMillis() - t;
    }
}