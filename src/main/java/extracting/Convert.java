package extracting;

import parsing.Article;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Convert {
    public static List<String> contentToWords(String text) {
        //TODO implement algorithm to change text into list of words
        List<String> words = new ArrayList<>();
        words.add("no");
        words.add("the");
        words.add("america");
        return words;
    }

    public static Map<String, Float> articlesToVector(List<Article> articles) {
        Map<String, Float> vector = new HashMap<>();
        for(Article article : articles) {
            List<String> wordsInArticles = contentToWords(article.getContent());
            for(String word : wordsInArticles) {
                if(!vector.containsKey(word)) {
                    vector.put(word, 0.f);
                }
            }
        }
        return vector;
    }
}
