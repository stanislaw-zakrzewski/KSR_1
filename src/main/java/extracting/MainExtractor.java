package extracting;

import extracting.feature_extractors.Extractor;
import results.Stopwatch;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainExtractor {
    public static List<Object> createVector(List<Object> elements, Map<String, List<Object>> elementsByTags, List<String> tags, int numberOfElementsPerTag, List<Extractor> extractors) {
        Converter converter = new Converter();
        List<Object> vector = new ArrayList<>();

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
        }
        return vector;
    }
}
