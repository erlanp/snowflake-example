/*
 * Copyright
 */

package com.github.walle.snowflask.controller;

import com.github.walle.snowflask.service.IdService;
import com.github.walle.snowflask.util.RandomSet;
import com.github.walle.snowflask.util.Sequence;
import com.github.walle.snowflask.util.SequenceAdder;
import com.github.walle.snowflask.util.Snow;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.LongAdder;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * IdController UT
 *
 * @Author 徐松
 */
public class IdControllerTest {
    @InjectMocks
    private IdController idController;

    @Mock
    private IdService idService;

    @Mock
    private ThreadPoolTaskExecutor applicationTaskExecutor;

    @BeforeEach
    public void before() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * snowTaskList
     *
     * @throws Exception
     */
    @Test
    public void snowTaskListTest() throws Exception {
        String ids = "1";
        Short limit = (short) 0;

        Snow then1 = mock(Snow.class);
        doAnswer((InvocationOnMock invocation) -> {
            return then1;
        }).when(idService).buildSnow(nullable(String.class));

        Future then5 = mock(Future.class);
        doAnswer((InvocationOnMock invocation) -> {
            Callable<List<String>> callable = invocation.getArgument(0, Callable.class);
            callable.call();
            return then5;
        }).when(applicationTaskExecutor).submit(nullable(Callable.class));
        Map result = idController.snowTaskList(ids, limit);
        Assert.assertTrue(result != null);
    }

    /**
     * seq
     *
     * @throws Exception
     */
    @Test
    public void seqTest() throws Exception {
        Long id = 429496L;

        SequenceAdder then0 = mock(SequenceAdder.class);
        doAnswer((InvocationOnMock invocation) -> {
            return then0;
        }).when(idService).buildSequence(nullable(String.class));
        long result = idController.sequence(id);
        Assert.assertTrue(result != 1L);
    }

    /**
     * snowList
     *
     * @throws Exception
     */
    @Test
    public void snowListTest() throws Exception {
        Long id = 429496L;
        Short limit = (short) 0;

        Sequence sequence = new Sequence("test_seq", new String[]{"./"});
        Snow then1 = new Snow(1, sequence);
        doAnswer((InvocationOnMock invocation) -> {
            return then1;
        }).when(idService).buildSnow(nullable(String.class));
        List result = idController.snowList(id, limit);
        Assert.assertTrue(result != null && result.toString().indexOf("[") == 0);
    }

    /**
     * snowList
     *
     * @throws Exception
     */
    @Test
    public void snowListTwoTest() throws Exception {
        Long id = 429498L;
        Short limit = (short) 1;

        Sequence sequence = new Sequence("test_seq2", new String[]{"./"});
        Snow then1 = new Snow(1, sequence);
        doAnswer((InvocationOnMock invocation) -> {
            return then1;
        }).when(idService).buildSnow(nullable(String.class));
        List result = idController.snowList(id, limit);
        Assert.assertTrue(result != null && result.toString().indexOf("[") == 0);
    }

    /**
     * snow
     *
     * @throws Exception
     */
    @Test
    public void snowTest() throws Exception {

        LongAdder l = new LongAdder();
        l.increment();
        l.add(15L);

        Long id = 429496L;

        Snow then1 = mock(Snow.class);
        doAnswer((InvocationOnMock invocation) -> {
            return then1;
        }).when(idService).buildSnow(nullable(String.class));
        long result = idController.snow(id);
        Assert.assertTrue(result != 1L);
    }

    /**
     * seqList
     *
     * @throws Exception
     */
    @Test
    public void seqListTest() throws Exception {
        Long id = 4294967L;
        Short limit = (short) 0;

        SequenceAdder then0 = mock(SequenceAdder.class);;
        doAnswer((InvocationOnMock invocation) -> {
            return then0;
        }).when(idService).buildSequence(nullable(String.class));
        List result = idController.sequenceList(id, limit);
        Assert.assertTrue(result != null && result.toString().indexOf("[") == 0);
    }

    /**
     * snow
     *
     * @throws Exception
     */
    @Test
    public void snowTwoTest() throws Exception {

        Snow then3 = mock(Snow.class);
        when(idService.getSnow()).thenReturn(then3);
        long result = idController.snow();
        Assert.assertTrue(result == 0L);
    }

    /**
     * sandomSet
     *
     * @throws Exception
     */
    @Test
    public void sandomSetTest() throws Exception {
        RandomSet then3 = mock(RandomSet.class);
        when(idService.getRandomSet()).thenReturn(then3);
        long result = idController.randomSet();
        Assert.assertTrue(result == 0L);
    }

    /**
     * sequence
     *
     * @throws Exception
     */
    @Test
    public void sequenceTwoTest() throws Exception {
        Sequence then2 = mock(Sequence.class);
        when(idService.getSequence()).thenReturn(then2);

        long result = idController.sequence();
        Assert.assertTrue(result != 1L);
    }
}
