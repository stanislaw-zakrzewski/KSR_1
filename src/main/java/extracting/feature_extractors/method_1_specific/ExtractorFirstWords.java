package extracting.feature_extractors.method_1_specific;

import extracting.feature_extractors.Extractor;
import parsing.Article;
import results.Stopwatch;

import java.util.List;
import java.util.Map;

public class ExtractorFirstWords implements Extractor {
    private int firstWordsCount;

    public ExtractorFirstWords(int firstWordsCount) {
        this.firstWordsCount = firstWordsCount;
    }

    @Override
    public Map<Object, Float> extract(Map<Object, Float> vector, List<Object> elements, List<Object> elementsForTag, String tag) {
        Stopwatch stopwatch = new Stopwatch();

        int wordsCounter = 1;
        for (Object article : elementsForTag) {
            for (String word : ((Article) article).getLemmatizedWords()) {
                if (vector.containsKey(word)) {
                    vector.replace(word, vector.get(word) + 1.0f/firstWordsCount);
                    if(wordsCounter == firstWordsCount) {
                        break;
                    } else {
                        wordsCounter ++;
                    }
                }
            }
        }

        System.out.println("ExtractorFirstWords: " + stopwatch.getTime());
        return vector;
    }
}
