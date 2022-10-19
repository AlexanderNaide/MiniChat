package com.gb.chatMini.client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class ChatController implements Initializable {
    public ListView<String> listView;
    public TextField input;
    private IONet net;

    public void sendMsg(ActionEvent actionEvent) throws IOException {
        String msg = input.getText();
        input.clear();
        if(msg.length() > 4){
            String prefix = msg.substring(0,4).toLowerCase();
            if (prefix.equals("file")) {
                sendFile(msg);
            } else {
                sendText(msg);
            }
        } else {
            sendText(msg);
        }
    }

    private void addMessage(String message) {
        Platform.runLater(() -> {
            listView.getItems().add(message);
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            Socket socket = new Socket("localhost", 6830);
            net = new IONet(this::addMessage, socket);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void sendText(String msg) throws IOException {
        net.sendMsg(msg);
    }

    public void sendFile(String msg) throws IOException {
        File file = new File(msg.substring(5));
        net.sendFile(file);
    }
}
