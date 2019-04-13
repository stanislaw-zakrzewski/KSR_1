import extracting.MainExtractor;
import extracting.feature_extractors.*;
import knn_classification.VectorForElement;
import knn_classification.knnNetwork;
import parsing.Article;
import parsing.ReadAll;
import results.*;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        ConfigReader config = new ConfigReader("src/main/resources/config.txt");

        Stopwatch stopwatch = new Stopwatch();

        //Read all articles
        List<Object> allArticles = ReadAll.read(config.getFolderPath(), config.getTagClass(), config.getArticlesToReadCount());
        Collections.shuffle(allArticles);

        //Remove all articles that have tags count other than 1 and if they have 1 check if it is in the list of tags
        List<Object> toRemove = new ArrayList<>();
        for (Object article : allArticles) {
            if (((Article) article).getTags().size() != 1) {
                toRemove.add(article);
            } else {
                if (!config.getTags().contains(((Article) article).getTags().get(0))) {
                    toRemove.add(article);
                }
            }
        }
        allArticles.removeAll(toRemove);

        //Split articles into train and test groups
        List<Object> trainArticles = new ArrayList<>();
        List<Object> testArticles = new ArrayList<>();
        for (int i = 0; i < allArticles.size(); i++) {
            if (((float) i) / ((float) allArticles.size()) < config.getTrainToTestRatio()) {
                trainArticles.add(allArticles.get(i));
            } else {
                testArticles.add(allArticles.get(i));
            }
        }

        Map<String, List<Object>> trainArticlesByTags = getElementsForTags(trainArticles, config.getTags());
        Map<String, List<Object>> testArticlesByTags = getElementsForTags(testArticles, config.getTags());

        //Check how many elements are there for each tag
        for (String o : testArticlesByTags.keySet()) {
            System.out.println(o + "  " + testArticlesByTags.get(o).size());
        }

        List<List<Float>> testVectors = new LinkedList<>();
        knnNetwork network = null;
        if (config.getExtractor().equals("1")) {
            //Generate vector pattern using extractors
            List<Extractor> extractors = new ArrayList<>();
            extractors.add(new ExtractorRemoveStopWords());
            extractors.add(new ExtractorRemoveNumbers());
            extractors.add(new ExtractorFirstWords());
            List<List<Object>> vector = MainExtractor.createVector(trainArticles, trainArticlesByTags, config.getTags(), config.getNumberOfElementsPerTag(), extractors);


            VectorForElement vectorForElement = new VectorForElement();
            for (Object o : testArticles) {
                testVectors.add(vectorForElement.generateVector(vector, o, config.getWordSimilarity()));
            }

            network = new knnNetwork(vector.size(), config.getTags());
        } else {
            for (Object o : testArticles) {
                testVectors.add(SemioticExtractor.getInstance().calculateVector(o));
            }
            network = new knnNetwork(11, config.getTags());
        }

        //Use knn to classify articles
        for (int i = 0; i < testVectors.size(); i++) {
            network.addVector(testArticles.get(i), testVectors.get(i));
        }
        Map<Object, String> classifiedArticles = network.classify(config.getK(), config.getFractionOfUncoveredForEachTag(), config.getDistance());

        //Show results of classification
        List<String> correctLabels = new ArrayList<>();
        List<String> resultLabels = new ArrayList<>();
        for (Object o : classifiedArticles.keySet()) {
            correctLabels.add(((Article) o).getTags().get(0));
            resultLabels.add(classifiedArticles.get(o));
        }
        System.out.println("Multi-Class Pecision: " + MultiClassPrecision.calculate(config.getTags(), correctLabels, resultLabels) + "\n");
        PrecisionAndRecallForTags.show(config.getTags(), correctLabels, resultLabels);
        System.out.println();
        ConfusionMatrix.calculate(config.getTags(), correctLabels, resultLabels);
        System.out.println("Cały program: " + stopwatch.getTime());
    }

    private static Map<String, List<Object>> getElementsForTags(List<Object> elements, List<String> tags) {
        Map<String, List<Object>> elementsForTags = new HashMap<>();
        tags.forEach(tag -> elementsForTags.put(tag, new ArrayList<>()));
        elements.forEach(article -> {
                    List<String> tagsForArticle = ((Article) article).getTags();
                    if (tagsForArticle.size() == 1) {
                        if (tags.contains(tagsForArticle.get(0))) {
                            elementsForTags.get(tagsForArticle.get(0)).add(article);
                        }
                    }
                }
        );
        return elementsForTags;
    }
}
