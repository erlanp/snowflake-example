package com.github.walle.snowflask.util;

import java.security.SecureRandom;
import java.util.BitSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

/**
 * 类snowflask, 不解决时钟回拨问题，但不写文件
 *
 * @Author 徐松
 */
public class RandomSet {
    private int shardId = 0;
    private final int million = 1048576;
    private final int million2 = 1048575;

    private Random random = new SecureRandom();

    public RandomSet(int shardId) {
        this.shardId = shardId;
    }

    private static HashMap<String, RandomSet> map = new HashMap<>(16);

    private HashMap<Long, BitSet> bitSetMap = new LinkedHashMap<>(16, 1.0f) {
        @Override
        protected boolean removeEldestEntry(Map.Entry<Long, BitSet> eldest) {
            // 超过15位 淘汰最开始的数据
            return size() > 15;
        }
    };

    private BitSet getBitSet(long i) {
        BitSet bitSet = bitSetMap.get(i);
        if (bitSet != null) {
            return bitSet;
        }
        bitSet = new BitSet(million);
        bitSetMap.put(i, bitSet);
        return bitSet;
    }

    public synchronized long gen(int times) {
        long result = -1L;
        try {
            long time = (System.currentTimeMillis() >> 9) - 1500000000L;
            BitSet bitSet = getBitSet(time);
            int nano = (int) (System.nanoTime() & million2);
            if (bitSet.get(nano) == false) {
                bitSet.set(nano);
            } else if (times <= 0) {
                return result;
            } else {
                Boolean call = true;
                int nano2 = 0;
                for (int j = 1; j <= 10; j++) {
                    nano = (nano + 1) & million2;
                    if (bitSet.get(nano) == false) {
                        bitSet.set(nano);
                        call = false;
                        break;
                    }
                    nano2 = random.nextInt(million);
                    if (bitSet.get(nano2) == false) {
                        bitSet.set(nano2);
                        nano = nano2;
                        call = false;
                        break;
                    }
                }
                if (call) {
                    return gen(times - 1);
                }
            }
            result = (time << 30) | (shardId << 20) | nano;
            return result;
        } catch (Exception e) {
            return gen(times - 1);
        } finally {
        }
    }

    public long gen() {
        return gen(10);
    }
}
