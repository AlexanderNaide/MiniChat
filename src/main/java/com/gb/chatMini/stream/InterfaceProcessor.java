package com.gb.chatMini.stream;

public class InterfaceProcessor {

    void actionWith(String arg, Action action){
        action.doAction(arg);
    }

    String convert(Integer arg, Converter<Integer, String> converter){
        return converter.convert(arg);
    }

}
