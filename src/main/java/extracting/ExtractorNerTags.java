package extracting;

import data_management.Article;
import data_management.Converter;
import data_management.Elements;
import extracting.feature_extractors.Extractor;
import extracting.feature_extractors.Stopwords;
import matching_words.word_comparators.WordComparator;
import program_performance.Stopwatch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/*
Possible ner tags:
LOCATION
NATIONALITY
NUMBER
MONEY
IDEOLOGY
PERSON
SET
MISC
TIME
ORDINAL
CAUSE_OF_DEATH
URL
STATE_OR_PROVINCE
ORGANIZATION
DATE
COUNTRY
CITY
RELIGION
PERCENT
TITLE
DURATION
CRIMINAL_CHARGE
 */

public class ExtractorNerTags implements Extractor {
    private static final List<String> nerTags = Arrays.asList("LOCATION", "NATIONALITY", "PERSON", "MONEY", "COUNTRY", "STATE_OR_PROVINCE");

    @SuppressWarnings("Duplicates")
    @Override
    public List<Object> extract(Elements elements) {
        System.out.println("I  Extractor ner tags:");
        Stopwatch stopwatch = new Stopwatch();
        List<Object> vector = new ArrayList<>();
        Converter converter = new Converter();
        for (String tag : elements.getTags()) {
            Map<Object, Float> vectorPart = converter.articlesToVectorUseNerTags(elements.getTrainElementsForTag(tag), nerTags);

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
                for(String nerTag : nerTags) {
                    Article current = (Article)o;
                    for (String key : current.getNerTags().keySet()) {
                        if(nerTag.equals(current.getNerTags().get(key))) {
                            if (vectorPart.containsKey(key)) {
                                vectorPart.replace(key, vectorPart.get(key) * (1 + (1.0f / 10) / 10));
                                if (wordsCounter == 10) {
                                    break;
                                } else {
                                    wordsCounter++;
                                }
                            }
                        }
                    }
                }
            }

            // Select N keywords from text
            System.out.println("\t# " + tag + ":");
            for (Object s : NElementsSelector.selectN(vectorPart, 5)) {
                System.out.println("\t\t> " + s);
                if (!vector.contains(s)) {
                    vector.add(s);
                } else {
                    vector.remove(s);
                }
            }
        }
        System.out.println("T  Extractor entities: " + stopwatch.getTime() + "s");
        return vector;
    }

    @SuppressWarnings("Duplicates")
    @Override
    public List<Float> getValues(List<Object> vector, Object element, WordComparator wordComparator) {
        List<Float> valuesForElement = new ArrayList<>();
        for (Object ignored : vector) {
            valuesForElement.add(0.f);
        }
        Article article = (Article) element;
        for(String nerTag : nerTags) {
            for (String key : article.getNerTags().keySet()) {
                if(nerTag.equals(article.getNerTags().get(key))) {
                    for(int i = 0; i < vector.size(); i++) {
                        valuesForElement.set(i, valuesForElement.get(i) + wordComparator.similarity(key,vector.get(i)));
                    }
                }
            }
        }
        for (String lemma : article.getEntityMentions()) {
            for (int i = 0; i < vector.size(); i++) {
                valuesForElement.set(i, valuesForElement.get(i) + wordComparator.similarity(lemma, vector.get(i)));
            }
        }
        return valuesForElement;
    }
}
