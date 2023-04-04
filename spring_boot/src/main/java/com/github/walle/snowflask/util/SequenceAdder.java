package com.github.walle.snowflask.util;

import java.io.IOException;
import java.util.concurrent.atomic.LongAdder;

/**
 * LongAdder通过分段有最终的一致性，但没有getAndAdd原子性的函数，这个类不建议使用
 *
 * @Author 徐松
 */
public class SequenceAdder extends BaseSequence {

    private LongAdder sequenceValue = new LongAdder();

    // 落盘值
    private long diskValue;

    private Disk disk;

    public SequenceAdder(String name, String[] dirPaths) throws IOException {
        disk = new Disk(name, dirPaths);
        diskValue = disk.diskGet();

        if (diskValue > sequenceValue.sum()) {
            sequenceValue.reset();
            sequenceValue.add(diskValue);
        }
    }

    /**
     * 自增
     *
     * @param delta
     * @return
     * @throws IOException
     */
    @Override
    public long incr(short delta) throws IOException {
        long curr = sequenceValue.longValue();
        sequenceValue.add(delta);
        if (curr + 1 >= diskValue) {
            synchronized (this) {
                diskValue = curr + 1 + LOG_CNT;
                disk.diskSet(diskValue);
            }
        }
        return curr + 1;
    }

    /**
     * 取当前
     *
     * @return
     * @throws IOException
     */
    @Override
    public long curr() throws IOException {
        return sequenceValue.longValue();
    }
}
