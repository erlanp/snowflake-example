package com.github.walle.snowflask.util;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Atomic 自增
 *
 * @Author 徐松
 */
public class SequenceAtomic extends BaseSequence {
    private AtomicLong atomicLong = new AtomicLong(0);

    // 落盘值
    private long diskValue;

    private Disk disk;

    public SequenceAtomic(String name, String[] dirPaths) throws IOException {
        disk = new Disk(name, dirPaths);
        diskValue = disk.diskGet();

        if (diskValue > atomicLong.get()) {
            atomicLong.set(diskValue);
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
        long curr = atomicLong.getAndAdd(delta);
        if (curr >= diskValue) {
            synchronized (this) {
                if (atomicLong.get() >= diskValue) {
                    diskValue = atomicLong.get() + LOG_CNT;
                    disk.diskSet(diskValue);
                }
            }
        }
        return curr;
    }

    /**
     * 取当前
     *
     * @return
     * @throws IOException
     */
    @Override
    public long curr() throws IOException {
        return atomicLong.get();
    }
}
