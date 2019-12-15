package com.npu.nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

public class GroupChatServer {
    //定义属性
    private Selector selector;
    private ServerSocketChannel listenChannel;
    private static int PORT=6667;
    //构造器
    public GroupChatServer(){
        try{
            selector=Selector.open();
            listenChannel=ServerSocketChannel.open();
            listenChannel.socket().bind(new InetSocketAddress(PORT));
            listenChannel.configureBlocking(false);
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    public void listen(){
        try{
            while(true){
                int count=selector.select(2000);
                if(count>0){//有事件处理
                    //遍历得到SelectionKey集合
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while(iterator.hasNext()){
                        SelectionKey key = iterator.next();
                        if(key.isAcceptable()){
                            SocketChannel sc = listenChannel.accept();
                            sc.configureBlocking(false);
                            //将该sc注册到Selector上
                            sc.register(selector,SelectionKey.OP_READ);
                            System.out.println(sc.getRemoteAddress()+"上线了");
                        }
                        if(key.isReadable()){
                            readData(key);
                        }
                        //当前key删除，防止重复处理
                        iterator.remove();
                    }
                }else System.out.println("等待中...");
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {

        }
    }

    /**
     * 有数据从通道过来时处理
     * @param key
     */
    private void readData(SelectionKey key){
        SocketChannel channel=null;
        try {
            channel = (SocketChannel) key.channel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int count = channel.read(buffer);
            //根据count值做处理
            if(count>0){
                //把缓冲区的数据转成字符串
                String msg = new String(buffer.array());
                //输出该消息
                System.out.println("from client: "+msg);
                //向其它的客户端转发消息(排除)
                sendInfoToOtherClients(msg,channel);
            }
        }catch (IOException e){
            try {
                System.out.println(channel.getRemoteAddress()+"离线了");
                //取消注册
                key.cancel();
                channel.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * 转发消息给其它客户（通道）
     */
    public void sendInfoToOtherClients(String msg,SocketChannel self) throws IOException{
        System.out.println("服务器转发消息中....");
        //遍历所有注册到selector上的SocketChannel，并排除self
        for(SelectionKey key:selector.keys()){
            //通过key去除对应的socketChannel
            Channel targetChannel =key.channel();
            if(targetChannel instanceof SocketChannel&&targetChannel!=self){
                SocketChannel dest=(SocketChannel)targetChannel;
                //将msg存储到buffer
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                //将buffer的数据写入通道中
                dest.write(buffer);
            }
        }
    }

    public static void main(String[] args) {
        //创建一个服务器对象
        GroupChatServer server = new GroupChatServer();
        server.listen();
    }
}
