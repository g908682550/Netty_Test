package com.npu.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;
import java.util.Arrays;

public class NettyByteBuf02 {
    public static void main(String[] args) {
        //创建ByteBuf
        ByteBuf byteBuf = Unpooled.copiedBuffer("hello,world!", CharsetUtil.UTF_8);
        if(byteBuf.hasArray()){
            System.out.println(new String(byteBuf.array(), Charset.forName("utf-8")));
        }
    }
}
