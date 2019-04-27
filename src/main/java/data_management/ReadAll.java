package data_management;

import data_management.Read;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReadAll {
    public static List<Object> read(String folderPath, String tag, int articlesToReadCount) {
        List<Object> articles = new ArrayList<>();
        try (Stream<Path> walk = Files.walk(Paths.get(folderPath))) {
            List<String> listOfFiles = walk.filter(Files::isRegularFile).map(Path::toString).collect(Collectors.toList());
            if (articlesToReadCount > listOfFiles.size()) {
                articlesToReadCount = listOfFiles.size();
            }
            for (int i = 0; i < articlesToReadCount; i++) {
                System.out.println(i);
                articles.addAll(Read.readTag(listOfFiles.get(i), tag));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return articles;
    }
}
