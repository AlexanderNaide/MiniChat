package com.gb.chatMini.server;

import org.w3c.dom.ls.LSOutput;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Handler implements Runnable {
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
            while (running) {
                int read = inputStream.read(buffer);
                String message = new String(buffer, 0, read).trim();
                if (message.equals("%f%")) {
                    Thread wf = new Thread(new writeFile());
                    wf.start();
                    wf.join();
                    System.out.println("The file is saved.");
                    outputStream.write(("The file is saved." + "\n").getBytes(UTF_8));
                } else {
                    System.out.println("Received: " + message);
                    outputStream.write((message + "\n").getBytes(UTF_8));
                }
                if (message.equals("quit")) {
                    outputStream.write(("Client disconnected!\n").getBytes(UTF_8));
                    close();
                    System.out.println("Client disconnected...");
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void close() throws IOException {
        outputStream.close();
        inputStream.close();
        socket.close();
    }

    private class writeFile implements Runnable {
        @Override
        public void run() {
            try {
                String name;
                BufferedOutputStream bos;
                int r;
                int nameLength;
                long fileLight;
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < 4; i++) {
                    r = inputStream.read();
                    sb.append((char) r);
                }
                nameLength = Integer.parseInt(sb.toString());
                sb.setLength(0);
                for (int i = 0; i < 20; i++) {
                    r = inputStream.read();
                    sb.append((char) r);
                }
                fileLight = Long.parseLong(sb.toString());
                sb.setLength(0);

                byte[] byteName = new byte[nameLength];
                for (int i = 0; i < nameLength; i++) {
                    r = inputStream.read();
                    byteName[i] = (byte) r;
                }
                name = new String(byteName, UTF_8);
                File file = new File("files/" + name);
                bos = new BufferedOutputStream(new FileOutputStream(file));

                long readFile = 0;

                while (readFile < fileLight) {
                    int read = inputStream.read(buffer);
                    bos.write(buffer, 0, read);
                    readFile += read;
                }
                bos.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}