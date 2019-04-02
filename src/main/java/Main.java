import extracting.MainExtractor;
import extracting.feature_extractors.Extractor;
import parsing.Article;
import parsing.ReadAll;

import java.util.*;

public class Main {
    public static void main(String[] args) {

        //Parametry do klasyfikacji (1/3)
        List<String> tags = Arrays.asList("west-germany", "usa", "france", "uk", "canada", "japan");

        ReadAll readAll = new ReadAll();
        List<Object> articles = new ArrayList<>();// readAll.readAll("src/main/resources/sgm/", "PLACES");

        Map<String, List<Object>> articlesByTags = new HashMap<>();

        tags.forEach(tag -> articlesByTags.put(tag, new ArrayList<>()));
        articles.forEach(article -> {
                    List<String> tagsForArticle = ((Article) article).getTags();
                    if (tagsForArticle.size() == 1) {
                        if (tags.contains(tagsForArticle.get(0))) {
                            articlesByTags.get(tagsForArticle.get(0)).add(article);
                        }
                    }
                }
        );

        List<Extractor> extractors = new ArrayList<>();

        //ReadAll readAll = new ReadAll();
        //List<Article> arts = readAll.readAll("src/main/resources/sgm/", "PLACES");


        List<Object> vector = MainExtractor.createVector(articles, articlesByTags, tags, 2, extractors);

        //TODO After reding all the articles we should filter them

        for (Object word : vector) {
            System.out.println((String) word);
        }
    }
}
