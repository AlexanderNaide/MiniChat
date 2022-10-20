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
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;

public class ChatController implements Initializable {

    private Path clienDir;
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

    private void initClickListener(){
        listView.setOnMouseClicked(event -> {
            if(event.getClickCount() == 2){
                String item = (String.valueOf(listView.getSelectionModel().getSelectedItems()));
                input.setText(item);
            }
        });
    }


    private void fillFileView() throws IOException {
        List<String> files = Files.list(clienDir)
                .map(p -> p.getFileName().toString())
                .toList();
        listView.getItems().addAll(files);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            clienDir = Paths.get("client");
            fillFileView();
            initClickListener();
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
