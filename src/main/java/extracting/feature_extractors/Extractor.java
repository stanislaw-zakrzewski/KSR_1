package extracting.feature_extractors;

import matching_words.word_comparators.WordComparator;

import java.util.List;
import java.util.Map;

public interface Extractor {
    Map<Object, Float> extract(Map<Object, Float> vector, List<Object> elements, List<Object> elementsForTag, String tag);
    List<Float> getValues(List<Object> vector, Object element, WordComparator wordComparator);
}
