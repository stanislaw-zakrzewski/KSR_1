package data_management;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ReadObjects {
    public static List<Object> read(String path) {
        File f = new File(path);
        List<Object> objects = new ArrayList<>();
        try {
            FileInputStream fis = new FileInputStream(f);
            ObjectInputStream ois = new ObjectInputStream(fis);
            objects = (List<Object>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return objects;
    }
}
