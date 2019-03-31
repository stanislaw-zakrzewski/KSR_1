package extracting.feature_extractors;

import extracting.Converter;
import parsing.Article;

import java.util.List;
import java.util.Map;

public class ExtractorFirstWords implements Extractor {

    @Override
    public Map<String, Float> extract(Map<String, Float> vector, List<Article> articles) {
        for(Article article : articles) {
            List<String> words = Converter.contentToWords(article.getContent());
            for (String word : words) {
                if (vector.containsKey(word)) {
                    vector.replace(word, vector.get(word) + 10);
                    break;
                }
            }
        }
        return vector;
    }
}
