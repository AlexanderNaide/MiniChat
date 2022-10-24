package com.gb.chatMini.nio;

import com.gb.chatMini.client.Callback;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class NIONet {


    private final Callback callback;
    private final Socket socket;
    private final DataInputStream inputStream;
    private final DataOutputStream outputStream;
    private ByteBuffer buffer;

//    private SocketChannel socketChannel;

    public NIONet(Callback callback, Socket socket) throws IOException {
        this.callback = callback;
        this.socket = socket;
//        socketChannel = socket.getChannel();
        inputStream = new DataInputStream(socket.getInputStream());
        outputStream = new DataOutputStream(socket.getOutputStream());
        buffer = ByteBuffer.allocate(5);
        Thread readThread = new Thread(this::readMessages);
        readThread.setDaemon(true);
        readThread.start();
    }

/*    public void writeUTF(String msg) throws IOException {
        outputStream.writeUTF(msg);
        outputStream.flush();
    }*/

/*    public void writeLong (long size) throws IOException {
        outputStream.writeLong(size);
        outputStream.flush();
    }*/

/*    public void writeBytes(byte[] bytes, int off, int cnt) throws IOException {
        outputStream.write(bytes, off, cnt);
        outputStream.flush();
    }*/

    public void sendMsg(String msg) throws IOException {
        outputStream.writeUTF(msg);
//        socketChannel.write(ByteBuffer.wrap(msg.getBytes(StandardCharsets.UTF_8)));
//        outputStream.write(msg.getBytes(StandardCharsets.UTF_8));
//        outputStream.flush();
    }

    private void readMessages() {
        try {
/*            StringBuilder sb = new StringBuilder();
            while (true){
                int read = socketChannel.read(buffer);
                if (read == -1){
                    socketChannel.close();
                    return;
                }
                if (read == 0){
                    break;
                }
                buffer.flip();
                while (buffer.hasRemaining()){
                    sb.append((char) buffer.get());
                }
                buffer.clear();
            }*/
            String msg = inputStream.readUTF();
            System.out.println(msg);
            callback.onReceive(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
