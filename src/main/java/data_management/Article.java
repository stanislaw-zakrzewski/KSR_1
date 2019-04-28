package data_management;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Article implements Serializable {
    private Map<String, List<String>> tags;
    private String title;
    private String content;
    private List<String> lemmas;
    private List<String> entityMentions;
    private Map<String, String> nerTags;

    public Article() {
    }

    public Article(Map<String, List<String>> tags, String title, String content) {
        this.tags = tags;
        this.title = title;
        this.content = content;
        entityMentions = new ArrayList<>();
        lemmas = new ArrayList<>();
        nerTags = new HashMap<>();
    }

    public Article(Map<String, List<String>> tags, String title, String content, List<String> lemmas, List<String> entityMentions, Map<String, String> nerTags) {
        this.tags = tags;
        this.title = title;
        this.content = content;
        this.lemmas = lemmas;
        this.entityMentions = entityMentions;
        this.nerTags = nerTags;
    }

    public Map<String, List<String>> getTags() {
        return tags;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public List<String> getLemmas() {
        return lemmas;
    }

    public List<String> getEntityMentions() {
        return entityMentions;
    }

    public Map<String, String> getNerTags() {
        return nerTags;
    }

    public void setTags(Map<String, List<String>> tags) {
        this.tags = tags;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setLemmas(List<String> lemmas) {
        this.lemmas = lemmas;
    }

    public void setEntityMentions(List<String> entityMentions) {
        this.entityMentions = entityMentions;
    }

    public void setNerTags(Map<String, String> nerTags) {
        this.nerTags = nerTags;
    }
}
