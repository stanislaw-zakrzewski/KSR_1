package extracting;

import data_management.Converter;
import data_management.Elements;
import extracting.feature_extractors.Extractor;
import matching_words.word_comparators.WordComparator;
import program_performance.Stopwatch;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainExtractor {
    public static List<List<Object>> createVector(Elements elements, List<Extractor> extractors) {
        Converter converter = new Converter();
        List<List<Object>> vector = new ArrayList<>();

        for(Extractor extractor : extractors) {
            vector.add(extractor.extract(elements));
        }
        /*
        for (int i = 0; i < tags.size(); i++) {
            Stopwatch stopwatch = new Stopwatch();
            Map<Object, Float> vectorPart = converter.articlesToVector(elementsByTags.get(tags.get(i)));
            System.out.println("Generate vector " + stopwatch.getTime());

            for (Extractor extractor : extractors) {
                vectorPart = extractor.extract(vectorPart, elements, elementsByTags.get(tags.get(i)), tags.get(i));
            }

            System.out.println("--" + tags.get(i));
            for (Object element : NElementsSelector.selectN(vectorPart, numberOfElementsPerTag)) {
                if(!vector.contains(element)) {
                    vector.add(element);
                }
                System.out.println(element);
            }
            System.out.println();
        }

        for(Object o : vector) {
            System.out.println(o);
        }*/
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
