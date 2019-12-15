package com.npu.netty.codec;

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
        //读取从客户端发送的StudentPojo.Student
        StudentPOJO.Student student=(StudentPOJO.Student)msg;
        System.out.println("客户端发送的数据 id="+student.getId()+"名字:"+student.getName());
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
