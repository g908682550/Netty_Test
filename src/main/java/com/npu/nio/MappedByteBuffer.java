package com.npu.nio;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.Random;
import java.util.RandomAccess;

/**
 * MappedBuffer可让文件直接在内存（堆外）中修改，即操作系统不需要拷贝一次
 */
public class MappedByteBuffer {
    public static void main(String[] args) throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile("2.txt", "rw");
        FileChannel channel = randomAccessFile.getChannel();
        /**
         * 参数1：使用的是读写模式
         * 参数2：可以直接修改的起始位置
         * 参数3：映射到内存的大小（不是索引），即2.txt的多少个字节映射到内存，可以直接修改的范围就是0-5
         * 实际类型是DirectByteBuffer
         */
        java.nio.MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);
        map.put(0,(byte)'H');
        map.put(2,(byte)'G');
        randomAccessFile.close();
    }
}
