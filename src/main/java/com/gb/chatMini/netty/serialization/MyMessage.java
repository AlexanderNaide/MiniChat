package com.gb.chatMini.netty.serialization;

import java.io.Serial;
import java.io.Serializable;

public class MyMessage implements Serializable {
    @Serial
    private static final long serialVersionUID = 5193392663743561680L;
    private final String text;

    public MyMessage(String text) {
        this.text = text;
    }

    public String getText(){
        return text;
    }
}
