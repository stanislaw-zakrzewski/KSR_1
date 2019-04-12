import extracting.MainExtractor;
import extracting.feature_extractors.*;
import knn_classification.VectorForElement;
import knn_classification.calculate_distance.ChebyshevDistance;
import knn_classification.calculate_distance.Distance;
import knn_classification.calculate_distance.EuclideanDistance;
import knn_classification.calculate_distance.ManhattanDistance;
import knn_classification.knnNetwork;
import matching_words.word_comparators.GeneralizedNGrams;
import matching_words.word_comparators.WordComparator;
import matching_words.word_comparators.NGrams;
import parsing.Article;
import parsing.ReadAll;
import results.ConfusionMatrix;
import results.MultiClassPrecision;
import results.PrecisionAndRecallForTags;
import results.Stopwatch;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        Stopwatch stopwatch = new Stopwatch();
        //Variables read from file
        String tagClass = "";
        String folderPath = "";
        int articlesToReadCount = 0;
        int k = 0;
        float fractionOfUncoveredForEachTag = 0;
        int numberOfElementsPerTag = 0;
        float trainToTestRatio = 0;
        Distance distance = null;
        WordComparator wordComparator = null;
        //Create list of extractors
        List<Extractor> extractors = new ArrayList<>();

        //Wczytaj config
        String line;
        List<String> tags = new ArrayList<>();

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("src/main/resources/config.txt"));
            while ((line = bufferedReader.readLine()) != null) {
                String[] lines = line.split(" = ");
                for (String l : lines) {
                    switch (l) {
                        case "tagClass":
                            tagClass = lines[1];
                            break;
                        case "folderPath":
                            folderPath = lines[1];
                            break;
                        case "articlesToReadCount":
                            articlesToReadCount = Integer.parseInt(lines[1]);
                            break;
                        case "k":
                            k = Integer.valueOf(lines[1]);
                            break;
                        case "fractionOfUncoveredForEachTag":
                            fractionOfUncoveredForEachTag = Float.parseFloat(lines[1]);
                            break;
                        case "tags":
                            String[] tg = lines[1].split(", ");
                            tags = Arrays.asList(tg);
                            break;
                        case "numberOfElementsPerTag":
                            numberOfElementsPerTag = Integer.valueOf(lines[1]);
                            break;
                        case "trainToTestRatio":
                            trainToTestRatio = Float.valueOf(lines[1]);
                            break;
                        case "distanceKNN":
                            switch (lines[1]) {
                                case ("chebyshev"):
                                    distance = new ChebyshevDistance();
                                    break;
                                case ("euclidean"):
                                    distance = new EuclideanDistance();
                                    break;
                                case ("manhattan"):
                                    distance = new ManhattanDistance();
                                    break;
                            }
                            break;
                        case "wordSimilarity":
                            switch (lines[1]) {
                                case ("generalizedNGrams"):
                                    wordComparator = new GeneralizedNGrams();
                                    break;
                                case ("NGrams"):
                                    wordComparator = new NGrams();
                                    break;
                            }
                            break;
                        case "extractors":
                            switch (lines[1]) {
                                case "1":
                                    extractors.add(new ExtractorRemoveStopWords());
                                    extractors.add(new ExtractorRemoveNumbers());
                                    extractors.add(new ExtractorRemoveRarelyOccuring(3));
                                    //extractors.add(new ExtractorTFIDF());
                                    extractors.add(new ExtractorFirstWords());
                                    break;
                                case "2":
                                    extractors.add(new ExtractorTFIDF());
                                    //extractors.add(new ExtractorFirstWords());
                                    extractors.add(new ExtractorRemoveNumbers());
                                    break;
                            }
                        default:
                            break;
                    }
                }

            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Read all articles
        List<Object> allArticles = ReadAll.read(folderPath, tagClass, articlesToReadCount);
        Collections.shuffle(allArticles);

        //Remove all articles that have tags count other than 1 and if they have 1 check if it is in the list of tags
        List<Object> toRemove = new ArrayList<>();
        for (Object article : allArticles) {
            if (((Article) article).getTags().size() != 1) {
                toRemove.add(article);
            } else {
                if (!tags.contains(((Article) article).getTags().get(0))) {
                    toRemove.add(article);
                }
            }
        }
        allArticles.removeAll(toRemove);
        System.out.println(allArticles.size());

        List<Object> trainArticles = new ArrayList<>();
        List<Object> testArticles = new ArrayList<>();
        for (int i = 0; i < allArticles.size(); i++) {
            if (((float) i) / ((float) allArticles.size()) < trainToTestRatio) {
                trainArticles.add(allArticles.get(i));
            } else {
                testArticles.add(allArticles.get(i));
            }
        }

        Map<String, List<Object>> trainArticlesByTags = getElementsForTags(trainArticles, tags);
        Map<String, List<Object>> testArticlesByTags = getElementsForTags(testArticles, tags);

        //Check how many elements are there for each tag
        for (String o : testArticlesByTags.keySet()) {
            System.out.println(o + "  " + testArticlesByTags.get(o).size());
        }

        //Generate vector using extractors
        List<List<Object>> vector = MainExtractor.createVector(trainArticles, trainArticlesByTags, tags, numberOfElementsPerTag, extractors);

        //Generate vectors for articles in test set
        List<List<Float>> testVectors = new LinkedList<>();
        VectorForElement vectorForElement = new VectorForElement();
        for (Object o : testArticles) {
            testVectors.add(vectorForElement.generateVector(vector, o, wordComparator));
        }

        //Use knn to classify articles
        knnNetwork network = new knnNetwork(vector.size(), tags);
        for (int i = 0; i < testVectors.size(); i++) {
            network.addVector(testArticles.get(i), testVectors.get(i));
        }

        Map<Object, String> classifiedArticles = network.classify(k, fractionOfUncoveredForEachTag, distance);
        List<String> correctlabels = new ArrayList<>();
        List<String> resultlabels = new ArrayList<>();

        //Show results of classification
        for (Object o : classifiedArticles.keySet()) {
            correctlabels.add(((Article) o).getTags().get(0));
            resultlabels.add(classifiedArticles.get(o));
        }
        System.out.println("Multi-Class Pecision: " + MultiClassPrecision.calculate(tags, correctlabels, resultlabels));
        System.out.println();
        PrecisionAndRecallForTags.show(tags, correctlabels, resultlabels);
        System.out.println();
        ConfusionMatrix.calculate(tags, correctlabels, resultlabels);
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
