package com.npu.netty.groupchat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class GroupChatServerHandler extends SimpleChannelInboundHandler<String> {

    //定义一个channel组，管理所有的channel
    //GlobalEventExecutor.INSTANCE  是全局的的事件执行器，是一个单例
    private static ChannelGroup channelGroup=new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    //使用一个hashMap管理
    public static Map<String,Channel> channels=new HashMap<>();

    //handlerAdded 表示连接建立，一旦连接，第一个被执行，将当前的channel加入到channelGroup中
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        //将该客户加入聊天的信息推送给其它在线的客户,该方法会将channelGroup中所有的channel遍历，并发送消息
        channelGroup.writeAndFlush("[客户端]"+channel.remoteAddress()+"加入聊天\n"+sdf.format(new Date())+"\n");
        channelGroup.add(channel);
        channels.put("id",channel);
    }

    //断开连接,将xx客户离开信息推送给当前在线的客户
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush("[客户端]"+channel.remoteAddress()+"离开了\n");
        System.out.println("channelGroup size: "+channelGroup.size());
    }

    //表示channel处于活动状态，提示xx上线
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel=ctx.channel();
        System.out.println(channel.remoteAddress()+"上线了~");
    }

    //表示channel处于不活动状态，提示xx下线了
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress()+"离线了");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        //获取到当前channel
        Channel channel = ctx.channel();
        //遍历channelGroup，根据不同情况会送不同消息
        channelGroup.forEach(ch->{
            if(channel!=ch){//不是当前channel，直接转发
                ch.writeAndFlush("[客户]"+channel.remoteAddress()+"发送了消息"+msg+"\n");
            }else{//回显一下自己发送的消息
                ch.writeAndFlush("[自己]发送了消息:"+msg);
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
