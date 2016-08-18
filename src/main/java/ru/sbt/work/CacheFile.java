package ru.sbt.work;


import ru.sbt.outputstream.AppendingObjectOutputStream;
import ru.sbt.util.Utils;

import java.io.*;
import java.util.List;

public class CacheFile implements CacheWork {

    private static final String SUFFIX_SER = ".ser";
    private final String prefix;
    private final boolean zip;
    File currentFile;


    public CacheFile(String s, boolean zip) throws IOException {
        this.prefix = s;
        this.zip = zip;
        currentFile = new File(prefix + SUFFIX_SER);

    }

    @Override
    public void write(List<Object> key, Object obj) {
        ObjectDto objectDto = new ObjectDto(key, obj);
        try (ObjectOutputStream objectOutputStream = getOutputStream()) {
            objectOutputStream.writeObject(objectDto);
            objectOutputStream.flush();
            objectOutputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error during serialization, change file");
        }

        if (zip) {
            Utils.zipFile(currentFile);
        }

    }


    @Override
    public Object read(List<Object> key) {


        if (currentFile.length() == 0) {
            return null;
        }
        try (ObjectInputStream objectInputStream = getInputStream()) {
            while (true) {
                ObjectDto objectDto = (ObjectDto) objectInputStream.readObject();
                if (objectDto.key.equals(key)) {
                    return objectDto.object;
                }
            }

        } catch (EOFException e) {
            //end file
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error during deserialization, change file");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Return class was removed");
        }

    }

    private ObjectOutputStream getOutputStream() throws IOException {
        ObjectOutputStream objectOutputStream;

        if (currentFile.exists()) {
            objectOutputStream = new AppendingObjectOutputStream(new FileOutputStream(currentFile, true));
        } else {
            objectOutputStream = new ObjectOutputStream(new FileOutputStream(currentFile));
        }
        return objectOutputStream;
    }

    private ObjectInputStream getInputStream() throws IOException {


        return new ObjectInputStream(new FileInputStream(currentFile));


    }

}
