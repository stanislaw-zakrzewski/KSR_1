import data_management.Article;
import data_management.ConfigReader;
import data_management.Elements;
import extracting.MainExtractor;
import extracting.feature_extractors.Extractor;
import extracting.feature_extractors.ExtractorKeywords;
import knn_classification.knnNetwork;
import program_performance.Stopwatch;
import results.Accuraccy;
import results.ConfusionMatrix;
import results.MultiClassPrecision;
import results.PrecisionAndRecallForTags;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        ConfigReader config = new ConfigReader("src/main/resources/config.txt");
        Stopwatch stopwatch = new Stopwatch();

        // Read all elements
        Elements elements = new Elements(config.getTagClass(), config.getTags(), config.getTrainToTestRatio(), "src/main/resources/extracted/reuters.txt");
        System.out.println("T  Read all elements: " + stopwatch.getTime() + "s");

        // Extract vector pattern
        List<Extractor> extractors = new ArrayList<>();
        extractors.add(new ExtractorKeywords());
        List<List<Object>> vector = MainExtractor.createVector(elements, extractors);
        System.out.println("T  Extract vector pattern: " + stopwatch.getTime() + "s");

        // Generate vectors for test elements using vector pattern
        List<List<Float>> testVectors = new LinkedList<>();
        for (Object o : elements.getTestElements()) {
            testVectors.add(MainExtractor.calculateValues(o, extractors, vector, config.getWordSimilarity()));
        }
        System.out.println("T  Generate vectors for test elements: " + stopwatch.getTime() + "s");

        // Initialize knn network
        int vectorSize = vector.stream().mapToInt(List::size).sum();
        knnNetwork network = new knnNetwork(vectorSize, config.getTags(), config.getTagClass());


        //Use knn to classify articles
        for (int i = 0; i < testVectors.size(); i++) {
            network.addVector(elements.getTestElements().get(i), testVectors.get(i));
        }
        Map<Object, String> classifiedArticles = network.classify(config.getK(), config.getFractionOfUncoveredForEachTag(), config.getDistance());
        System.out.println("T  Classify test elements: " + stopwatch.getTime() + "s");

        //Show results of classification
        List<String> correctLabels = new ArrayList<>();
        List<String> resultLabels = new ArrayList<>();
        for (Object o : classifiedArticles.keySet()) {
            correctLabels.add(((Article) o).getTags().get(config.getTagClass()).get(0));
            resultLabels.add(classifiedArticles.get(o));
        }
        System.out.println("Multi-class precision: " + MultiClassPrecision.calculate(config.getTags(), correctLabels, resultLabels) + "\n");
        PrecisionAndRecallForTags.show(config.getTags(), correctLabels, resultLabels);
        System.out.println();
        ConfusionMatrix.calculate(config.getTags(), correctLabels, resultLabels);
        float acc = 0;
        for (String tag : config.getTags()) {
            System.out.println("Accuracy of \"" + tag + "\" tag: " + Accuraccy.calculate(tag, correctLabels, resultLabels));
            acc += Accuraccy.calculate(tag, correctLabels, resultLabels);
        }
        System.out.println("Multi-class accuracy: " + acc / config.getTags().size());
        System.out.println("All operations: " + stopwatch.getTime() + "s");
    }
}
