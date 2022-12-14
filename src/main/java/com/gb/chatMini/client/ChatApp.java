package com.gb.chatMini.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class ChatApp extends Application {



    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("chatMini.fxml")));
        primaryStage.setScene(new Scene(parent));
        primaryStage.setTitle("MiniChat");
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
