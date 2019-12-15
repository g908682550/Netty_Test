package com.npu.netty.dubborpc.customer;

import com.npu.netty.dubborpc.netty.NettyClient;
import com.npu.netty.dubborpc.netty.NettyServer;
import com.npu.netty.dubborpc.publicinterface.HelloService;

public class ClientBootstrap {

    private static final String protocol="HelloService#hello#";

    public static void main(String[] args) {
        NettyClient customer = new NettyClient();
        HelloService helloService = (HelloService)customer.getBean(HelloService.class, protocol);
        String result = helloService.hello("你好吗,dubbo~？");
        System.out.println("调用的结果："+result);
    }
}
