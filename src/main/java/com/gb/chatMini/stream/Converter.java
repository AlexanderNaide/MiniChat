package com.gb.chatMini.stream;

public interface Converter<T, V> {

    V convert(T arg);

}
