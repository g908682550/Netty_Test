package com.npu.netty.dubborpc.provider;

import com.npu.netty.dubborpc.publicinterface.HelloService;

public class HelloServiceImpl implements HelloService {
    @Override
    public String hello(String msg) {
        System.out.println("received msg="+msg);
        if(msg!=null){
            return "I have received your msg："+msg;
        }else
            return "I have received your msg";
    }
}
