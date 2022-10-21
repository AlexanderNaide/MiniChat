package com.gb.chatMini.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerIo {
    public static void main(String[] args) throws IOException {
        System.out.println("Server started...");
        while(true){
            try(ServerSocket serverSocket = new ServerSocket(6830)){
                Socket socket = serverSocket.accept();
                System.out.println("Client connected...");
                new Thread(new NIOHandler(socket)).start();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
