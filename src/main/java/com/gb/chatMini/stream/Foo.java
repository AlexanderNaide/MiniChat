package com.gb.chatMini.stream;


@FunctionalInterface
public interface Foo {
    void foo();
    default void raf(){
        foo();
    }
}
