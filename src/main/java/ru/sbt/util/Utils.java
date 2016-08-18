package ru.sbt.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Utils {
    public static void zipFile(File currentFile) {
        try {


            FileInputStream fileOutputStream = new FileInputStream(currentFile);
            ZipEntry entry = new ZipEntry(currentFile.getName());
            ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(currentFile.getName().replace(".ser", ".zip")));
            zipOutputStream.putNextEntry(entry);
            byte[] b = new byte[1024];
            int count;
            while ((count = fileOutputStream.read(b)) > 0) {
                zipOutputStream.write(b, 0, count);
            }
            zipOutputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            throw new RuntimeException("Error during compress file", e);
        }
    }


}
