import extracting.MainExtractor;
import extracting.feature_extractors.Extractor;
import extracting.feature_extractors.ExtractorFirstWords;
import extracting.feature_extractors.ExtractorRemoveStopWords;
import knn_classification.VectorForElement;
import knn_classification.knnNetwork;
import matching_words.word_comparators.NGrams;
import matching_words.word_comparators.WordComparator;
import parsing.Article;
import parsing.ReadAll;

import java.util.*;

public class Main {
    private static final int k = 10;
    private static final int numberOfElementsUncoveredForEachTag = 20;
    private static final List<String> tags = List.of("west-germany", "usa", "france", "uk", "canada", "japan");
    private static final int numberOfElementsPerTag = 5;
    private static final float trainToTestRatio = 0.4f;

    public static void main(String[] args) {
        //Parametry do klasyfikacji (1/3)
        ReadAll readAll = new ReadAll();
        List<Object> allArticles = readAll.readAll("src/main/resources/sgm/", "PLACES");

        //Remove all articles that have tags count other than 1 and if they have 1 check if it is in the list of tags
        List<Object> toRemove = new ArrayList<>();
        for(Object article : allArticles) {
            if(((Article)article).getTags().size() != 1) {
                toRemove.add(article);
            } else {
                if(!tags.contains(((Article)article).getTags().get(0))) {
                    toRemove.add(article);
                }
            }
        }
        allArticles.removeAll(toRemove);

        List<Object> trainArticles = new ArrayList<>();
        List<Object> testArticles = new ArrayList<>();
        for(int i = 0; i < allArticles.size(); i++) {
            if(((float)i)/((float)allArticles.size()) < trainToTestRatio) {
                trainArticles.add(allArticles.get(i));
            } else {
                testArticles.add(allArticles.get(i));
            }
        }

        Map<String, List<Object>> trainArticlesByTags = new HashMap<>();

        tags.forEach(tag -> trainArticlesByTags.put(tag, new ArrayList<>()));
        trainArticles.forEach(article -> {
                    List<String> tagsForArticle = ((Article) article).getTags();
                    if (tagsForArticle.size() == 1) {
                        if (tags.contains(tagsForArticle.get(0))) {
                            trainArticlesByTags.get(tagsForArticle.get(0)).add(article);
                        }
                    }
                }
        );

        //Create list of extractors
        List<Extractor> extractors = new ArrayList<>();
        extractors.add(new ExtractorRemoveStopWords());
        extractors.add(new ExtractorFirstWords());

        //Generate vector using extractors
        List<List<Object>> vector = MainExtractor.createVector(trainArticles, trainArticlesByTags, tags, numberOfElementsPerTag, extractors);

        //Generate vectors for articles in test set
        WordComparator comparator = new NGrams();
        List<List<Float>> testVectors = new LinkedList<>();
        testArticles.forEach(a -> testVectors.add(VectorForElement.generateVector(vector, a, comparator)));

        //Use knn to classify articles
        knnNetwork network = new knnNetwork(vector.size(), tags);
        for(int i = 0; i < testVectors.size(); i++) {
            network.addVector(testArticles.get(i), testVectors.get(i));
        }
        Map<Object, String> classifiedArticles = network.classify(k,numberOfElementsUncoveredForEachTag);
        for(Object o : classifiedArticles.keySet()) {
            System.out.println(((Article)o).getTags().get(0) + "    " + classifiedArticles.get(o));
        }
    }
}
