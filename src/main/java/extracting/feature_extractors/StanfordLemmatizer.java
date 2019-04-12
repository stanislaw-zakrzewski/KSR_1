package extracting.feature_extractors;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.CoreNLPProtos;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.simple.Sentence;
import edu.stanford.nlp.util.CoreMap;

import java.util.*;

public class StanfordLemmatizer {
    private static StanfordLemmatizer instance;
    private static Map<String, String> zmiana;

    public static StanfordLemmatizer getInstance() {
        if (instance == null) {
            instance = new StanfordLemmatizer();
            zmiana = new HashMap<>();
        }
        return instance;
    }

    protected StanfordCoreNLP pipeline;

    private StanfordLemmatizer() {
        // Create StanfordCoreNLP object properties, with POS tagging
        // (required for lemmatization), and lemmatization
        Properties props;
        props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma");

        // StanfordCoreNLP loads a lot of models, so you probably
        // only want to do this once per execution
        this.pipeline = new StanfordCoreNLP(props);
    }

    public List<String> lemmatize(String documentText)
    {
        List<String> lemmas = new LinkedList<>();

        String[] words = documentText.split("\\W+");
        for(String word : words) {
            if(!word.equals("")) {
                if (!zmiana.containsKey(word)) {
                    Sentence sentence = new Sentence(word);
                    zmiana.put(word, sentence.lemma(0));
                }
                lemmas.add(zmiana.get(word));
            }
        }
        return  lemmas;
        /*



        // create an empty Annotation just with the given text
        Annotation document = new Annotation(documentText);

        // run all Annotators on this text
        this.pipeline.annotate(document);

        // Iterate over all of the sentences found
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
        for(CoreMap sentence: sentences) {
            // Iterate over all tokens in a sentence
            for (CoreLabel token: sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                if(!zmiana.containsKey(token.value())) {
                    zmiana.put(token.value(), token.get(CoreAnnotations.LemmaAnnotation.class));
                }
                // Retrieve and add the lemma for each word into the list of lemmas
                lemmas.add(zmiana.get(token.value()));
                //lemmas.add(token.get(CoreAnnotations.LemmaAnnotation.class));
            }
        }

        return lemmas;*/
    }
}