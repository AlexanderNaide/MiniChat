package com.gb.chatMini.nettiNew.client;

import com.gb.chatMini.nettiNew.AbstractMessage;

public interface OnMessageReceived {

    void onReceive(AbstractMessage msg);

}
