package com.gb.copyPaste;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class Example {
    public static void main(String[] args) {
        File file = new File("files/11.JPG");
        byte[] buffer = new byte[8192];
        try (FileInputStream is = new FileInputStream(file)){
            int read;
            try(FileOutputStream os = new FileOutputStream("files/copy.jpg")){
                while ((read = is.read(buffer)) != -1){
                    os.write(buffer, 0, read);
                }
            } catch (Exception e){
                e.printStackTrace();
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
