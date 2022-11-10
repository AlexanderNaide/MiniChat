package com.gb.chatMini.nettiNew.client;

import com.gb.chatMini.nettiNew.AbstractMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ClientMessageHandler extends SimpleChannelInboundHandler<AbstractMessage> {

    private final OnMessageReceived callback;
    public ClientMessageHandler(OnMessageReceived callback) {
        this.callback = callback;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext,
                                AbstractMessage abstractMessage) throws Exception {
        callback.onReceive(abstractMessage);
    }
}
