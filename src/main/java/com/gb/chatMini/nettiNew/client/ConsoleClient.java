package com.gb.chatMini.nettiNew.client;

import com.gb.chatMini.nettiNew.StringMessage;

import java.time.LocalDateTime;
import java.util.Scanner;

public class ConsoleClient {
    public static void main(String[] args) {
        NettyNet net = new NettyNet(System.out::println);
        Scanner in = new Scanner(System.in);
        while (in.hasNextLine()){
            String msg = in.nextLine();
            net.sendMessage(new StringMessage(msg, LocalDateTime.now()));
        }
    }
}
