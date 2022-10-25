package com.gb.chatMini.client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ResourceBundle;

public class NewIOChatController implements Initializable {

    public ListView<String> statuses;
    private Path clienDir;
    public ListView<String> listView;
    public TextField input;
    private NewIONet net;
    private byte[] buffer;

    public void sendMsg(ActionEvent actionEvent) throws IOException {
        sendFile(input.getText());
        input.clear();
    }

    private void addStatus(String message) {
        Platform.runLater(() -> {
            statuses.getItems().add(message);
        });


        /*
        Platform.runLater(() -> {
            listView.getItems().add(message);
        });
        */
    }

    private void initClickListener(){
        listView.setOnMouseClicked(event -> {
            if(event.getClickCount() == 2){
                String item = (String.valueOf(listView.getSelectionModel().getSelectedItem()));
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
            buffer = new byte[8192];
            clienDir = Paths.get("client");
            fillFileView();
            initClickListener();
            Socket socket = new Socket("localhost", 6830);
            net = new NewIONet(this::addStatus, socket);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private void sendText(String msg) throws IOException {
        net.sendMsg(msg);
    }

    private void sendFile (String filename) throws IOException {
        Path file = clienDir.resolve(filename);
        net.writeUTF("#file#");
        net.writeUTF(filename.replaceAll(" ", "_"));
        net.writeLong(Files.size(file));
        try(FileInputStream fis = new FileInputStream(file.toFile())){
            int read = 0;

            while ((read = fis.read(buffer)) != -1){
                net.writeBytes(buffer, 0, read);
            }
        }
    }
}
