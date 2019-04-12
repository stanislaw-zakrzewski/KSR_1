package extracting;

import extracting.feature_extractors.StanfordLemmatizer;
import parsing.Article;

import java.text.BreakIterator;
import java.util.*;
import java.util.stream.Collectors;

//TODO Fix this garbage
public class Converter {
    public Map<Object, Float> articlesToVector(List<Object> articles) {
        HashSet<String> words = new HashSet<>();
        for(Object article : articles) {
            words.addAll(((Article)article).getLemmatizedWords());
        }
        Map<Object, Float> vector = new HashMap<>();
        for(String word : words) {
            vector.put(word, 0.f);
        }
        return vector;
    }
}
