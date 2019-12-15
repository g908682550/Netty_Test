package com.npu.netty.webSocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;


public class MyServer {
        public static void main(String[] args) throws Exception{
            //创建两个线程组
            NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
            NioEventLoopGroup workGroup = new NioEventLoopGroup();
            try{
                ServerBootstrap serverBootstrap=new ServerBootstrap();
                serverBootstrap.group(bossGroup,workGroup).channel(NioServerSocketChannel.class).handler(new LoggingHandler(LogLevel.INFO))
                        .childHandler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel ch) throws Exception {
                                ChannelPipeline pipeline = ch.pipeline();
                                //基于Http协议，使用Http的编码解码器
                                pipeline.addLast(new HttpServerCodec());
                                //是以块方式写，添加ChunkedWrite处理器
                                pipeline.addLast(new ChunkedWriteHandler());
                                /*
                                说明：
                                1、http数据在传输过程中是分段的,HttpObjectAggregator可以将多段聚合起来
                                2、这就是为什么当浏览器发送大量数据时，就会发出多次Http请求
                                 */
                                pipeline.addLast(new HttpObjectAggregator(8192));
                                /*
                                1、对于webSocket，它的数据是以 帧(frame)形式传递
                                2、可以看到WebSocketFrame有多个子类
                                3、浏览器请求时， ws://localhost:7000/xxx
                                4、WebSocketServerProtocolHandler 核心功能将http协议升级为ws协议，保持长连接
                                 */
                                pipeline.addLast(new WebSocketServerProtocolHandler("/hello"));
                                //自定义handler，处理业务逻辑
                                pipeline.addLast(new MyTextWebSocketFrameHandler());
                            }
                        });
                ChannelFuture channelFuture = serverBootstrap.bind(7000).sync();
                channelFuture.channel().closeFuture().sync();
            }finally {
                bossGroup.shutdownGracefully();
                workGroup.shutdownGracefully();
            }
    }
}
