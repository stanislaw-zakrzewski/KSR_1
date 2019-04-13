package data_management;

import knn_classification.calculate_distance.ChebyshevDistance;
import knn_classification.calculate_distance.Distance;
import knn_classification.calculate_distance.EuclideanDistance;
import knn_classification.calculate_distance.ManhattanDistance;
import matching_words.word_comparators.GeneralizedNGrams;
import matching_words.word_comparators.NGrams;
import matching_words.word_comparators.WordComparator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ConfigReader {
    private String folderPath;
    private String tagClass;
    private String extractor;
    private List<String> tags;
    private int articlesToReadCount;
    private int numberOfElementsPerTag;
    private int k;
    private float fractionOfUncoveredForEachTag;
    private float trainToTestRatio;
    private Distance distance;
    private WordComparator wordSimilarity;

    public ConfigReader(String path) {
        try {
            String line;
            BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
            while ((line = bufferedReader.readLine()) != null) {
                String[] keyValue = line.split(" = ");
                switch (keyValue[0]) {
                    //String values
                    case "folderPath":
                        folderPath = keyValue[1];
                        break;
                    case "tagClass":
                        tagClass = keyValue[1];
                        break;
                    case "extractors":
                        extractor = keyValue[1];
                        break;
                    //List<String> values
                    case "tags":
                        tags = Arrays.asList(keyValue[1].split(", "));
                        break;
                    //Integer values
                    case "articlesToReadCount":
                        articlesToReadCount = Integer.parseInt(keyValue[1]);
                        break;
                    case "numberOfElementsPerTag":
                        numberOfElementsPerTag = Integer.parseInt(keyValue[1]);
                        break;
                    case "k":
                        k = Integer.parseInt(keyValue[1]);
                        break;
                    //Float values
                    case "fractionOfUncoveredForEachTag":
                        fractionOfUncoveredForEachTag = Float.parseFloat(keyValue[1]);
                        break;
                    case "trainToTestRatio":
                        trainToTestRatio = Float.parseFloat(keyValue[1]);
                        break;
                    //Distance
                    case "distanceKNN":
                        switch (keyValue[1]) {
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
                    //Word similarity
                    case "wordSimilarity":
                        switch (keyValue[1]) {
                            case ("generalizedNGrams"):
                                wordSimilarity = new GeneralizedNGrams();
                                break;
                            case ("NGrams"):
                                wordSimilarity = new NGrams();
                                break;
                        }
                        break;
                    default:
                        break;
                }
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String getFolderPath() {
        return folderPath;
    }

    public String getTagClass() {
        return tagClass;
    }

    public String getExtractor() {
        return extractor;
    }

    public List<String> getTags() {
        return tags;
    }

    public int getArticlesToReadCount() {
        return articlesToReadCount;
    }

    public int getNumberOfElementsPerTag() {
        return numberOfElementsPerTag;
    }

    public int getK() {
        return k;
    }

    public float getFractionOfUncoveredForEachTag() {
        return fractionOfUncoveredForEachTag;
    }

    public float getTrainToTestRatio() {
        return trainToTestRatio;
    }

    public Distance getDistance() {
        return distance;
    }

    public WordComparator getWordSimilarity() {
        return wordSimilarity;
    }
}
