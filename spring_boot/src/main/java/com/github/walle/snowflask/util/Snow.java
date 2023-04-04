package com.github.walle.snowflask.util;

import java.io.IOException;

/**
 * 类snowflask, 解决时钟回拨问题
 *
 * @Author 徐松
 */
public class Snow {

    private BaseSequence sequence;

    private long mark = 0L;

    private long lastTime = 0L;

    private short serialNumber1 = 0;

    private short serialNumber2 = 0;

    private int shardId;

    private static final int SERIAL_NUMBER = 256;

    public Snow(int shardId, BaseSequence sequence) {
        this.shardId = shardId;
        this.sequence = sequence;
        lastTime = System.currentTimeMillis() - 1500000000000L;
    }

    /**
     * 产生id
     *
     * @return
     * @throws IOException
     */
    public synchronized long gen() throws IOException {
        long result = -1L;
        long currentTime = System.currentTimeMillis() - 1500000000000L;
        while (lastTime > currentTime) {
            // Clock moved 补闰秒 或者 防止时间回拨
            mark = sequence.incr((short) ((lastTime - currentTime + 1023) & 32767));
        }
        // currentTime % 2
        if ((currentTime & 1) == 1L) {
            if (serialNumber1 == 0) {
                serialNumber2 = 0;
            }
            serialNumber1++;
            if (serialNumber1 >= SERIAL_NUMBER) {
                serialNumber1 = 0;
                serialNumber2 = 0;
                mark = sequence.incr();
            }
            result = (currentTime + mark * 2L) << 16 | (shardId << 8) | ((currentTime & 255) ^ serialNumber1);
        } else {
            if (serialNumber2 == 0) {
                serialNumber1 = 0;
            }
            serialNumber2++;
            if (serialNumber2 >= SERIAL_NUMBER) {
                serialNumber2 = 0;
                serialNumber1 = 0;
                mark = sequence.incr();
            }
            result = (currentTime + mark * 2L) << 16 | (shardId << 8) | ((currentTime & 255) ^ serialNumber2);
        }
        lastTime = currentTime;
        return result;
    }
}