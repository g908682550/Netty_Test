package com.npu.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileChannel03 {
    public static void main(String[] args) throws IOException {
        FileInputStream fileInputStream = new FileInputStream("d:\\1.txt");
        FileChannel fileChannel1=fileInputStream.getChannel();
        FileOutputStream fileOutputStream = new FileOutputStream("2.txt");
        FileChannel fileChannel2 = fileOutputStream.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(5);
        while(true){
            buffer.clear();
            int i = fileChannel1.read(buffer);
            if(i==-1) break;
            buffer.flip();
            fileChannel2.write(buffer);
        }
        fileInputStream.close();
        fileOutputStream.close();

    }
}
