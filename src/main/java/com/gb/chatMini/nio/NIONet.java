package com.gb.chatMini.nio;

import com.gb.chatMini.client.Callback;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

public class NIONet {

    private final Callback callback;
    private int port;
    private final SocketChannel socketChannel;
    private ByteBuffer bufferInput;
    private ByteBuffer bufferOutput;

    public NIONet(Callback callback, int port) throws IOException {
        this.port = port;
        InetAddress address = InetAddress.getLocalHost();
        InetSocketAddress socketAddress = new InetSocketAddress(address, port);
        socketChannel = SocketChannel.open(socketAddress);
        bufferInput = ByteBuffer.allocate(8192);
        bufferOutput = ByteBuffer.allocate(8192);
        this.callback = callback;
        Thread readThread = new Thread(this::readMessages);
        readThread.setDaemon(true);
        readThread.start();
    }




    private void close(SocketChannel channel){
        try {
            channel.close();
        } catch (Exception e){
            System.out.println("Server disconnected...");
            e.printStackTrace();
        }
    }

    public void sendMsg(String msg) throws IOException {
        bufferOutput.put(msg.getBytes(StandardCharsets.UTF_8));
        bufferOutput.flip();
        socketChannel.write(bufferOutput);
        bufferOutput.clear();
    }

    private void readMessages() {
        try {
            while (true) {
                socketChannel.read(bufferInput);
                bufferInput.flip();
                String data = StandardCharsets.UTF_8.decode(bufferInput).toString();
                bufferInput.clear();
                callback.onReceive(">>>>> " + data);
                System.out.println(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
