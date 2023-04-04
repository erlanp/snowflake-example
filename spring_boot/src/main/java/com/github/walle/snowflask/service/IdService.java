package com.github.walle.snowflask.service;

import com.github.walle.snowflask.util.BaseSequence;
import com.github.walle.snowflask.util.RandomSet;
import com.github.walle.snowflask.util.Sequence;
import com.github.walle.snowflask.util.SequenceAtomic;
import com.github.walle.snowflask.util.Snow;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;

/**
 * 实例工厂
 *
 * @Author 徐松
 */
@Service
@PropertySource(value = "classpath:snow.properties")
public class IdService implements InitializingBean {
    protected HashMap<String, BaseSequence> dict = new HashMap<>(16);

    protected HashMap<String, Snow> dictSnow = new HashMap<>(16);

    @Value("${dir_path:./}")
    private String dirPath;

    @Value("${shard_id}")
    private int shardId;

    private Sequence sequence;

    private Snow snow;

    private volatile RandomSet randomSet;

    private Object[] synchronizedSequenceArray = new Object[16];

    private Object[] synchronizedSnowArray = new Object[16];

    private Object synchronizedRandomSet = new Object();

    /**
     * 被spring初始化后再自己初始化
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws IOException {
        for (short i = 0; i < 16; i++) {
            synchronizedSequenceArray[i] = new Object();
            synchronizedSnowArray[i] = new Object();
        }

        sequence = new Sequence("default", dirPath.split(","));
        snow = new Snow(shardId, buildSequence("default_snow"));
    }

    public Sequence getSequence() {
        return sequence;
    }

    public Snow getSnow() {
        return snow;
    }

    public RandomSet getRandomSet() {
        if (randomSet != null) {
            return randomSet;
        }
        synchronized (synchronizedRandomSet) {
            if (randomSet != null) {
                return randomSet;
            }
            randomSet = new RandomSet(shardId);
        }
        return randomSet;
    }

    /**
     * SequenceAdder 工厂
     *
     * @param name
     * @return
     * @throws IOException
     */
    public BaseSequence buildSequence(String name) throws IOException {
        BaseSequence localSequence = dict.get(name);
        if (localSequence != null) {
            return localSequence;
        }
        // 分段锁
        synchronized (synchronizedSequenceArray[spread(name.hashCode())]) {
            localSequence = dict.get(name);
            if (localSequence != null) {
                return localSequence;
            }
            localSequence = new SequenceAtomic(name, dirPath.split(","));
            dict.put(name, localSequence);
        }
        return localSequence;
    }

    /**
     * Snow 工厂
     *
     * @param name
     * @return
     * @throws IOException
     */
    public Snow buildSnow(String name) throws IOException {
        Snow localSnow = dictSnow.get(name);
        if (localSnow != null) {
            return localSnow;
        }
        // 分段锁
        synchronized (synchronizedSnowArray[spread(name.hashCode())]) {
            localSnow = dictSnow.get(name);
            if (localSnow != null) {
                return localSnow;
            }
            localSnow = new Snow(shardId, buildSequence(name + "_snow"));
            dictSnow.put(name, localSnow);
        }
        return localSnow;
    }

    private static final int spread(int h) {
        return (h ^ (h >>> 16)) & 15;
    }
}
