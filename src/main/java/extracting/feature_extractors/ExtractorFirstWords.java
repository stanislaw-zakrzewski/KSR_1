package extracting.feature_extractors;

import extracting.Converter;
import parsing.Article;

import java.util.List;
import java.util.Map;

public class ExtractorFirstWords implements Extractor {
    private Converter converter;

    public ExtractorFirstWords() {
        converter = new Converter();
    }

    @Override
    public Map<Object, Float> extract(Map<Object, Float> vector, List<Object> elements, List<Object> elementsForTag, String tag) {
        for(Object article : elementsForTag) {
            List<String> words = converter.contentToWords(((Article)article).getContent());
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
