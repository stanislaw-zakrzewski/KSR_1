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
    public static void main(String[] args) {

        //Parametry do klasyfikacji (1/3)
        List<String> tags = Arrays.asList("west-germany", "usa", "france", "uk", "canada", "japan");

        ReadAll readAll = new ReadAll();
        List<Object> articles = readAll.readAll("src/main/resources/sgm/", "PLACES");

        Map<String, List<Object>> articlesByTags = new HashMap<>();

        tags.forEach(tag -> articlesByTags.put(tag, new ArrayList<>()));
        articles.forEach(article -> {
                    List<String> tagsForArticle = ((Article) article).getTags();
                    if (tagsForArticle.size() == 1) {
                        if (tags.contains(tagsForArticle.get(0))) {
                            articlesByTags.get(tagsForArticle.get(0)).add(article);
                        }
                    }
                }
        );

        List<Object> toRemove = new ArrayList<>();
        for(Object article : articles) {
            if(((Article)article).getTags().size() != 1) {
                toRemove.add(article);
            } else {
                if(!tags.contains(((Article)article).getTags().get(0))) {
                    toRemove.add(article);
                }
            }
        }
        articles.removeAll(toRemove);

        List<Extractor> extractors = new ArrayList<>();
        extractors.add(new ExtractorRemoveStopWords());
        extractors.add(new ExtractorFirstWords());

        List<List<Object>> vector = MainExtractor.createVector(articles, articlesByTags, tags, 5, extractors);

        WordComparator comparator = new NGrams();

        List<List<Float>> testVectors = new LinkedList<>();
        articles.forEach(a -> testVectors.add(VectorForElement.generateVector(vector, a, comparator)));

        Map<String, List<Float>> ret = new HashMap<>();
        for(String tag : tags) {
            List<Float> pom = new ArrayList<>();
            for(int i = 0; i < tags.size(); i++) {
                pom.add(0.f);
            }
            ret.put(tag, pom);
        }


        /*for(int i = 0; i < testVectors.size(); i++) {
            System.out.println(((Article)articles.get(i)).getTags().get(0));
            testVectors.get(i).forEach(v -> System.out.print(v + "  |  "));
            List<Float> pom = ret.get(((Article)articles.get(i)).getTags().get(0));
            for(int j = 0; j < testVectors.get(i).size(); j++) {
                pom.set(j, pom.get(j) + testVectors.get(i).get(j));
            }
            System.out.println();
            System.out.println();
        }*/

        //TODO After reding all the articles we should filter them

        for(String tag : tags) {
            System.out.println(tag);
            ret.get(tag).forEach(r -> System.out.print(r + "  |  "));
            System.out.println();
        }

        for (List<Object> words : vector) {
            words.forEach(w -> System.out.print(w + "\t"));
            System.out.println();
        }

        knnNetwork network = new knnNetwork(vector.size(), tags);
        for(int i = 0; i < testVectors.size(); i++) {
            network.addVector(articles.get(i), testVectors.get(i));
        }
        Map<Object, String> oko = network.classify(10,200);

        for(Object o : oko.keySet()) {
            System.out.println(((Article)o).getTags().get(0) + "    " + oko.get(o));
        }
    }
}
