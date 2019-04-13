package extracting.feature_extractors.method_1_specific;

import extracting.feature_extractors.Extractor;
import data_management.Article;
import program_performance.Stopwatch;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExtractorRemoveRarelyOccuring implements Extractor {
    private int wordsCount = 0;

    public ExtractorRemoveRarelyOccuring(int wordsCount) {
        this.wordsCount = wordsCount;
    }

    @Override
    public Map<Object, Float> extract(Map<Object, Float> vector, List<Object> elements, List<Object> elementsForTag, String tag) {
        Stopwatch stopwatch = new Stopwatch();
        Map<String, Integer> wordsInDocumentsCount = new HashMap<>();
        for(Object o : vector.keySet()) {
            wordsInDocumentsCount.put((String)o, 0);
        }
        for(Object o : elementsForTag) {
            for(String word : wordsInDocumentsCount.keySet()) {
                if(((Article)o).getLemmatizedWords().contains(word)) {
                    wordsInDocumentsCount.replace(word, wordsInDocumentsCount.get(word) + 1);
                }
            }
            while(wordsInDocumentsCount.values().remove(wordsCount-1));
        }
        for(String key : wordsInDocumentsCount.keySet()) {
            if(wordsInDocumentsCount.get(key) < wordsCount) {
                vector.remove(key);
            }
        }
        System.out.println("ExtractorRemoveRarelyOccuring: " + stopwatch.getTime());
        return vector;
    }
}
