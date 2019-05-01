import data_management.*;
import edu.stanford.nlp.coref.data.CorefChain;
import edu.stanford.nlp.ie.util.RelationTriple;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.trees.Tree;
import extracting.MainExtractor;
import extracting.feature_extractors.Extractor;
import extracting.feature_extractors.ExtractorKeywords;
import extracting.feature_extractors.ExtractorRemoveNumbers;
import extracting.feature_extractors.ExtractorRemoveStopWords;
import extracting.feature_extractors.method_1_specific.ExtractorFirstWords;
import extracting.feature_extractors.method_2_specific.SemioticExtractor;
import extracting.feature_extractors.method_3_specific.ExtractorOurMethod;
import extracting.feature_extractors.method_4_specific.PoemExtractor;
import knn_classification.VectorForElement;
import knn_classification.knnNetwork;
import matching_words.word_comparators.OurComparator;
import matching_words.word_comparators.WordComparator;
import program_performance.Stopwatch;
import results.Accuraccy;
import results.ConfusionMatrix;
import results.MultiClassPrecision;
import results.PrecisionAndRecallForTags;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        ConfigReader config = new ConfigReader("src/main/resources/config.txt");

        Stopwatch stopwatch = new Stopwatch();

        // Read all elements
        Elements elements = new Elements(config.getTagClass(), config.getTags(), config.getTrainToTestRatio(), "src/main/resources/extracted/reuters.txt");
        System.out.println(stopwatch.getTime());

        // Extract vector pattern
        List<Extractor> extractors = new ArrayList<>();
        extractors.add(new ExtractorKeywords());
        List<List<Object>> vector = MainExtractor.createVector(elements, extractors);
        for (List<Object> o1 : vector) {
            for (Object o2 : o1) {
                System.out.println(o2);
            }
        }

        List<List<Float>> testVectors = new LinkedList<>();
        knnNetwork network = null;
        int ext = Integer.parseInt(config.getExtractor());


        VectorForElement vectorForElement = new VectorForElement();

        for (Object o : elements.getTestElements()) {
            testVectors.add(MainExtractor.calculateValues(o, extractors, vector, config.getWordSimilarity()));
        }

        int vectorSize = vector.stream().mapToInt(List::size).sum();
        network = new knnNetwork(vectorSize, config.getTags(), config.getTagClass());


        //Use knn to classify articles
        for (int i = 0; i < testVectors.size(); i++) {
            network.addVector(elements.getTestElements().get(i), testVectors.get(i));
        }
        Map<Object, String> classifiedArticles = network.classify(config.getK(), config.getFractionOfUncoveredForEachTag(), config.getDistance());

        //Show results of classification
        List<String> correctLabels = new ArrayList<>();
        List<String> resultLabels = new ArrayList<>();
        for (Object o : classifiedArticles.keySet()) {
            correctLabels.add(((Article) o).getTags().get("PLACES").get(0));
            resultLabels.add(classifiedArticles.get(o));
        }
        System.out.println("Multi-Class Pecision: " + MultiClassPrecision.calculate(config.getTags(), correctLabels, resultLabels) + "\n");
        PrecisionAndRecallForTags.show(config.getTags(), correctLabels, resultLabels);
        System.out.println();
        ConfusionMatrix.calculate(config.getTags(), correctLabels, resultLabels);
        float acc = 0;
        for (String tag : config.getTags()) {
            System.out.println(Accuraccy.calculate(tag, correctLabels, resultLabels));
            acc += Accuraccy.calculate(tag, correctLabels, resultLabels);
        }
        System.out.println("Accuracy: " + acc / config.getTags().size());
        System.out.println("Cały program: " + stopwatch.getTime());
    }
}
