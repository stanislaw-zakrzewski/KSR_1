package data_management;

import java.io.*;
import java.util.List;

public class WriteObjects {
    public static void write(List<Object> objects, String pathName) {
        File f = new File(pathName);
        try {
            FileOutputStream fos = new FileOutputStream(f);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(objects);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
