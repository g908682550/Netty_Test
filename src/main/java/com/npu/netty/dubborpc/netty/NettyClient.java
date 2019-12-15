package com.npu.netty.dubborpc.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.lang.reflect.Proxy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class NettyClient {
    private static ExecutorService executor= Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private static NettyClientHandler client;

    //编写方法使用代理模式获取一个代理对象
    public Object getBean(final Class<?> serviceClass,final String protocol){
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),new Class<?>[] {serviceClass},((proxy, method, args) -> {
            if(client==null) initClient();
            //设置要发给服务器端的信息
            //protocol是协议头，args[0]就是客户端调用api hello方法的参数
            client.setPara(protocol+args[0]);
            return executor.submit(client).get();

        }));
    }

    //初始化客户端
    private static void initClient() throws Exception{
        client=new NettyClientHandler();
        NioEventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group).channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY,true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new StringDecoder());
                        pipeline.addLast(new StringEncoder());
                        pipeline.addLast(client);
                    }
                });
        ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 7000).sync();

    }
}
