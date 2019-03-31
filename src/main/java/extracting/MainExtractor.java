package extracting;

import extracting.feature_extractors.ExtractorFirstWords;
import extracting.feature_extractors.ExtractorRemoveStopWords;
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

        ExtractorRemoveStopWords extractorRemoveStopWords = new ExtractorRemoveStopWords();
        ExtractorFirstWords extractorFirstWords = new ExtractorFirstWords();

        for(String tag : tags) {
            System.out.println(tag);
            Map<String, Float> vectorPart = Converter.articlesToVector(articlesByTags.get(tag));
            //Remove StopWords
            vectorPart = extractorRemoveStopWords.extract(vectorPart, articles);
            //First words
            vectorPart = extractorFirstWords.extract(vectorPart, articles);
            for(String element : NElementsSelector.selectN(vectorPart, numberOfElementsPerTag)) {
                if(!vector.contains(element)) {
                    vector.add(element);
                }
            }
        }
        System.out.println();

        return vector;
    }
}
