package extracting.feature_extractors;

import java.util.List;
import java.util.Map;

public interface Extractor {
    Map<Object, Float> extract(Map<Object, Float> vector, List<Object> elements, List<Object> elementsForTag, String tag);
}
