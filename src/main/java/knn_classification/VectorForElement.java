package knn_classification;

import extracting.Converter;
import matching_words.word_comparators.WordComparator;
import parsing.Article;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class VectorForElement {
    public static List<Float> generateVector(List<List<Object>> objectsVector, Object element, WordComparator comparator) {
        List<Float> vector = new LinkedList<>();
        objectsVector.forEach(e -> vector.add(0f));

        List<String> words = Converter.contentToWords(((Article)element).getContent());
        for (String word : words) {
            for (int i = 0; i < objectsVector.size(); i++) {
                for (Object word2 : (objectsVector.get(i))) {
                    vector.set(i, vector.get(i) + comparator.similarity(word, word2));
                }
            }
        }
        float max = vector.stream().max(Comparator.comparing(Float::floatValue)).get();
        if(max != 0) {
            for (int i = 0; i < vector.size(); i++) {
                vector.set(i, vector.get(i)/max);
            }
        }
        return vector;
    }
}
