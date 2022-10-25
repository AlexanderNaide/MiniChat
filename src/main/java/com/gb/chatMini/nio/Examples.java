package com.gb.chatMini.nio;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.stream.Collectors;

import static java.nio.file.StandardWatchEventKinds.*;

public class Examples {
    public static void main(String[] args) throws IOException {

        Path path = Paths.get("server/2/../1.txt");
        System.out.println(path);
        System.out.println(path.getFileName());
        System.out.println(path.normalize());
        System.out.println(path.getName(0));
        System.out.println(path.getName(1));
        System.out.println(path.getName(2));
        System.out.println(path.getName(3));
        System.out.println(path.getNameCount());
        System.out.println(path.getName(path.getNameCount() - 1));
        System.out.println(path.getRoot());
        System.out.println(path);

        Path pathB = Paths.get(path.getName(0) + "/2.txt");
//        Files.copy(path, pathB); // ������� ����������� � NIO ��/� ����/�����
        Files.copy(path, pathB, StandardCopyOption.REPLACE_EXISTING); // StandardCopyOption - ����� �����������, REPLACE_EXISTING - � ����� ������, ���� ���� ��� ����������, NIO ������ ����������� ���� � �� ������� Exception.
//        Files.move(Paths.get("server/2.txt"), Paths.get("server/3.txt")); // � ����������� � ��������������
        Files.move(Paths.get("server/2.txt"), Paths.get("server/3.txt"), StandardCopyOption.REPLACE_EXISTING); // (� ����������� � ��������������) �� ��, �� � ������ �����������������

        // ����� ��������������� ������ ��������
        Files.walkFileTree(Paths.get("server"), new FileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }
        });



        // ����� ������ ����� ����������� �������� ��� ������ ��������������� ������ ��������
        Files.walkFileTree(Paths.get("server"), new FileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (file.getFileName().toString().equals("3.txt")){ // file - ����� �������� ���� �� ������� ������� � ������ ������
                    System.out.println("���� <3.txt> ������.");
                    return FileVisitResult.TERMINATE; // ��������� �����
                }
                return FileVisitResult.CONTINUE; // ���������� �����
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }
        });

        // ����������� ����� ����� ����������� ��� �������� ��������, �.�. Java �� ������ �������, ���� ��� �� ����� ������
        Files.createDirectory(Paths.get("server/2"));
        Path copy = Paths.get("server/2/1.txt");
        Files.copy(Paths.get("server/1.txt"), copy, StandardCopyOption.REPLACE_EXISTING);
        ;
        Files.walkFileTree(Paths.get("server/2"), new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });


//        Files.list(Paths.get("server")); // �� ���������� ���� �������� ����� ������, ������� ��� �����
        List<Path> pathList = Files.list(Paths.get("server")).toList();
        System.out.println(pathList);

        List<String> data = Files.lines(Paths.get("server/1.txt")).toList(); // lines - ���������� ����� ����� �� �����, toList - �������� ����� ����� � ������ �����
        List<String> data2 = Files.lines(Paths.get("server/1.txt"), StandardCharsets.UTF_8).toList(); // lines - ���������� ����� ����� �� �����, toList - �������� ����� ����� � ������ ����� // ���� � ����������
        System.out.println(data);

        byte[] dataByte = Files.readAllBytes(Paths.get("server/�����.jpg")); // - ������ ����� � ���� ������
        Files.write(Paths.get("server/copySkrin.jpg"), dataByte, StandardOpenOption.CREATE); // ������ ������ � ���� ����� ������� (����� ���������� �� �������� �������)
                                                                                      // CREATE - ��������������
                                                                                      // CREATE_NEW - ������ ��������� �����
                                                                                      // APPEND - ���������� ������ � ����������� �����
                                                                                      // � ��.


        RandomAccessFile srcFile = new RandomAccessFile("server/1.txt", "rw");
        FileChannel srcChannel = srcFile.getChannel();

        RandomAccessFile distFile = new RandomAccessFile("server/5.txt", "rw");
        FileChannel distChannel = distFile.getChannel();

        long position = 0;
        long count = srcChannel.size();

        distChannel.transferFrom(srcChannel, position, count); // (�������, ����� ����������� ����� �������������� ��������� ������ )��������� ������ � ���� �� ������. ������ srcChannel ����� ����� Socket � ������ ���������� ������ �� ���� � ����
        srcChannel.transferTo(position, count, distChannel); // (�������, ����� ��������� ������ �������������� ������������ ����� - ��������)
        srcChannel.close();
        distChannel.close();
        srcFile.close();
        distFile.close();
        // �� �������� ����������� �������


//        FileChannel - ��� ������ � ������� (������ �����/������ �������)
//        DatagramChannel - ������� �������� �� UDP
//        SocketChannel - ������ Socket-� (�������� TCP/IP)
//        ServerSocketChannel - ������ ServerSocket-�

        /**
         * ���� ������ ��� �� ������� ����������� ���� ����� ����� � NIO
         */
        RandomAccessFile raf = new RandomAccessFile("server/5.txt", "rw");
        FileChannel inChannel = raf.getChannel();
        ByteBuffer buf = ByteBuffer.allocate(8);
        int bytesRead = inChannel.read(buf); // ������ � ������ ������
        while (bytesRead != -1){
            buf.flip(); // ������ ��������� � ����� ������
            while (buf.hasRemaining()){ // "�� ��� ���, ���� � ������� �������� ���-�� �������������"
                System.out.print((char) buf.get());
            }
            buf.clear(); // ������� ������
            bytesRead = inChannel.read(buf); // ����������� ��������� ������ ������
        }
        inChannel.close();
        raf.close();

        System.out.println();
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.put((byte) 10);
        buffer.put((byte) 20);
        buffer.flip();
        System.out.println(buffer.get());
        System.out.println(buffer.get());
        buffer.flip();
        buffer.put((byte) 30);
        buffer.flip();
        System.out.println(buffer.get());








//        Path path1 = path.resolve("�����.jpg");

//        wotcher(path); // - ���������� �����, ��������� ����������� ��������� � ������

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

//        Files.list() - ������ ���� ������ � ���������� � �����


    }

    private static void wotcher(Path path) throws IOException { // - ���������� �����, ��������� ����������� ��������� � ������
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
