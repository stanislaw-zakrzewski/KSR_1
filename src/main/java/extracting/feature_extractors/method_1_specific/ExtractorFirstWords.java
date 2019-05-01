package extracting.feature_extractors.method_1_specific;

import data_management.Elements;
import extracting.feature_extractors.Extractor;
import data_management.Article;
import extracting.feature_extractors.Stopwords;
import matching_words.word_comparators.WordComparator;
import program_performance.Stopwatch;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExtractorFirstWords implements Extractor {
    private int firstWordsCount;

    public ExtractorFirstWords(int firstWordsCount) {
        this.firstWordsCount = firstWordsCount;
    }

    public Map<Object, Float> extract(Map<Object, Float> vector, List<Object> elements, List<Object> elementsForTag, String tag) {
        Stopwatch stopwatch = new Stopwatch();



        int wordsCounter = 1;
        for (Object article : elementsForTag) {
            for (String word : ((Article) article).getLemmas()) {
                if (vector.containsKey(word)) {
                    vector.replace(word, vector.get(word) * (1 + (1.0f / firstWordsCount) / 10));
                    if (wordsCounter == firstWordsCount) {
                        break;
                    } else {
                        wordsCounter++;
                    }
                }
            }
        }

        System.out.println("ExtractorFirstWords: " + stopwatch.getTime());
        return vector;
    }

    @Override
    public List<Object> extract(Elements elements) {
        return null;
    }

    @Override
    public List<Float> getValues(List<Object> vector, Object element, WordComparator wordComparator) {
        return null;
    }
}
