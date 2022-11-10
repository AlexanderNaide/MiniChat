package com.gb.chatMini.nettiNew.test;

import lombok.Data;

@Data // Такая анотация от lombok создает: геттеры, сеттеры, toString
public class User {
    private int age;
    private String name;
}
