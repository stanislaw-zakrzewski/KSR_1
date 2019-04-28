package data_management;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreEntityMention;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import java.util.List;
import java.util.Properties;

public class DataProcess {
    private static final String reutersPath = "src/main/resources/sgm/";


    public static void main(String[] args) {
        ConfigReader config = new ConfigReader("src/main/resources/config.txt");
        List<Object> allArticles = ReadAll.read(reutersPath, config.getTagClass(), config.getArticlesToReadCount());
        String text;

        Properties props = new Properties();
        // set the list of annotators to run
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner");
        // set a property for an annotator, in this case the coref annotator is being set to use the neural algorithm
        props.setProperty("coref.algorithm", "neural");
        // build pipeline
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);



        int j = 0;
        for (Object o : allArticles) {
            Article article = (Article) o;
            text = article.getContent();
            CoreDocument document = new CoreDocument(text);
            // annnotate the document
            pipeline.annotate(document);
            // examples
            for (CoreLabel label : document.tokens()) {
                article.getLemmas().add(label.value());
            }
            for (CoreSentence sentence : document.sentences()) {
                for (int i = 0; i < sentence.tokens().size(); i++) {
                    if (!sentence.nerTags().get(i).equals("O")) {
                        article.getNerTags().put(sentence.tokens().get(i).value(), sentence.nerTags().get(i));
                    }
                }
                for (CoreEntityMention entityMention : sentence.entityMentions()) {
                    article.getEntityMentions().add(entityMention.text());
                }
            }
            System.out.println(j++);
        }

        WriteObjects.write(allArticles);
    }
}
