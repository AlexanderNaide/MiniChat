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
//        Files.copy(path, pathB); // Простое копирование в NIO из/в Файл/Поток
        Files.copy(path, pathB, StandardCopyOption.REPLACE_EXISTING); // StandardCopyOption - опции копирования, REPLACE_EXISTING - С таким флагом, если файл уже существует, NIO просто перезапишет файл и не выкинет Exception.
//        Files.move(Paths.get("server/2.txt"), Paths.get("server/3.txt")); // И перемещение и переименование
        Files.move(Paths.get("server/2.txt"), Paths.get("server/3.txt"), StandardCopyOption.REPLACE_EXISTING); // (И перемещение и переименование) То же, но с опцией автопереписывания

        // Метод автоматического обхода каталога
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



        // Метод поиска файла стандатными методами при помощи автоматического обхода каталога
        Files.walkFileTree(Paths.get("server"), new FileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (file.getFileName().toString().equals("3.txt")){ // file - метод получает файл на который смотрит в данный момент
                    System.out.println("Файл <3.txt> найден.");
                    return FileVisitResult.TERMINATE; // Прерывает обход
                }
                return FileVisitResult.CONTINUE; // Продолжает обход
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

        // Рекурсивный обход может пригодиться при удалении каталога, т.к. Java не удалит каталог, пока тот не будет очищен
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


//        Files.list(Paths.get("server")); // из указанного пути получаем поток файлов, которые там лежат
        List<Path> pathList = Files.list(Paths.get("server")).toList();
        System.out.println(pathList);

        List<String> data = Files.lines(Paths.get("server/1.txt")).toList(); // lines - возвращает поток строк из файла, toList - собирает поток строк в список строк
        List<String> data2 = Files.lines(Paths.get("server/1.txt"), StandardCharsets.UTF_8).toList(); // lines - возвращает поток строк из файла, toList - собирает поток строк в список строк // тоже с кодировкой
        System.out.println(data);

        byte[] dataByte = Files.readAllBytes(Paths.get("server/Скрин.jpg")); // - чтение файла в одну строку
        Files.write(Paths.get("server/copySkrin.jpg"), dataByte, StandardOpenOption.CREATE); // Запись данных в файл одной строкой (можно записывать из входящих потоков)
                                                                                      // CREATE - перезаписывать
                                                                                      // CREATE_NEW - только создавать новый
                                                                                      // APPEND - дописывать данные в продолжение файла
                                                                                      // и др.


        RandomAccessFile srcFile = new RandomAccessFile("server/1.txt", "rw");
        FileChannel srcChannel = srcFile.getChannel();

        RandomAccessFile distFile = new RandomAccessFile("server/5.txt", "rw");
        FileChannel distChannel = distFile.getChannel();

        long position = 0;
        long count = srcChannel.size();

        distChannel.transferFrom(srcChannel, position, count); // (Вариант, когда принимающий канал инициализирует перекачку данных )перекачка данных в файл по каналу. Вместо srcChannel можно взять Socket и просто перекачать данные из сети в файл
        srcChannel.transferTo(position, count, distChannel); // (Вариант, когда перекачку данных инициализирует отправляющий канал - отправка)
        srcChannel.close();
        distChannel.close();
        srcFile.close();
        distFile.close();
        // не забываем освобождать ресурсы


//        FileChannel - для работы с файлами (Замена Инпут/Оутпут стримам)
//        DatagramChannel - Сетевой протокол по UDP
//        SocketChannel - замена Socket-у (протокол TCP/IP)
//        ServerSocketChannel - Замена ServerSocket-у

        /**
         * Ниже пример как по порядку считывается файл через канал в NIO
         */
        RandomAccessFile raf = new RandomAccessFile("server/5.txt", "rw");
        FileChannel inChannel = raf.getChannel();
        ByteBuffer buf = ByteBuffer.allocate(8);
        int bytesRead = inChannel.read(buf); // Буффер в режиме записи
        while (bytesRead != -1){
            buf.flip(); // буффер переходит в режим чтения
            while (buf.hasRemaining()){ // "До тех пор, пока в буффере остается что-то непрочитанное"
                System.out.print((char) buf.get());
            }
            buf.clear(); // очищаем буффер
            bytesRead = inChannel.read(buf); // подтягиваем следующую порцию данных
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








//        Path path1 = path.resolve("Скрин.jpg");

//        wotcher(path); // - прикольная фишка, позволяет отслеживать изменения в папках

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

    private static void wotcher(Path path) throws IOException { // - прикольная фишка, позволяет отслеживать изменения в папках
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
