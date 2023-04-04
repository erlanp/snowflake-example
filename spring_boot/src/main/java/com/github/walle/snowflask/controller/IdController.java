package com.github.walle.snowflask.controller;

import com.github.walle.snowflask.service.IdService;
import com.github.walle.snowflask.util.BaseSequence;
import com.github.walle.snowflask.util.Snow;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * 对外接口
 *
 * @Author 徐松
 */
@RestController
public class IdController {
    @Resource
    private IdService idService;

    @Resource(name = "taskExecutor")
    ThreadPoolTaskExecutor applicationTaskExecutor;

    /**
     * 使用线程池的雪花列表
     *
     * @param ids
     * @param limit
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/snow_task_list/{ids}")
    public Map<String, List<String>> snowTaskList(@PathVariable String ids, @RequestParam(value = "limit",
            defaultValue = "63") Short limit) throws ExecutionException, InterruptedException {
        limit = getLimit(limit);

        HashSet<String> set = new HashSet<>(Arrays.asList(ids.split(",")));
        Map<String, Future<List<String>>> map = new HashMap<>(set.size());
        for (String key : set) {
            map.put(key, applicationTaskExecutor.submit(snowCallable(Long.valueOf(key), limit)));
        }
        Map<String, List<String>> result = new HashMap<>(set.size());
        for (Map.Entry<String, Future<List<String>>> entry : map.entrySet()) {
            result.put(entry.getKey(), entry.getValue().get());
        }
        return result;
    }

    private Callable<List<String>> snowCallable(Long id, Short limit) {
        return () -> {
            return snowList(id, limit);
        };
    }

    /**
     * 雪花列表
     *
     * @param id
     * @param limit
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/snow_list/{id}")
    public List<String> snowList(@PathVariable Long id,
                                 @RequestParam(value = "limit", defaultValue = "63") Short limit) throws IOException {
        Snow snow = idService.buildSnow(getId(id));
        limit = getLimit(limit);
        List<String> snowList = new ArrayList<>(limit);
        for (int i = 0; i < limit; i++) {
            snowList.add(String.valueOf(snow.gen()));
        }
        return snowList;
    }

    /**
     * 雪花随机
     *
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/random_set")
    public long randomSet() throws IOException {
        return idService.getRandomSet().gen();
    }

    /**
     * 雪花默认
     *
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/snow")
    public long snow() throws IOException {
        return idService.getSnow().gen();
    }

    /**
     * 雪花 自定义
     *
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/snow/{id}")
    public long snow(@PathVariable Long id) throws IOException {
        Snow snow = idService.buildSnow(getId(id));
        return snow.gen();
    }

    @RequestMapping(value = "/sequence_list/{id}")
    public List<Long> sequenceList(@PathVariable Long id,
                                   @RequestParam(value = "limit", defaultValue = "63") Short limit) throws IOException {
        BaseSequence sequence = idService.buildSequence(getId(id));
        List<Long> seqList = new ArrayList<>(2);

        long curr = sequence.incr(limit);
        seqList.add(curr - limit);
        seqList.add(curr);
        return seqList;
    }

    @RequestMapping(value = "/sequence")
    public long sequence() throws IOException {
        return idService.getSequence().incr();
    }

    /**
     * 自增
     *
     * @param id
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/sequence/{id}")
    public long sequence(@PathVariable Long id) throws IOException {
        BaseSequence sequence = idService.buildSequence(getId(id));
        return sequence.incr();
    }

    private static String getId(Long id) {
        return id.toString();
    }

    private static short getLimit(Short limit) {
        if (limit == 0) {
            return (short) 100;
        } else {
            return (short) Math.min(Math.abs(limit), 4095);
        }
    }
}
