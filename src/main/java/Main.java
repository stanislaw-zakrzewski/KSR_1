import data_management.Article;
import data_management.ConfigReader;
import data_management.ReadAll;
import data_management.StanfordLemmatizer;
import edu.stanford.nlp.coref.data.CorefChain;
import edu.stanford.nlp.ie.util.RelationTriple;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.trees.Tree;
import extracting.MainExtractor;
import extracting.feature_extractors.Extractor;
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

        //Read all articles
        List<Object> allArticles = ReadAll.read(config.getFolderPath(), config.getTagClass(), config.getArticlesToReadCount());
        Collections.shuffle(allArticles);

        //Remove all articles that have tags count other than 1 and if they have 1 check if it is in the list of tags
        List<Object> toRemove = new ArrayList<>();
        for (Object article : allArticles) {
            if (((Article) article).getTags().get("PLACES").size() != 1) {
                toRemove.add(article);
            } else {
                if (!config.getTags().contains(((Article) article).getTags().get("PLACES").get(0))) {
                    toRemove.add(article);
                }
            }
        }

        Set<String> nerTagsSet = new HashSet<>();
        Set<String> entityMentionsSet = new HashSet<>();


        allArticles.removeAll(toRemove);
        System.out.println(allArticles.size());
        String text = ((Article)(allArticles.get(0))).getContent();
        String text2 = "Joe Smith was born in California. " +
                "In 2017, he went to Paris, France in the summer. " +
                "His flight left at 3:00pm on July 10th, 2017. " +
                "After eating some escargot for the first time, Joe said, \"That was delicious!\" " +
                "He sent a postcard to his sister Jane Smith. " +
                "After hearing about Joe's trip, Jane decided she might go to France one day.";
        System.out.println(text);

        Properties props = new Properties();
        // set the list of annotators to run
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner");
        // set a property for an annotator, in this case the coref annotator is being set to use the neural algorithm
        props.setProperty("coref.algorithm", "neural");
        // build pipeline
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        // create a document object

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

        int i = 0;
        System.out.println(trainArticles.size());
        for(Object o : trainArticles) {
            text = ((Article)(o)).getContent();
            CoreDocument document = new CoreDocument(text);
            // annnotate the document
            pipeline.annotate(document);
            // examples
            for(CoreSentence sentence : document.sentences()) {
                nerTagsSet.addAll(sentence.nerTags());
                for(CoreEntityMention entityMention : sentence.entityMentions()) {
                    entityMentionsSet.add(entityMention.text());
                }

            }
            System.out.println(++i);
        }
        System.out.println();
        for(String s : nerTagsSet) {
            System.out.print(s + "   ");
        }
        System.out.println();
        for(String s : entityMentionsSet) {
            System.out.print(s + "   ");
        }

        /*
        CoreDocument document = new CoreDocument(text);
        // annnotate the document
        pipeline.annotate(document);
        // examples

        // 10th token of the document
        CoreLabel token = document.tokens().get(10);
        System.out.println("Example: token");
        System.out.println(token);
        System.out.println();

        // text of the first sentence
        String sentenceText = document.sentences().get(0).text();
        System.out.println("Example: sentence");
        System.out.println(sentenceText);
        System.out.println();

        // second sentence
        CoreSentence sentence = document.sentences().get(1);

        // list of the part-of-speech tags for the second sentence
        List<String> posTags = sentence.posTags();
        System.out.println("Example: pos tags");
        System.out.println(posTags);
        System.out.println();

        // list of the ner tags for the second sentence
        List<String> nerTags = sentence.nerTags();
        System.out.println("Example: ner tags");
        System.out.println(nerTags);
        System.out.println();

        // constituency parse for the second sentence
        Tree constituencyParse = sentence.constituencyParse();
        System.out.println("Example: constituency parse");
        System.out.println(constituencyParse);
        System.out.println();


        // entity mentions in the second sentence
        List<CoreEntityMention> entityMentions = sentence.entityMentions();
        System.out.println("Example: entity mentions");
        System.out.println(entityMentions);
        System.out.println();

        // coreference between entity mentions
        CoreEntityMention originalEntityMention = document.sentences().get(3).entityMentions().get(1);
        System.out.println("Example: original entity mention");
        System.out.println(originalEntityMention);
        System.out.println("Example: canonical entity mention");
        System.out.println(originalEntityMention.canonicalEntityMention().get());
        System.out.println();

        // get document wide coref info
        Map<Integer, CorefChain> corefChains = document.corefChains();
        System.out.println("Example: coref chains for document");
        System.out.println(corefChains);
        System.out.println();





        /*
        Map<String, List<Object>> trainArticlesByTags = getElementsForTags(trainArticles, config.getTags());
        Map<String, List<Object>> testArticlesByTags = getElementsForTags(testArticles, config.getTags());

        //Check how many elements are there for each tag
        for (String o : testArticlesByTags.keySet()) {
            System.out.println(o + "  " + testArticlesByTags.get(o).size());
        }

        List<List<Float>> testVectors = new LinkedList<>();
        knnNetwork network = null;
        int ext = Integer.parseInt(config.getExtractor());
        if (ext == 2) {
            for (Object o : testArticles) {
                testVectors.add(SemioticExtractor.getInstance().calculateVector(o));
            }
            network = new knnNetwork(11, config.getTags());
        } else if(ext == 4) {
            for (Object o : testArticles) {
                testVectors.add(PoemExtractor.getInstance().calculateVector(o));
            }
            network = new knnNetwork(6, config.getTags());
        } else {
            List<Extractor> extractors = new ArrayList<>();
            extractors.add(new ExtractorRemoveStopWords());
            extractors.add(new ExtractorRemoveNumbers());
            if (ext == 1) {
                //extractors.add(new ExtractorRemoveFrequentOccurences(0.2f));
                //extractors.add(new ExtractorTFIDF());
                extractors.add(new ExtractorFirstWords(10));
                //extractors.add(new ExtractorTFIDF());
            } else {
                extractors.add(new ExtractorOurMethod());
                //extractors.add(new ExtractorRemoveFrequentOccurences(0.2f));
                extractors.add(new ExtractorFirstWords(10));
            }
            List<Object> vector = MainExtractor.createVector(trainArticles, trainArticlesByTags, config.getTags(), config.getNumberOfElementsPerTag(), extractors);

            VectorForElement vectorForElement = new VectorForElement();
            if (ext == 1) {
                for (Object o : testArticles) {
                    testVectors.add(vectorForElement.generateVector(vector, o, config.getWordSimilarity()));
                }
            } else {
                WordComparator our = new OurComparator();
                for (Object o : testArticles) {
                    testVectors.add(vectorForElement.generateVector(vector, o, our));
                }
            }

            network = new knnNetwork(vector.size(), config.getTags());
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
        float acc = 0;
        for(String tag : config.getTags()) {
            System.out.println(Accuraccy.calculate(tag, correctLabels, resultLabels));
            acc +=Accuraccy.calculate(tag, correctLabels, resultLabels);
        }
        System.out.println("Accuracy: " + acc/config.getTags().size());
        System.out.println("Cały program: " + stopwatch.getTime());
        */
    }

    private static Map<String, List<Object>> getElementsForTags(List<Object> elements, List<String> tags) {
        Map<String, List<Object>> elementsForTags = new HashMap<>();
        tags.forEach(tag -> elementsForTags.put(tag, new ArrayList<>()));
        elements.forEach(article -> {
                    List<String> tagsForArticle = ((Article) article).getTags().get("PLACES");
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
