package extracting.feature_extractors.method_3_specific;

import data_management.Elements;
import extracting.feature_extractors.Extractor;
import matching_words.word_comparators.WordComparator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExtractorOurMethod implements Extractor {
    public Map<Object, Float> extract(Map<Object, Float> vector, List<Object> elements, List<Object> elementsForTag, String tag) {
        Map<Object, Float> vectorRefactored = new HashMap<>();
        for(Object o : vector.keySet()) {
            String s = (String)o;
            s = Character.toString(s.charAt(0)) + s.charAt(s.length()-1) + s.length();
            if(vectorRefactored.containsKey(s)) {
                vectorRefactored.replace(s, vectorRefactored.get(s) + vector.get(o));
            } else {
                vectorRefactored.put(s, vector.get(o));
            }
        }

        return vectorRefactored;
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
