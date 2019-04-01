import extracting.MainExtractor;
import parsing.Article;
import parsing.ReadAll;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        ReadAll readAll = new ReadAll();
        List<Article> arts = readAll.readAll("src/main/resources/sgm/", "PLACES");

        List<String> vector = MainExtractor.createVector(arts, 3);
        for (String word : vector) {
            System.out.println(word);
        }
    }
}
