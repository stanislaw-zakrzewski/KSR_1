package knn_classification;

import matching_words.word_comparators.OurComparator;
import matching_words.word_comparators.WordComparator;
import parsing.Article;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class VectorForElement {
    public List<Float> generateVector(List<List<Object>> objectsVector, Object element, WordComparator comparator) {
        List<Float> vector = new LinkedList<>();
        objectsVector.forEach(e -> vector.add(0f));

        List<String> words = ((Article) element).getLemmatizedWords();

        if (comparator instanceof OurComparator) {
            words = ((Article) element).getOurWords();
        }

        for (String word : words) {
            for (int i = 0; i < objectsVector.size(); i++) {
                for (Object word2 : (objectsVector.get(i))) {
                    float similarity = comparator.similarity(word, word2);
                    vector.set(i, vector.get(i) + comparator.similarity(word, word2));
                }
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
