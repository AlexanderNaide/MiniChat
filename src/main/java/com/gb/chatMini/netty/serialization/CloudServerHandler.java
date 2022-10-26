package com.gb.chatMini.netty.serialization;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

public class CloudServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client connected...");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try{
            if (ctx == null){
                return;
            }
            System.out.println(msg.getClass());
            if (msg instanceof MyMessage){
                System.out.println("Client text message: " + ((MyMessage) msg).getText());
                MyMessage answer = new MyMessage("Hello Client!");
                ctx.write(answer);
            } else {
                System.out.println("Server received wrong object!");
            }

        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
