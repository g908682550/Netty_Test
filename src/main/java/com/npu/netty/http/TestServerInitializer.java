package com.npu.netty.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

public class TestServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        //加入一个netty提供的httpServerCodec codec=>[coder-decoder]
        //HttpServerCodec说明：是netty提供的处理http的编码解码器
        pipeline.addLast("MyHttpServerCodec",new HttpServerCodec());
        pipeline.addLast("My TestHttpServerHandler",new TestHttpServerHandler());
    }
}
