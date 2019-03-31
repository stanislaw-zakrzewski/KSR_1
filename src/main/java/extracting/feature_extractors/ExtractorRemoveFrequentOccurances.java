package extracting.feature_extractors;

import extracting.Converter;
import parsing.Article;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExtractorRemoveFrequentOccurances implements Extractor {
    @Override
    public Map<String, Float> extract(Map<String, Float> vector, List<Article> articles) {
        Map<String, Integer> occurances = new HashMap<>();
        for(Article article : articles) {
            List<String> words = Converter.contentToWords(article.getContent());
            for(String word : words) {
                if(vector.containsKey(word)) {
                    if (occurances.containsKey(word)) {
                        occurances.replace(word, occurances.get(word) + 1);
                    } else {
                        occurances.put(word, 1);
                    }
                }
            }
        }
        for(String word : occurances.keySet()) {
            if(occurances.get(word) > articles.size()/2 && articles.size() > 30) {
                System.out.println(word + "\t\t" + occurances.get(word));
            }
        }
        return vector;
    }
}
