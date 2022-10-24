package com.gb.chatMini.nio;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class ConsoleController implements Initializable {
    public ListView<String> listView;
    public TextField input;
    private byte[] buffer;
    private Path clienDir;

    private NIONet net;

    public void sendMsg(ActionEvent actionEvent) throws IOException {
        String inputMsg = input.getText();
        input.clear();
        net.sendMsg(inputMsg);
        addMessage("<<<<< " + inputMsg);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            buffer = new byte[8192];
            clienDir = Paths.get("client");
//            Socket socket = new Socket("localhost", 6830);
            net = new NIONet(this::addMessage, 6830);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private void addMessage(String message) {
        Platform.runLater(() -> {
            listView.getItems().add(message);
        });
    }
}
