package extracting;

import extracting.feature_extractors.ExtractorFirstWords;
import extracting.feature_extractors.ExtractorRemoveFrequentOccurances;
import extracting.feature_extractors.ExtractorRemoveRarelyOccurringWords;
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
        ExtractorRemoveFrequentOccurances extractorRemoveFrequentOccurances = new ExtractorRemoveFrequentOccurances();
        ExtractorRemoveRarelyOccurringWords extractorRemoveRarelyOccurringWords = new ExtractorRemoveRarelyOccurringWords();

        for(String tag : tags) {
            System.out.println("-----------------" + tag + articlesByTags.get(tag).size());
            //System.out.println(tag + "\t\t" + articlesByTags.get(tag).size());
            Map<String, Float> vectorPart = Converter.articlesToVector(articlesByTags.get(tag));
            //Remove StopWords
            vectorPart = extractorRemoveStopWords.extract(vectorPart, articlesByTags.get(tag));
            //First words
            vectorPart = extractorFirstWords.extract(vectorPart, articlesByTags.get(tag));
            //TODO Remove frequent occurances
            //vectorPart = extractorRemoveFrequentOccurances.extract(vectorPart, articlesByTags.get(tag));
            //Remove rarely occurring words USE ARTICLES INSTEAD OF ARTICLES BY TAGS
            vectorPart = extractorRemoveRarelyOccurringWords.extract(vectorPart, articles);
            for(String element : NElementsSelector.selectN(vectorPart, numberOfElementsPerTag)) {
                System.out.println(tag + "\t\t" + element);
                if(!vector.contains(element)) {
                    vector.add(element);
                }
            }
        }
        System.out.println();

        return vector;
    }
}
