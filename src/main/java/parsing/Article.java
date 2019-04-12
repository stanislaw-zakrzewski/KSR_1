package parsing;

import extracting.feature_extractors.StanfordLemmatizer;

import java.util.List;

public class Article {
    private List<String> tags;
    private String title;
    private String content;
    private List<String> lemmatizedWords;

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
