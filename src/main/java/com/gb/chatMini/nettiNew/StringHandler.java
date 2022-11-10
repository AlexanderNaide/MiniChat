package com.gb.chatMini.nettiNew;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StringHandler extends SimpleChannelInboundHandler<String> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.debug("Client connected...");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.debug("Client Disconnected...");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String s) throws Exception {
        // ctx.fireChannelRead(s); // - пропихнуть сообщение в следующий InboundHandler
        // ctx.writeAndFlush(s); // - выпихнуть сообщение обратно на клиент, в OutboundHandler

        log.debug("Received: {}", s);
        ctx.writeAndFlush("hello " + s);

    }
}
