package extracting.feature_extractors;

import parsing.Article;

import java.util.List;
import java.util.Map;

public interface Extractor {
    Map<String, Float> extract(Map<String, Float> vector, List<Article> articles);
}
