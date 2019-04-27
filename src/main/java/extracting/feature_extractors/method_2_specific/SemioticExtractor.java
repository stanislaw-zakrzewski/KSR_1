package extracting.feature_extractors.method_2_specific;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import data_management.Article;

import java.util.*;

public class SemioticExtractor {
    private static SemioticExtractor instance;
    private StanfordCoreNLP pipeline;

    public static SemioticExtractor getInstance() {
        if(instance == null) {
            instance = new SemioticExtractor();
        }
        return instance;
    }

    private SemioticExtractor() {
        Properties props;
        props = new Properties();
        props.put("annotators", "tokenize, ssplit");

        this.pipeline = new StanfordCoreNLP(props);
    }

    public List<Float> calculateVector(Object element) {
        List<Float> vector = new ArrayList<>();
        Annotation document = new Annotation(((Article) element).getContent());
        pipeline.annotate(document);

        //First element is sentence count
        vector.add((float) document.get(CoreAnnotations.SentencesAnnotation.class).size());

        //Second element is the words count
        vector.add((float) ((Article) element).getLemmas().size());

        //Next 8 elements contains count of different size words
        Map<String, Float> wordsCount = new HashMap<>();
        wordsCount.put("2orLess", 0.f);
        wordsCount.put("3", 0.f);
        wordsCount.put("4", 0.f);
        wordsCount.put("5", 0.f);
        wordsCount.put("6", 0.f);
        wordsCount.put("7", 0.f);
        wordsCount.put("8", 0.f);
        wordsCount.put("9orMore", 0.f);

        for (String word : ((Article) element).getLemmas()) {
            switch (word.length()) {
                case 0:
                case 1:
                case 2:
                    wordsCount.replace("2orLess", wordsCount.get("2orLess") + 1);
                    break;
                case 3:
                    wordsCount.replace("3", wordsCount.get("3") + 1);
                    break;
                case 4:
                    wordsCount.replace("4", wordsCount.get("4") + 1);
                    break;
                case 5:
                    wordsCount.replace("5", wordsCount.get("5") + 1);
                    break;
                case 6:
                    wordsCount.replace("6", wordsCount.get("6") + 1);
                    break;
                case 7:
                    wordsCount.replace("7", wordsCount.get("7") + 1);
                    break;
                case 8:
                    wordsCount.replace("8", wordsCount.get("8") + 1);
                    break;
                default:
                    wordsCount.replace("9orMore", wordsCount.get("9orMore") + 1);
                    break;
            }
        }

        for (String key : wordsCount.keySet()) {
            vector.add(wordsCount.get(key));
        }

        //11th element is the count of capitalized words
        float capitalizedCount = 0;
        for(String word : ((Article)element).getLemmas()) {
            if(Character.isUpperCase(word.charAt(0))) {
                capitalizedCount += 1;
            }
        }
        vector.add(capitalizedCount);

        return vector;
    }
}
