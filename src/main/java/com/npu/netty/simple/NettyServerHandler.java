package com.npu.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

/**
 * 说明
 * 我们自定义一个handler需要继承netty规定好的某个HandlerAdapter，此时才能称之为一个handler
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    //读取数据实际（这里我们可以处理客户端发送的消息）
    /*
    1、ChannelHandlerContext：上下文对象，含有管道pipeline，通道
    2、Object msg：就是客户端发送的数据，默认Object
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //如果这里有一个非常耗时的业务->异步执行->提交该channel 对应NioEventLoop的taskQueue中，

        //解决方案1 用户程序自定义的普通任务
        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000*10);
                    ctx.writeAndFlush(Unpooled.copiedBuffer("hello client~~1111",CharsetUtil.UTF_8));
                } catch (InterruptedException e) {
                    System.out.println("发生异常"+e.getMessage());
                }
            }
        });
        //用户自定义定时任务->该任务提交到scheduleTaskQueue中
        ctx.channel().eventLoop().schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000*10);
                    ctx.writeAndFlush(Unpooled.copiedBuffer("hello client~~2222",CharsetUtil.UTF_8));
                } catch (InterruptedException e) {
                    System.out.println("发生异常"+e.getMessage());
                }
            }
        },5, TimeUnit.SECONDS);

//        System.out.println("server ctx="+ctx);
//        //将msg转成一个ByteBuf,ByteBuf是Netty提供的，不是NIO的ByteBuffer
//        ByteBuf buf=(ByteBuf)msg;
//        System.out.println("client send msg: "+buf.toString(CharsetUtil.UTF_8));
//        System.out.println("client address: "+ctx.channel().remoteAddress());
    }

    //读取数据完毕
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //writeAndFlush 是write+flush,将数据写到缓存，并刷新
        //一般讲，我们对这个发送的数据进行编码
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello client~~",CharsetUtil.UTF_8));
    }

    //处理异常，一般是需要关闭通道

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
