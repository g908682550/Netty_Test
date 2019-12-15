package com.npu.netty.codec2;

import com.npu.netty.codec.StudentPOJO;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;

public class NettyServer {
    public static void main(String[] args) throws Exception {
        //创建BossGroup和WorkGroup
        //创建两个线程组，BossGroup只是处理连接请求，真正的和客户端业务处理会交给WorkGroup完成，两个都是无限循环
        NioEventLoopGroup bossGroup=new NioEventLoopGroup();
        NioEventLoopGroup workGroup=new NioEventLoopGroup();
        //创建服务器端的启动对象，配置参数
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup,workGroup)//设置两个线程组
                    .channel(NioServerSocketChannel.class)//使用NioServerSocketChannel作为服务器的通道实现
                    .option(ChannelOption.SO_BACKLOG,128)//设置线程队列等待连接的个数
                    .childOption(ChannelOption.SO_KEEPALIVE,true)//设置保持活动连接状态
                    .childHandler(new ChannelInitializer<SocketChannel>() {//创建一个通道测试对象（匿名对象）,给pipeline设置处理器
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast("decoder",new ProtobufDecoder(MyDataInfo.MyMessage.getDefaultInstance()));//需指定对哪种对象进行解码
                            ch.pipeline().addLast(new NettyServerHandler());
                        }
                    });//给我们的workGroup的EventLoop对应的管道设置处理器
            System.out.println("Server is ready....");
            //启动服务器，绑定一个端口并且同步，生成了一个ChannelFuture对象
            ChannelFuture cf = bootstrap.bind(6668).sync();
            //对关闭通道进行监听
            cf.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}
