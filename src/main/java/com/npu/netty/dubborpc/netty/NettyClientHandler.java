package com.npu.netty.dubborpc.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.Callable;

public class NettyClientHandler extends ChannelInboundHandlerAdapter implements Callable {

    private ChannelHandlerContext context;//上下文
    private String result;//返回的结果
    private String para;//客户端调用方法时传入的参数

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    //收到服务器的数据后调用方法
    //唤醒等待的线程
    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        result=(String)msg;
        notify();
    }

    //与服务器创建好连接后调用
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        context=ctx;
    }

    //被代理对象调用，发送数据给服务器，等待被唤醒（channelRead），返回结果
    @Override
    public synchronized Object call() throws Exception {
        context.writeAndFlush(para);
        wait();//等待channelRead方法获取到服务器的结果后，唤醒
        return result;//服务方返回的结果
    }

    void setPara(String para){
        this.para=para;
    }
}
