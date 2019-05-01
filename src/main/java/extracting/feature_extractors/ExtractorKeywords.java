package extracting.feature_extractors;

import data_management.Article;
import data_management.Converter;
import data_management.Elements;
import extracting.NElementsSelector;
import matching_words.word_comparators.WordComparator;

import java.util.*;

public class ExtractorKeywords implements Extractor {

    @Override
    public List<Object> extract(Elements elements) {
        List<Object> vector = new ArrayList<>();
        Converter converter = new Converter();
        for (String tag : elements.getTags()) {
            Map<Object, Float> vectorPart = converter.articlesToVector(elements.getTrainElementsForTag(tag));

            //remove stopwords
            List<String> toRemove = new ArrayList<>();
            for (Object key : vectorPart.keySet()) {
                if (Stopwords.getInstance().contains(((String) key).toLowerCase())) {
                    toRemove.add((String) key);
                }
            }
            for (String key : toRemove) {
                vectorPart.remove(key);
            }

            //Update value of first words
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

            for (Object s : NElementsSelector.selectN(vectorPart, 5)) {
                if (!vector.contains(s)) {
                    vector.add(s);
                }
            }
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
