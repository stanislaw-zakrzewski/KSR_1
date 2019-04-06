package extracting.feature_extractors;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExtractorRemoveNumbers implements Extractor {
    /***
     * Replaces all numbers with NUMBER word
     *
     * @param vector - used
     * @param elements - unused in this case
     * @param elementsForTag - unused in this case
     * @param tag - unused in this case
     * @return updated vector, with removed numbers
     */
    @Override
    public Map<Object, Float> extract(Map<Object, Float> vector, List<Object> elements, List<Object> elementsForTag, String tag) {
        List<String> toRemove = new ArrayList<>();
        for (Object key : vector.keySet()) {
            for (char c : ((String) key).toCharArray()) {
                if (Character.isDigit(c)) toRemove.add((String) key);
            }

        }
        for (String key : toRemove) {
            vector.remove(key);
        }
        return vector;
    }
}
