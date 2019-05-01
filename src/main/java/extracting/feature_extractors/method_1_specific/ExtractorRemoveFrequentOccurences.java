package extracting.feature_extractors.method_1_specific;

import data_management.Elements;
import extracting.feature_extractors.Extractor;
import data_management.Article;
import matching_words.word_comparators.WordComparator;
import program_performance.Stopwatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExtractorRemoveFrequentOccurences implements Extractor {
    private float percent;
    public ExtractorRemoveFrequentOccurences(float percent) {
        this.percent = percent;
    }


    public Map<Object, Float> extract(Map<Object, Float> vector, List<Object> elements, List<Object> elementsForTag, String tag) {
        Stopwatch stopwatch = new Stopwatch();
        int value = (int)(elements.size() * percent);
        Map<String, Integer> wordsInDocumentsCount = new HashMap<>();
        for(Object o : vector.keySet()) {
            wordsInDocumentsCount.put((String)o, 0);
        }
        for(Object o : elements) {
            for(String word : wordsInDocumentsCount.keySet()) {
                if(((Article)o).getLemmas().contains(word)) {
                    wordsInDocumentsCount.replace(word, wordsInDocumentsCount.get(word) + 1);
                }
            }
            while(wordsInDocumentsCount.values().remove(value));
        }
        List<Object> toRemove = new ArrayList<>();
        for(String key : wordsInDocumentsCount.keySet()) {
            if(!wordsInDocumentsCount.keySet().contains(key)) {
                toRemove.add(key);
            }
        }
        vector.keySet().removeAll(toRemove);
        System.out.println("ExtractorRemoveFrequentOccuring: " + stopwatch.getTime());
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
