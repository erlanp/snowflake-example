package com.github.walle.snowflask.util;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * 持久化类
 *
 * @Author 徐松
 */
public class Disk implements AutoCloseable {
    // 落盘文件
    private RandomAccessFile[] files = null;

    public Disk(String name, String[] dirPaths) throws IOException {
        files = new RandomAccessFile[dirPaths.length];
        for (int i = 0; i < dirPaths.length; i++) {
            String endStr = dirPaths[i].substring(dirPaths.length - 1);
            String dirPath = ("\\".equals(endStr) || "/".equals(endStr)) ? dirPaths[i] : (dirPaths[i] + "/");
            File dir = new File(dirPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            files[i] = new RandomAccessFile(dirPath + name + ".txt", "rw");
        }
    }

    public long diskGet() throws IOException {
        long readLong = 0L;
        for (RandomAccessFile file : files) {
            long length = file.length();
            if (length == 0L) {
                continue;
            }
            file.seek(length - 8L);
            readLong = Math.max(readLong, file.readLong());
        }
        return readLong;
    }

    public void diskSet(long value) throws IOException {
        for (RandomAccessFile file : files) {
            // 避免多次写固态硬盘同一个位置 原来是 file.seek(0)可能写坏一点固态硬盘
            file.seek(value >> 16 << 3);
            file.writeLong(value);
        }
    }

    @Override
    public void close() {
        if (files != null) {
            for (RandomAccessFile file : files) {
                try {
                    file.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
