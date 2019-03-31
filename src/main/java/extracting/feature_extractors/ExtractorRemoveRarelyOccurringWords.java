package extracting.feature_extractors;

import extracting.Converter;
import parsing.Article;

import java.util.*;

public class ExtractorRemoveRarelyOccurringWords implements Extractor {
    @Override
    public Map<String, Float> extract(Map<String, Float> vector, List<Article> articles) {
        Map<String, Integer> occurrances = new HashMap<>();
        for(Article article : articles) {
            List<String> words = new ArrayList<>(new HashSet<>(Converter.contentToWords(article.getContent())));
            for(String word : words) {
                if(vector.containsKey(word)) {
                    if (occurrances.containsKey(word)) {
                        occurrances.replace(word, occurrances.get(word) + 1);
                    } else {
                        occurrances.put(word, 1);
                    }
                }
            }
        }
        for(String key : occurrances.keySet()) {
            if(occurrances.get(key) < 5) {
                vector.remove(key);
            }
        }
        return vector;
    }
}
