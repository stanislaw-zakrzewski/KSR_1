package data_management;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class Converter {
    public Map<Object, Float> articlesToVector(List<Object> articles) {
        HashSet<String> words = new HashSet<>();
        for(Object article : articles) {
            words.addAll(((Article)article).getLemmas());
        }
        Map<Object, Float> vector = new HashMap<>();
        for(String word : words) {
            vector.put(word, 1.f);
        }
        return vector;
    }

    public Map<Object, Float> articlesToVectorUseEntities(List<Object> articles) {
        HashSet<String> words = new HashSet<>();
        for(Object article : articles) {
            words.addAll(((Article)article).getEntityMentions());
        }
        Map<Object, Float> vector = new HashMap<>();
        for(String word : words) {
            vector.put(word, 1.f);
        }
        return vector;
    }

    public Map<Object, Float> articlesToVectorUseNerTags(List<Object> articles, List<String> nerTags) {
        Map<Object, Float> vector = new HashMap<>();
        for(String tag : nerTags) {
            Map<Object, Float> vectorPart = articlesToVectorSingleNerTag(articles, tag);
            for(Object key : vectorPart.keySet()) {
                if(!vector.containsKey(key)) {
                    vector.put(key, vectorPart.get(key));
                }
            }
        }
        return vector;
    }

    private Map<Object, Float> articlesToVectorSingleNerTag(List<Object> articles, String nerTag) {
        HashSet<String> words = new HashSet<>();
        for(Object article : articles) {
            Article current = (Article)article;
            for(String key : current.getNerTags().keySet()) {
                if(current.getNerTags().get(key).equals(nerTag)) {
                    words.add(key);
                }
            }
        }
        Map<Object, Float> vector = new HashMap<>();
        for(String word : words) {
            vector.put(word, 1.f);
        }
        return vector;
    }
}
