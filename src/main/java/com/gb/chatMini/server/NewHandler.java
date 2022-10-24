package com.gb.chatMini.server;

import java.io.*;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.charset.StandardCharsets.UTF_8;

public class NewHandler implements Runnable {

    private static final int SIZE = 8192;
    private Path serverDir;
    private boolean running;
    private final byte[] buffer;
    private final DataInputStream inputStream;
    private final DataOutputStream outputStream;
    private final Socket socket;

    public NewHandler(Socket socket) throws IOException {
        this.running = true;
        this.socket = socket;
        buffer = new byte[SIZE];
        serverDir = Paths.get("server");
        inputStream = new DataInputStream(socket.getInputStream());
        outputStream = new DataOutputStream(socket.getOutputStream());
    }

    @Override
    public void run() {
        try {
            while (running) {

                String command = inputStream.readUTF();
                if(command.equals("quit")){
                    outputStream.writeUTF("Client disconnected!");
                    close();
                    System.out.println("Client disconnected...");
                    break;
                } else if (command.equals("#file#")) {
                    String fileName = inputStream.readUTF();
                    long size = inputStream.readLong();
                    try(FileOutputStream fos = new FileOutputStream(
                            serverDir.resolve(fileName).toFile())) {
                        outputStream.writeUTF("File " + fileName + " created");
                        for (int i = 0; i < (size + SIZE - 1) / SIZE; i++) {
                            int read = inputStream.read(buffer);
                            fos.write(buffer, 0, read);
                            outputStream.writeUTF("Uploaded " + (i + 1) + " batch");
                        }
                    }
                    outputStream.writeUTF("File successfully uploaded.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void close() throws IOException {
        outputStream.close();
        inputStream.close();
        socket.close();
    }

    private class writeFile implements Runnable {
        @Override
        public void run() {
            try {
                String name;
                BufferedOutputStream bos;
                int r;
                int nameLength;
                long fileLight;
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < 4; i++) {
                    r = inputStream.read();
                    sb.append((char) r);
                }
                nameLength = Integer.parseInt(sb.toString());
                sb.setLength(0);
                for (int i = 0; i < 20; i++) {
                    r = inputStream.read();
                    sb.append((char) r);
                }
                fileLight = Long.parseLong(sb.toString());
                sb.setLength(0);

                byte[] byteName = new byte[nameLength];
                for (int i = 0; i < nameLength; i++) {
                    r = inputStream.read();
                    byteName[i] = (byte) r;
                }
                name = new String(byteName, UTF_8);
                File file = new File("files/" + name);
                bos = new BufferedOutputStream(new FileOutputStream(file));

                long readFile = 0;

                while (readFile < fileLight) {
                    int read = inputStream.read(buffer);
                    bos.write(buffer, 0, read);
                    readFile += read;
                }
                bos.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}