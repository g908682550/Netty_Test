package com.npu.nio;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileChannel01 {
    public static void main(String[] args) throws IOException {
        String s="hello world";
        //创建一个输出流
        FileOutputStream fileOutputStream = new FileOutputStream("d:\\1.txt");
        //通过该输出流获取filechannel 真实类型是FileChannelImpl
        FileChannel filechannel = fileOutputStream.getChannel();
        ByteBuffer buffer=ByteBuffer.allocate(1024);
        //将s放入buffer中
        buffer.put(s.getBytes());
        buffer.flip();
        filechannel.write(buffer);
        fileOutputStream.close();
    }
}
