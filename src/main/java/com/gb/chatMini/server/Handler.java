package com.gb.chatMini.server;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class Handler implements Runnable{
    private boolean running;
    private final byte[] buffer;
    private final InputStream inputStream;
    private final OutputStream outputStream;
    private final Socket socket;

    public Handler(Socket socket) throws IOException {
        this.running = true;
        this.socket = socket;
        buffer = new byte[8192];
        inputStream = socket.getInputStream();
        outputStream = socket.getOutputStream();
    }

    @Override
    public void run() {
        try {
            while (running){
                int read = inputStream.read(buffer);
                String message = new String(buffer, 0, read).trim();
                if (message.equals("%f%")){
                    Thread wf = new Thread(new writeFile());
                    wf.start();
                    wf.join();
                    System.out.println("The file is saved.");
                    outputStream.write(("The file is saved." + "\n").getBytes(StandardCharsets.UTF_8));
                } else {
                    System.out.println("Received: " + message);
                    outputStream.write((message + "\n").getBytes(StandardCharsets.UTF_8));
                }
                if (message.equals("quit")){
                    outputStream.write(("Client disconnected!\n").getBytes(StandardCharsets.UTF_8));
                    close();
                    System.out.println("Client disconnected...");
                    break;
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void close() throws IOException {
        outputStream.close();
        inputStream.close();
        socket.close();
    }

    private class writeFile implements Runnable{
        @Override
        public void run() {

            try {
                int to = inputStream.read(buffer);
                System.out.println("\n\nbuffer1" + Arrays.toString(buffer));
                String line = new String(Arrays.copyOfRange(buffer, 0, to), StandardCharsets.UTF_8);
                System.out.println("line - " + line);
                int in = line.indexOf("%n%");
                String name = line.substring(0, in);
                System.out.println("name - " + name);
                File file = new File("files/" + name);
                FileOutputStream fos = new FileOutputStream(file);
                in += 3;
                String newLine = line.substring(in);
                System.out.println("line2 - " + newLine);
                System.out.println("\n\nbuffer2" + Arrays.toString(buffer));
                byte[] newBuffer = Arrays.copyOfRange(buffer, 0, in);
                System.out.println("\n\nbuffer3" + Arrays.toString(newBuffer));
                System.out.println("name3 - " + new String(newBuffer, 0, newBuffer.length));
                System.out.println("name2 - " + new String(buffer, 0, in));

//                System.out.println("новая строка - " + (line.getBytes(StandardCharsets.UTF_8, in, line.length())));
                fos.write(line.getBytes(StandardCharsets.UTF_8), in, line.length());


                do {
                    to = inputStream.read(buffer);
                    fos.write(buffer, 0, to);
//                    in = 0;
                } while (to == buffer.length);
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }



//            File file = new File("files/copy2.jpg");
/*            try(FileOutputStream fos = new FileOutputStream(file)){
                do {
                    or = inputStream.read(buffer);
                    fos.write(buffer, 0, or);
                    System.out.println( "\n тут -> " + new String(buffer, 0, or));
                } while (or >= buffer.length);
            } catch (Exception e){
                e.printStackTrace();
            }*/
        }
    }
}
