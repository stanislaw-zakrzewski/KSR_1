package extracting.feature_extractors;

import data_management.Elements;
import matching_words.word_comparators.WordComparator;

import java.util.List;

public interface Extractor {
    List<Object> extract(Elements elements);
    List<Float> getValues(List<Object> vector, Object element, WordComparator wordComparator);
}
