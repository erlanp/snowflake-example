/*
 * Copyright
 */

package com.github.walle.snowflask.util;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Disk UT
 *
 * @author
 * @date 2023-03-17
 */
public class DiskTest {
    private Disk disk;

    @BeforeEach
    public void before() throws IOException {
        String name = "test2";
        String[] dirPaths = new String[]{"./"};
        disk = new Disk(name, dirPaths);
    }

    /**
     * close
     *
     * @throws Exception
     */
    @Test
    public void closeTest() throws Exception {
        String error = null;

        try {
            disk.close();
        } catch (Exception exp) {
            error = Arrays.toString(exp.getStackTrace());
        }
        Assertions.assertTrue(error == null);
    }
}
