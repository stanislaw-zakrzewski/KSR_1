package extracting.feature_extractors.method_3_specific;

import extracting.feature_extractors.Extractor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExtractorOurMethod implements Extractor {
    @Override
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
}
