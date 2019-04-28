package data_management;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReadAllObjects {
    public static List<Object> read(String folderPath) {
        List<Object> objects = new ArrayList<>();
        try (Stream<Path> walk = Files.walk(Paths.get(folderPath))) {
            List<String> listOfFiles = walk.filter(Files::isRegularFile).map(Path::toString).collect(Collectors.toList());
            for (String file : listOfFiles) {
                objects.addAll(ReadObjects.read(file));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return objects;
    }
}
