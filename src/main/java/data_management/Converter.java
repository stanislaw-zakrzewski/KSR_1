package data_management;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class Converter {
    public Map<Object, Float> articlesToVector(List<Object> articles) {
        HashSet<String> words = new HashSet<>();
        for(Object article : articles) {
            words.addAll(((Article)article).getLemmatizedWords());
        }
        Map<Object, Float> vector = new HashMap<>();
        for(String word : words) {
            vector.put(word, 1.f);
        }
        return vector;
    }
}
