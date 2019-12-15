package com.npu.netty.inboundhandlerandoutbounthandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class MyByteToLongDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        //Long为8个字节,需要判断有8个字节才能读取一个Long
        if(in.readableBytes()>=8){
            out.add(in.readLong());
        }
    }
}
