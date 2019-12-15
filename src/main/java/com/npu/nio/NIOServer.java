package com.npu.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NIOServer {

    public static void main(String[] args) throws Exception{
        //创建ServerSocketChannel->ServerSocket
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //得到一个Selector对象
        Selector selector=Selector.open();
        //绑定一个端口6666，在服务器端监听
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));
        //设置为非阻塞
        serverSocketChannel.configureBlocking(false);
        //把serverSocketChannel注册到selector 关心事件为OP_ACCEPT
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        //循环等待客户端连接
        while(true){
            if(selector.select(1000)==0){
                System.out.println("服务器等待1S，无连接");
                continue;
            }
            //如果返回的不是0,就获取到相关的Selection集合
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            //遍历集合
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
            while(keyIterator.hasNext()){
                //获取到SelectionKey
                SelectionKey key = keyIterator.next();
                //根据key对应的通道发生的事件做相应的处理
                if(key.isAcceptable()){//如果是OP_ACCEPT,有新的客户端连接
                    //给该客户端生成一个socketChannel
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    System.out.println("客户端连接成功 生成了一个socketChannel"+socketChannel.hashCode());
                    //将socketChannel设置为非阻塞
                    socketChannel.configureBlocking(false);
                    //将socketChannel注册到selector，关注事件为OP_READ，同时给socketChannel关联一个buffer
                    socketChannel.register(selector,SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }
                if(key.isReadable()){
                    SocketChannel socketChannel=(SocketChannel)key.channel();
                    ByteBuffer buffer = (ByteBuffer)key.attachment();
                    socketChannel.read(buffer);
                    System.out.println("from client "+new String(buffer.array()));
                }
                //手动从集合中移动当前的selectionKey,防止重复操作
                keyIterator.remove();
            }
        }
    }
}
