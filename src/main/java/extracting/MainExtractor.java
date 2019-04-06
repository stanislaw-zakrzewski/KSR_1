package extracting;

import extracting.feature_extractors.Extractor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainExtractor {
    public static List<List<Object>> createVector(List<Object> elements, Map<String, List<Object>> elementsByTags, List<String> tags, int numberOfElementsPerTag, List<Extractor> extractors) {
        Converter converter = new Converter();
        List<List<Object>> vector = new ArrayList<>();

        for (int i = 0; i < tags.size(); i++) {
            Map<Object, Float> vectorPart = converter.articlesToVector(elementsByTags.get(tags.get(i)));

            for(Extractor extractor : extractors) {
                vectorPart = extractor.extract(vectorPart, elements, elementsByTags.get(tags.get(i)), tags.get(i));
            }

            //TODO Stemizacja PorterStemmer

            //TODO Remove frequent occurances

            System.out.println("--" + tags.get(i));
            vector.add(new ArrayList<Object>());
            for (Object element : NElementsSelector.selectN(vectorPart, numberOfElementsPerTag)) {
                vector.get(i).add(element);
                System.out.println(element);
            }
            System.out.println();
        }
        return vector;
    }
}
