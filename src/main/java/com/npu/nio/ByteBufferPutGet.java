package com.npu.nio;

import java.nio.ByteBuffer;

public class ByteBufferPutGet {
    public static void main(String[] args) {
        ByteBuffer buffer=ByteBuffer.allocate(64);

        buffer.putInt(100);
        buffer.putLong(9L);
        buffer.putChar('a');

        buffer.flip();

        System.out.println();

        System.out.println(buffer.getInt());

    }
}
