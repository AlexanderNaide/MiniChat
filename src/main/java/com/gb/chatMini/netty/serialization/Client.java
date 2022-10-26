package com.gb.chatMini.netty.serialization;

import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;

import java.io.IOException;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        ObjectEncoderOutputStream oeos = null;
        ObjectDecoderInputStream odis = null;
        try (Socket socket = new Socket("localhost", 6830)){
            oeos = new ObjectEncoderOutputStream(socket.getOutputStream());
            MyMessage textMessage = new MyMessage("Hello Server!!!");
            oeos.writeObject(textMessage);
            oeos.flush();
            odis = new ObjectDecoderInputStream(socket.getInputStream());
            MyMessage msgFromServer = (MyMessage) odis.readObject();
            System.out.println("Answer from server: " + msgFromServer.getText());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try{
                oeos.close();
            } catch (IOException e){
                e.printStackTrace();
            }
            try {
                odis.close();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
