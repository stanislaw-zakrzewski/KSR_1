package data_management;

import data_management.StanfordLemmatizer;

import java.util.ArrayList;
import java.util.List;

public class Article {
    private List<String> tags;
    private String title;
    private String content;
    private List<String> lemmatizedWords;
    private List<String> ourWords;

    public Article(List<String> tags, String title, String content) {
        this.tags = tags;
        this.title = title;
        this.content = content;
    }

    public List<String> getTags() {
        return tags;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public List<String> getLemmatizedWords() {
        if(lemmatizedWords == null) {
            lemmatizedWords = StanfordLemmatizer.getInstance().lemmatize(content);
        }
        return lemmatizedWords;
    }

    public List<String> getOurWords() {
        if(ourWords == null) {
            ourWords = new ArrayList<>();
            for(String word : getLemmatizedWords()) {
                ourWords.add(Character.toString(word.charAt(0)) + word.charAt(word.length()-1) + word.length());
            }
        }
        return ourWords;
    }

    public void printAll() {
        System.out.println("Article title: " + title);
        System.out.println("Article body: " + content);
        System.out.print("Tags: ");
        for (int i=0;i<tags.size();i++) {
            System.out.println(tags.get(i));
        }
        System.out.println("\n \n");
    }
}
