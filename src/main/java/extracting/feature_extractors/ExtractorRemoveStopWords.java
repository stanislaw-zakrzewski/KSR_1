package extracting.feature_extractors;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExtractorRemoveStopWords implements Extractor {

    /***
     * Highly dependant on Stopwords class!
     *
     * @param vector - used
     * @param elements - unused in this case
     * @param elementsForTag - unused in this case
     * @param tag - unused in this case
     * @return updated vector, with removed stopwords
     */
    public Map<Object, Float> extract(Map<Object, Float> vector, List<Object> elements, List<Object> elementsForTag, String tag) {
        List<String> toRemove = new ArrayList<>();
        for (Object key : vector.keySet()) {
            if (Stopwords.getInstance().contains((String) key)) {
                toRemove.add((String) key);
            }
        }
        for (String key : toRemove) {
            vector.remove(key);
        }
        return vector;
    }
}