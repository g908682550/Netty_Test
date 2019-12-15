package com.npu.netty.dubborpc.publicinterface;

//这个是服务提供方和服务消费方公用的接口
public interface HelloService {
    String hello(String msg);
}
