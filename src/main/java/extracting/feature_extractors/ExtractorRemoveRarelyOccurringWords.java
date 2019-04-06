package extracting.feature_extractors;

import extracting.Converter;
import parsing.Article;

import java.util.*;

public class ExtractorRemoveRarelyOccurringWords implements Extractor {
    private Converter converter;

    public ExtractorRemoveRarelyOccurringWords() {
        converter = new Converter();
    }
    /***
     *
     * @param vector - used
     * @param elements - used
     * @param elementsForTag - unused in this case
     * @param tag - unused in this case
     * @return updated vector with removed rarely occurring elements
     */
    public Map<Object, Float> extract(Map<Object, Float> vector, List<Object> elements, List<Object> elementsForTag, String tag) {
        Map<String, Integer> occurrances = new HashMap<>();
        for(Object article : elements) {
            List<String> words = new ArrayList<>(new HashSet<>(converter.contentToWords(((Article)article).getContent())));
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
