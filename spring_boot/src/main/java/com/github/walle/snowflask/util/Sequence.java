package com.github.walle.snowflask.util;

import java.io.IOException;

/**
 * 自增类
 *
 * @Author 徐松
 */
public class Sequence extends BaseSequence {

    private long sequenceValue = 0L;

    // 落盘值
    private long diskValue;

    private Disk disk;

    public Sequence(String name, String[] dirPaths) throws IOException {
        disk = new Disk(name, dirPaths);
        diskValue = disk.diskGet();

        if (diskValue > sequenceValue) {
            sequenceValue = diskValue;
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
    public synchronized long incr(short delta) throws IOException {
        sequenceValue += delta;
        if (sequenceValue >= diskValue) {
            diskValue = sequenceValue + LOG_CNT;
            disk.diskSet(diskValue);
        }
        return sequenceValue;
    }

    /**
     * 取当前
     *
     * @return
     * @throws IOException
     */
    @Override
    public long curr() throws IOException {
        return incr((short)0);
    }
}
