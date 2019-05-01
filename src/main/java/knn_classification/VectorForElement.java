package knn_classification;

import extracting.feature_extractors.Extractor;
import matching_words.word_comparators.OurComparator;
import matching_words.word_comparators.WordComparator;
import data_management.Article;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class VectorForElement {
    public List<Float> generateVector(List<List<Object>> objectsVector, Object element, WordComparator wordComparator, List<Extractor> extractors) {
        List<Float> vector = new LinkedList<>();
        for (int i = 0; i < objectsVector.size(); i++) {
            vector.addAll(extractors.get(i).getValues(objectsVector.get(i), element, wordComparator));
        }
        return vector;
    }
}
