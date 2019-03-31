package extracting;

import parsing.Article;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MainExtractor {
    public static List<String> createVector(List<Article> articles, int numberOfElementsPerTag) {
        List<String> tags = new ArrayList<>();
        for (Article article : articles) {
            for (String tag : article.getTags()) {
                if (!tags.contains(tag)) {
                    tags.add(tag);
                }
            }
        }

        Map<String, List<Article>> articlesByTags = new HashMap<>();
        for(String tag : tags) {
            articlesByTags.put(tag, articles.stream().filter(a -> a.getTags().contains(tag)).collect(Collectors.toList()));
        }

        List<String> vector = new ArrayList<>();

        for(String tag : tags) {
            Map<String, Float> vectorPart = Convert.articlesToVector(articlesByTags.get(tag));
            //TODO feature extraction
            for(String element : NElementsSelector.selectN(vectorPart, numberOfElementsPerTag)) {
                if(!vector.contains(element)) {
                    vector.add(element);
                }
            }
        }

        return vector;
    }
}
