package com.npu.nio;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileChannel02 {
    public static void main(String[] args) throws IOException {
        File file=new File("d:\\1.txt");
        //创建一个输出流
        FileInputStream fileInputStream = new FileInputStream(file);
        //通过该输出流获取filechannel 真实类型是FileChannelImpl
        FileChannel channel = fileInputStream.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        channel.read(buffer);
        System.out.println(new String(buffer.array()));
        fileInputStream.close();
    }
}
