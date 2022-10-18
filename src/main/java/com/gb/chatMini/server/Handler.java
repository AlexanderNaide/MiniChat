package com.gb.chatMini.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Handler implements Runnable{
    private boolean running;
    private final byte[] buffer;
    private final InputStream inputStream;
    private final OutputStream outputStream;
    private final Socket socket;

    public Handler(Socket socket) throws IOException {
        this.running = true;
        this.socket = socket;
        buffer = new byte[8192];
        inputStream = socket.getInputStream();
        outputStream = socket.getOutputStream();
    }

    @Override
    public void run() {
        try {
            while (running){
                int read = inputStream.read(buffer);
                String message = new String(buffer, 0, read).trim();
                if (message.equals("quit")){
                    outputStream.write(("Client disconnected!\n").getBytes(StandardCharsets.UTF_8));
                    close();
                    System.out.println("Client disconnected...");
                    break;
                }
                System.out.println("Received: " + message);
                outputStream.write((message + "\n").getBytes(StandardCharsets.UTF_8));
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void close() throws IOException {
        outputStream.close();
        inputStream.close();
        socket.close();
    }
}
