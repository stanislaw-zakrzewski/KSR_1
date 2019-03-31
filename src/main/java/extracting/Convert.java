package extracting;

import parsing.Article;

import java.text.BreakIterator;
import java.util.*;

class Convert {
    private static List<String> contentToWords(String text) {
        List<String> words = new ArrayList<>();
        BreakIterator breakIterator = BreakIterator.getWordInstance();
        breakIterator.setText(text);
        int lastIndex = breakIterator.first();
        while (BreakIterator.DONE != lastIndex) {
            int firstIndex = lastIndex;
            lastIndex = breakIterator.next();
            if (lastIndex != BreakIterator.DONE && Character.isLetterOrDigit(text.charAt(firstIndex))) {
                words.add(text.substring(firstIndex, lastIndex).toLowerCase());
            }
        }

        return words;
    }

    static Map<String, Float> articlesToVector(List<Article> articles) {
        Random random = new Random();
        Map<String, Float> vector = new HashMap<>();
        for(Article article : articles) {
            List<String> wordsInArticles = contentToWords(article.getContent());
            for(String word : wordsInArticles) {
                if(!vector.containsKey(word)) {
                    vector.put(word, random.nextFloat());
                }
            }
        }
        return vector;
    }
}
