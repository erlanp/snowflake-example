package com.github.walle.snowflask.util;

import java.io.IOException;

/**
 * 自增基类
 *
 * @Author 徐松
 */
public abstract class BaseSequence {
    /**
     * 落盘间隔值
     */
    protected static final short LOG_CNT = 256;

    /**
     * 自增1
     *
     * @return
     * @throws IOException
     */
    public long incr() throws IOException {
        return incr((short) 1);
    }

    /**
     * 自增
     *
     * @param delta
     * @return
     * @throws IOException
     */
    public abstract long incr(short delta) throws IOException;

    /**
     * 取当前
     *
     * @return
     * @throws IOException
     */
    public abstract long curr() throws IOException;
}
