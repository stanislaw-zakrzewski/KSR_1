package extracting.feature_extractors;

import extracting.Converter;
import parsing.Article;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExtractorRemoveFrequentOccurances implements Extractor {

    /***
     *
     * @param vector - used
     * @param elements - unused in this case
     * @param elementsForTag - used
     * @param tag - unused in this case
     * @return vector with removed frequent occurances
     */
    public Map<Object, Float> extract(Map<Object, Float> vector, List<Object> elements, List<Object> elementsForTag, String tag) {
        Map<String, Integer> occurrances = new HashMap<>();
        for(Object article : elementsForTag) {
            List<String> words = Converter.contentToWords(((Article)article).getContent());
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
        for(String word : occurrances.keySet()) {
            if(occurrances.get(word) > elementsForTag.size()/2 && elementsForTag.size() > 30) {
                System.out.println(word + "\t\t" + occurrances.get(word));
            }
        }
        return vector;
    }
}
