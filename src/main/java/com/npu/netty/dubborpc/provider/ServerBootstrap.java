package com.npu.netty.dubborpc.provider;

import com.npu.netty.dubborpc.netty.NettyServer;

//该类启动一个服务提供者，就是NettyServer
public class ServerBootstrap {
    public static void main(String[] args) {
        NettyServer.startServer("127.0.0.1",7000);
    }
}
