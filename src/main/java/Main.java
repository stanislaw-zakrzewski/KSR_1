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
import results.Precision;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        int k = 0;
        float fractionOfUncoveredForEachTag = 0;
        //List<String> tags = List.of("west-germany", "usa", "france", "uk", "canada", "japan");
        int numberOfElementsPerTag = 0;
        float trainToTestRatio = 0;

        //Wczytaj config
        String line;
        FileReader fileReader = null;
        List<String> tgs = new ArrayList<>();

        try {
            fileReader = new FileReader("src/main/resources/config.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while ((line = bufferedReader.readLine()) != null) {
                //System.out.println(line);
                String[] lines = line.split(" = ");
                for (int d=0;d<lines.length;d++) {
                    System.out.println(d+"  wartosc  "+lines[d]);
                }
                for (String l : lines) {
                    //System.out.println(l);
                    switch (l) {
                        case "k":
                            k = Integer.valueOf(lines[1]);
                            break;
                        case "fractionOfUncoveredForEachTag":
                            fractionOfUncoveredForEachTag = Float.valueOf(lines[1]);
                            break;
                        case "tags":
                            String[] tg = lines[1].split(", ");
                            tgs = Arrays.asList(tg);
                            break;
                        case "numberOfElementsPerTag":
                            numberOfElementsPerTag = Integer.valueOf(lines[1]);
                            break;
                        case "trainToTestRatio":
                            trainToTestRatio = Float.valueOf(lines[1]);
                            break;
                        default:
                            System.out.println("DUPA");
                            break;
                    }
                }

            }
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        final List<String> tags = tgs;

        //Parametry do klasyfikacji (1/3)
        ReadAll readAll = new ReadAll();
        List<Object> allArticles = readAll.readAll("src/main/resources/sgm/", "PLACES");

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

        List<Object> trainArticles = new ArrayList<>();
        List<Object> testArticles = new ArrayList<>();
        for (int i = 0; i < allArticles.size(); i++) {
            if (((float) i) / ((float) allArticles.size()) < trainToTestRatio) {
                trainArticles.add(allArticles.get(i));
            } else {
                testArticles.add(allArticles.get(i));
            }
        }

        Map<String, List<Object>> trainArticlesByTags = getElementsForTags(trainArticles);
        Map<String, List<Object>> testArticlesByTags = getElementsForTags(testArticles);

        //Check how many elements are there for each tag
        for (String o : testArticlesByTags.keySet()) {
            System.out.println(o + "  " + testArticlesByTags.get(o).size());
        }

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
        for (int i = 0; i < testVectors.size(); i++) {
            network.addVector(testArticles.get(i), testVectors.get(i));
        }
        Map<Object, String> classifiedArticles = network.classify(k, 0.1f);
        int i = 0;
        List<String> correctlabels = new ArrayList<>();
        List<String> resultlabels = new ArrayList<>();
        for (Object o : classifiedArticles.keySet()) {
            correctlabels.add(((Article) o).getTags().get(0));
            resultlabels.add(classifiedArticles.get(o));
            System.out.print(++i + "\t");
            System.out.println(((Article) o).getTags().get(0) + "    " + classifiedArticles.get(o));
        }
        System.out.println(Precision.calculate(tags, correctlabels, resultlabels));
    }

    private static Map<String, List<Object>> getElementsForTags(List<Object> elements) {
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
