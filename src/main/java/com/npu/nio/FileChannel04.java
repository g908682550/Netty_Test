package com.npu.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class FileChannel04 {
    public static void main(String[] args) throws IOException {
        FileInputStream fileInputStream = new FileInputStream("C:\\Users\\gy136\\Desktop\\校徽.jpg");
        FileOutputStream fileOutputStream = new FileOutputStream("D:\\a.jpg");

        FileChannel sourceChannel = fileInputStream.getChannel();
        FileChannel destChannel=fileOutputStream.getChannel();

        destChannel.transferFrom(sourceChannel,0,sourceChannel.size());

        fileInputStream.close();
        fileOutputStream.close();

    }
}
