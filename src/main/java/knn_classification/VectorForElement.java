package knn_classification;

import matching_words.word_comparators.OurComparator;
import matching_words.word_comparators.WordComparator;
import data_management.Article;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class VectorForElement {
    public List<Float> generateVector(List<Object> objectsVector, Object element, WordComparator comparator) {
        List<Float> vector = new LinkedList<>();
        for(int i = 0; i < objectsVector.size(); i++) {
            vector.add(0.f);
        }

        List<String> words = ((Article) element).getLemmas();

        if (comparator instanceof OurComparator) {
            //words = ((Article) element).getOurWords();
        }

        for (String word : words) {
            for (int i = 0; i < objectsVector.size(); i++) {
                vector.set(i, vector.get(i) + comparator.similarity(word, objectsVector.get(i)));
            }
        }
        Optional<Float> max = vector.stream().max(Comparator.comparing(Float::floatValue));
        if (max.isPresent()) {
            if (max.get() != 0) {
                for (int i = 0; i < vector.size(); i++) {
                    vector.set(i, vector.get(i) / max.get());
                }
            }
        }
        return vector;
    }
}
