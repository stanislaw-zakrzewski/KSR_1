package extracting.feature_extractors;

import parsing.Article;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExtractorRemoveStopWords implements Extractor {
    public Map<String, Float> extract(Map<String, Float> vector, List<Article> articles) {
        List<String> toRemove = new ArrayList<>();
        for(String key : vector.keySet()) {
            if(Stopwords.getInstance().contains(key)) {
                toRemove.add(key);
            }
        }
        for(String key : toRemove) {
            vector.remove(key);
        }
        return vector;
    }
}
