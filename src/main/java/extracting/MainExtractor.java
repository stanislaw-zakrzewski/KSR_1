package extracting;

import data_management.Elements;
import extracting.feature_extractors.Extractor;
import matching_words.word_comparators.WordComparator;
import program_performance.Stopwatch;

import java.util.ArrayList;
import java.util.List;

public class MainExtractor {
    public static List<List<Object>> createVector(Elements elements, List<Extractor> extractors) {
        Stopwatch stopwatch = new Stopwatch();
        List<List<Object>> vector = new ArrayList<>();

        for(Extractor extractor : extractors) {
            vector.add(extractor.extract(elements));
        }
        System.out.println("T  All extractors: " + stopwatch.getTime() + "s");
        return vector;
    }

    public static List<Float> calculateValues(Object element, List<Extractor> extractors, List<List<Object>> vector, WordComparator wordComparator) {
        List<Float> values = new ArrayList<>();
        for(int i = 0; i < extractors.size(); i++) {
            values.addAll(extractors.get(i).getValues(vector.get(i), element, wordComparator));
        }
        return values;
    }
}
