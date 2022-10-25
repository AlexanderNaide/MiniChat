package com.gb.chatMini.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

public class NIOServer {
    private ServerSocketChannel serverChannel;
    private Selector selector;
    private ByteBuffer buf;

    public NIOServer(int port) throws IOException {
        buf = ByteBuffer.allocate(8192);
        serverChannel = ServerSocketChannel.open();
        selector = Selector.open();
        serverChannel.bind(new InetSocketAddress(port));
        serverChannel.configureBlocking(false);
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (serverChannel.isOpen()){
            selector.select();
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = keys.iterator();
            try {
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    if (key.isAcceptable()) {
                        handleAccept();
                    }
                    if (key.isReadable()) {
                        handleRead(key);
                    }
                    iterator.remove();
                }
            } catch (Exception e){
                e.printStackTrace();
            }

        }

    }

    private void handleAccept() throws IOException {
        System.out.println("Client accepted...");
        SocketChannel socketChannel = serverChannel.accept();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ, "Hello world!");
    }

    private void handleRead(SelectionKey key)  {
        SocketChannel channel = (SocketChannel) key.channel();
//        StringBuilder sb = new StringBuilder();
        String msg = null;
        try {
            while (true){
                int read = channel.read(buf);
                if (read == -1){
                    close(channel);
                    return;
                }
                if (read == 0){
                    break;
                }
                buf.flip();
//                buf.position(2);
                msg = StandardCharsets.UTF_8.decode(buf).toString();

/*                while (buf.hasRemaining()){
                    sb.append((char) buf.get());
                }*/
                buf.clear();
            }

            //        processMessage(channel, sb.toString());
            System.out.println("Client: " + msg);
            // todo processMessage(sb)
//            String response = "Hello " + sb + key.attachment();
//            String response = "Hello " + msg;
            String response = msg;
            channel.write(ByteBuffer.wrap(response.getBytes(StandardCharsets.UTF_8)));
            System.out.println("response: " + response);


        } catch (Exception e){
            System.out.println("Client disconnected...");
            close(channel);
//            e.printStackTrace();
        }


    }

    private void close(SocketChannel channel){
        try {
            channel.close();
        } catch (Exception e){
            System.out.println("Client disconnected...");
            e.printStackTrace();
        }
    }

    private void processMessage(SocketChannel channel, String msg){
        // TODO
    }

    public static void main(String[] args) throws IOException {
        new NIOServer(6830);
    }
}
