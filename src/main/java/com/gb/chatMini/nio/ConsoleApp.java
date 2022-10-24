package com.gb.chatMini.nio;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class ConsoleApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("console.fxml")));
        primaryStage.setScene(new Scene(parent));
        primaryStage.setTitle("Telnet console");
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
