package com.gb.chatMini.nettiNew.test;

import lombok.extern.slf4j.Slf4j;

@Slf4j // Такая анотация добавляет логер
public class Test {
    public static void main(String[] args) {
        User user = new User();
        user.setAge(25);
        user.setName("Ivan");
        log.debug("User: {}", user);
    }
}
