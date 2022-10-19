package com.gb.chatMini.client;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class IONet {


    private final Callback callback;
    private final Socket socket;
    private final InputStream inputStream;
    private final OutputStream outputStream;
    private final byte[] buffer;

    public IONet(Callback callback, Socket socket) throws IOException {
        this.callback = callback;
        this.socket = socket;
        inputStream = socket.getInputStream();
        outputStream = socket.getOutputStream();
        buffer = new byte[8192];
        Thread readThread = new Thread(this::readMessages);
        readThread.setDaemon(true);
        readThread.start();
    }

    public void sendMsg(String msg) throws IOException {
        outputStream.write(msg.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
    }
    public void sendFile(File file) throws IOException {
        try (FileInputStream is = new FileInputStream(file)){
            int read;
            outputStream.write("%f%".getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
            //


//            outputStream.write((String.format("%05d", file.getName().length())).getBytes(StandardCharsets.UTF_8));
//            outputStream.flush();
            outputStream.write((file.getName() + "%n%").getBytes(StandardCharsets.UTF_8));
//            outputStream.write(file.getName().getBytes(StandardCharsets.UTF_8));
            outputStream.flush();


            //
            try{
                while ((read = is.read(buffer)) != -1){
                    outputStream.write(buffer, 0, read);
                    outputStream.flush();
                }
            } catch (Exception e){
                e.printStackTrace();
            }
//            outputStream.write("%c%".getBytes(StandardCharsets.UTF_8));
//            outputStream.flush();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void readMessages() {
        try {
            while (true){
                int read = inputStream.read(buffer);
                String msg = new String(buffer, 0, read).trim();
                callback.onReceive(msg);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void close() throws IOException{
        outputStream.close();
        inputStream.close();
        socket.close();
    }
}
