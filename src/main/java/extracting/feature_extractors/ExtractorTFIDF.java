package extracting.feature_extractors;

import parsing.Article;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExtractorTFIDF implements Extractor {
    private int n;
    private int wordsCount;

    @Override
    public Map<Object, Float> extract(Map<Object, Float> vector, List<Object> elements, List<Object> elementsForTag, String tag) {
        n = elementsForTag.size();
        List<List<String>> allDocuments = new ArrayList<>();
        for(Object o : elements) {
            allDocuments.add(StanfordLemmatizer.getInstance().lemmatize(((Article)o).getContent()));
        }
        for(Object o : vector.keySet()) {
            vector.replace(o, calculateTFIDFForSingleWord((String)o, allDocuments));
        }
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
