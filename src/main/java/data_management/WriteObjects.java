package data_management;

import java.io.*;
import java.util.List;

public class WriteObjects {
    public static void write(List<Object> objects) {
        File f = new File("src/main/resources/extracted/reuters/01.txt");
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
