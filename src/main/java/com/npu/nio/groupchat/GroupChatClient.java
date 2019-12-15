package com.npu.nio.groupchat;

import com.sun.org.apache.bcel.internal.generic.Select;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class GroupChatClient {
    //定义相关属性
    private final String HOST="127.0.0.1";
    private final int PORT=6667;
    private Selector selector;
    private SocketChannel socketChannel;
    private String username;

    /**
     * 构造器，完成初始化工作
     */
    GroupChatClient() throws IOException {
        selector=Selector.open();
        //连接服务器
        socketChannel=socketChannel.open(new InetSocketAddress(HOST,PORT));
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
        username= socketChannel.getLocalAddress().toString().substring(1);
        System.out.println(username+" is ok...");
    }

    /**
     * 向服务器端发送消息
     * @param info
     */
    public void sendInfo(String info){
        info=username+"说："+info;
        try{
            socketChannel.write(ByteBuffer.wrap(info.getBytes()));
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * 读取从服务器端的消息
     */
    public void readInfo(){
        try{
            int readChannels = selector.select(2000);
            if(readChannels>0){//有事件发生的通道
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while(iterator.hasNext()){
                    SelectionKey key = iterator.next();
                    if(key.isReadable()){
                        //得到相关通道
                        SocketChannel channel=(SocketChannel)key.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        channel.read(buffer);
                        String msg=new String(buffer.array());
                        System.out.println(msg.trim());
                    }
                   iterator.remove();
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException{
        //启动客户端
        GroupChatClient client=new GroupChatClient();
        //启动一个线程,每隔3s，读取从服务器发送数据
        new Thread(){
            public void run(){
                while(true){
                    client.readInfo();
                    try{
                        Thread.currentThread().sleep(3000);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        }.start();
        Scanner scanner = new Scanner(System.in);
        while(scanner.hasNextLine()){
            String s = scanner.nextLine();
            client.sendInfo(s);
        }
    }
}
