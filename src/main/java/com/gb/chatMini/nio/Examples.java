package com.gb.chatMini.nio;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

import static java.nio.file.StandardWatchEventKinds.*;

public class Examples {
    public static void main(String[] args) throws IOException {

        Path path = Paths.get("server");

//        Path path1 = path.resolve("Скрин.jpg");

        wotcher(path); // - прикольная фишка, позволяет отслеживать изменения в папках

        // create - dir, file
        // Files.createFile()
        // Files.createDirectory()

        // read
//        Files.readAllBytes();
//        Files.readAllLines();
//        Files.newInputStream();
//        Files.newBufferedReader();

        // write
//        Files.write();
//        Files.newBufferedWriter();

//        Files.list() - список всех файлов и директорий в папке


    }

    private static void wotcher(Path path) throws IOException {
        WatchService watchService = FileSystems.getDefault().newWatchService();
        new Thread(() -> {
            try{
                while(true){
                    WatchKey watchKey = watchService.take();
                    System.out.println("New key");
                    List<WatchEvent<?>> events = watchKey.pollEvents();
                    System.out.println("New events");
                    for (WatchEvent<?> event : events){
                        System.out.println(event.kind() + " " + event.context());
                    }
                    watchKey.reset();
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }).start();

        path.register(watchService, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
    }
}
