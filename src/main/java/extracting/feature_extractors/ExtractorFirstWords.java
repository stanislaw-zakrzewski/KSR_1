package extracting.feature_extractors;

import extracting.Converter;
import parsing.Article;
import results.Stopwatch;

import java.util.List;
import java.util.Map;

public class ExtractorFirstWords implements Extractor {

    public ExtractorFirstWords() {
        Converter converter = new Converter();
    }

    @Override
    public Map<Object, Float> extract(Map<Object, Float> vector, List<Object> elements, List<Object> elementsForTag, String tag) {
        Stopwatch stopwatch = new Stopwatch();

        for(Object article : elementsForTag) {
            for (String word : ((Article) article).getLemmatizedWords()) {
                if (vector.containsKey(word)) {
                    vector.replace(word, vector.get(word) + 1);
                    break;
                }
            }
        }

        System.out.println("ExtractorFirstWords: " + stopwatch.getTime());
        return vector;
    }
}
