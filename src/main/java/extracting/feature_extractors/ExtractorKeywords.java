package extracting.feature_extractors;

import data_management.Article;
import matching_words.word_comparators.WordComparator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExtractorKeywords implements Extractor {
    @Override
    public Map<Object, Float> extract(Map<Object, Float> vector, List<Object> elements, List<Object> elementsForTag, String tag) {
        return null;
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
