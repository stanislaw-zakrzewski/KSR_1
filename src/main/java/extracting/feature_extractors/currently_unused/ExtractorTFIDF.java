package extracting.feature_extractors.currently_unused;

import extracting.feature_extractors.Extractor;
import data_management.StanfordLemmatizer;
import data_management.Article;
import program_performance.Stopwatch;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExtractorTFIDF implements Extractor {
    private int n;
    private int wordsCount;

    @Override
    public Map<Object, Float> extract(Map<Object, Float> vector, List<Object> elements, List<Object> elementsForTag, String tag) {
        Stopwatch stopwatch = new Stopwatch();
        n = elementsForTag.size();
        List<List<String>> allDocuments = new ArrayList<>();
        for(Object o : elementsForTag) {
            allDocuments.add(StanfordLemmatizer.getInstance().lemmatize(((Article)o).getContent()));
        }
        for(Object o : vector.keySet()) {
            vector.replace(o, calculateTFIDFForSingleWord((String)o, allDocuments)/elementsForTag.size());
        }
        System.out.println("ExtractorTFIDF: " + stopwatch.getTime());
        return vector;
    }

    private Float calculateTFIDFForSingleWord(String word, List<List<String>> elements) {
        wordsCount = 0;
        float value = 0;
        List<Float> tfs = new ArrayList<>();
        for (List<String> o : elements) {
            tfs.add(calculateForSingleDocument(word, o));
        }
        float idf = (float) Math.log((float) n / (float) wordsCount);
        tfs.forEach(v -> v /= idf);
        for (Float f : tfs) {
            value += f;
        }
        return value;
    }

    private Float calculateForSingleDocument(String word, List<String> words) {
        for (String s : words) {
            if (s.equals(word)) {
                wordsCount++;
            }
        }
        return (float)wordsCount/(float)words.size();
    }
}
