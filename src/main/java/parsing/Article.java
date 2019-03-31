package parsing;

import java.util.List;

public class Article {
    private List<String> tags;
    private String title;
    private String content;

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
}
