package com.npu.nio;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * scattering:将数据写入buffer时，可以采用buffer数组，依次写入[分散]
 * gathering:从buffer读取数据时，也可采用buffer数组，依次读
 */
public class ScatteringAndGatheringTest {
    public static void main(String[] args) throws Exception{
        //使用ServerSocketChannel和SocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress address = new InetSocketAddress(7000);
        serverSocketChannel.socket().bind(address);

        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0]=ByteBuffer.allocate(5);
        byteBuffers[1]=ByteBuffer.allocate(3);
        SocketChannel socketChannel = serverSocketChannel.accept();
        int messageLength=8;
        while(true) {
            int byteRead = 0;
            while (byteRead < messageLength) {
                long l = socketChannel.read(byteBuffers);
                byteRead += l;//累计读取的字节数
                System.out.println("byteRead=" + byteRead);
                Arrays.asList(byteBuffers).stream().map(byteBuffer ->
                        "position=" + byteBuffer.position() + ", limit=" + byteBuffer.limit()
                ).forEach(System.out::println);
            }
            //将所有buffer进行反转
            Arrays.asList(byteBuffers).forEach(byteBuffer -> byteBuffer.flip());
            //将数据读出显示到客户端
            long byteWrite=0;
            while(byteWrite<messageLength){
                long l = socketChannel.write(byteBuffers);
                byteWrite+=l;
            }
            Arrays.asList(byteBuffers).forEach(byteBuffer -> byteBuffer.clear());
            System.out.println("byteRead:="+byteRead
                    +",byteWrite="+byteWrite+",messagelength"+messageLength);
        }
    }
}
