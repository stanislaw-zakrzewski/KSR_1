package extracting;

import extracting.feature_extractors.Extractor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainExtractor {
    public static List<Object> createVector(List<Object> elements, Map<String, List<Object>> elementsByTags, List<String> tags, int numberOfElementsPerTag, List<Extractor> extractors) {
        List<Object> vector = new ArrayList<>();

        for (String tag : tags) {
            Map<Object, Float> vectorPart = Converter.articlesToVector(elementsByTags.get(tag));

            for(Extractor extractor : extractors) {
                vectorPart = extractor.extract(vectorPart, elements, elementsByTags.get(tag), tag);
            }

            //TODO Stemizacja PorterStemmer

            //TODO Remove frequent occurances

            for (Object element : NElementsSelector.selectN(vectorPart, numberOfElementsPerTag)) {
                if (!vector.contains(element)) {
                    vector.add(element);
                }
            }
        }
        return vector;
    }
}
