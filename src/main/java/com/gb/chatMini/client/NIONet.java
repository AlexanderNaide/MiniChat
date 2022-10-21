package com.gb.chatMini.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class NIONet {


    private final Callback callback;
    private final Socket socket;
    private final DataInputStream inputStream;
    private final DataOutputStream outputStream;
    private final byte[] buffer;

    public NIONet(Callback callback, Socket socket) throws IOException {
        this.callback = callback;
        this.socket = socket;
        inputStream = new DataInputStream(socket.getInputStream());
        outputStream = new DataOutputStream(socket.getOutputStream());
        buffer = new byte[8192];
        Thread readThread = new Thread(this::readMessages);
        readThread.setDaemon(true);
        readThread.start();
    }

    public void writeUTF(String msg) throws IOException {
        outputStream.writeUTF(msg);
        outputStream.flush();
    }

    public void writeLong (long size) throws IOException {
        outputStream.writeLong(size);
        outputStream.flush();
    }

    public void writeBytes(byte[] bytes, int off, int cnt) throws IOException {
        outputStream.write(bytes, off, cnt);
        outputStream.flush();
    }

    public void sendMsg(String msg) throws IOException {
        outputStream.write(msg.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
    }

    private void readMessages() {
        try {
            while (true) {
                int read = inputStream.read(buffer);
                String msg = new String(buffer, 0, read, StandardCharsets.UTF_8).trim();
                callback.onReceive(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() throws IOException {
        outputStream.close();
        inputStream.close();
        socket.close();
    }
}
