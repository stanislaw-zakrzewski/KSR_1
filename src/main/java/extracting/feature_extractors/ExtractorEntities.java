package extracting.feature_extractors;

import data_management.Article;
import data_management.Converter;
import data_management.Elements;
import extracting.NElementsSelector;
import matching_words.word_comparators.WordComparator;
import program_performance.Stopwatch;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExtractorEntities implements Extractor {

    @SuppressWarnings("Duplicates")
    @Override
    public List<Object> extract(Elements elements) {
        Stopwatch stopwatch = new Stopwatch();
        List<Object> vector = new ArrayList<>();
        Converter converter = new Converter();
        for (String tag : elements.getTags()) {
            Map<Object, Float> vectorPart = converter.articlesToVector(elements.getTrainElementsForTag(tag));

            // Remove numbers
            List<String> toRemove = new ArrayList<>();
            for (Object key : vectorPart.keySet()) {
                for (char c : ((String) key).toCharArray()) {
                    if (Character.isDigit(c)) {
                        toRemove.add((String) key);
                        break;
                    }
                }
            }
            vectorPart.keySet().removeAll(toRemove);

            // Remove stop words
            toRemove = new ArrayList<>();
            for (Object key : vectorPart.keySet()) {
                if (Stopwords.getInstance().contains(((String) key).toLowerCase())) {
                    toRemove.add((String) key);
                }
            }
            vectorPart.keySet().removeAll(toRemove);

            // Update value of first words
            for (Object o : elements.getTrainElementsForTag(tag)) {
                int wordsCounter = 1;
                for (String word : ((Article) o).getLemmas()) {
                    if (vectorPart.containsKey(word)) {
                        vectorPart.replace(word, vectorPart.get(word) * (1 + (1.0f / 10) / 10));
                        if (wordsCounter == 10) {
                            break;
                        } else {
                            wordsCounter++;
                        }
                    }
                }
            }

            // Select N keywords from text
            for (Object s : NElementsSelector.selectN(vectorPart, 5)) {
                if (!vector.contains(s)) {
                    vector.add(s);
                }
            }
        }
        System.out.println("I  Extractor entities (" + stopwatch.getTime() + "s), list of entities extracted: ");
        for(Object keyword : vector) {
            System.out.println("\t> " + keyword);
        }
        return vector;
    }

    @Override
    public List<Float> getValues(List<Object> vector, Object element, WordComparator wordComparator) {
        List<Float> valuesForElement = new ArrayList<>();
        for (Object ignored : vector) {
            valuesForElement.add(0.f);
        }
        Article article = (Article) element;
        for (String lemma : article.getLemmas()) {
            for (int i = 0; i < vector.size(); i++) {
                valuesForElement.set(i, valuesForElement.get(i) + wordComparator.similarity(lemma, vector.get(i)));
            }
        }
        return valuesForElement;
    }
}
