package data_management;

import java.util.*;

public class Elements {
    private List<Object> trainElements;
    private Map<String, List<Object>> trainElementsForTags;
    private List<Object> testElements;
    private Map<String, List<Object>> testElementsForTags;
    private List<String> tags;

    public Elements(String classTag, List<String> tags, float trainToTestRatio, String filePath) {
        this.tags = tags;
        List<Object> allElements = ReadObjects.read(filePath);
        Collections.shuffle(allElements);

        //Remove all articles that have tags count other than 1 and if they have 1 check if it is in the list of tags
        List<Object> toRemove = new ArrayList<>();
        for (Object object : allElements) {
            Article article = ((Article) object);
            if (article.getTags().get(classTag).size() != 1) {
                toRemove.add(article);
            } else if (!tags.contains(article.getTags().get(classTag).get(0))) {
                toRemove.add(article);
            }
        }
        allElements.removeAll(toRemove);

        trainElements = new ArrayList<>();
        trainElementsForTags = new HashMap<>();
        testElements = new ArrayList<>();
        testElementsForTags = new HashMap<>();
        for (String tag : tags) {
            trainElementsForTags.put(tag, new ArrayList<>());
            testElementsForTags.put(tag, new ArrayList<>());
        }

        for (int i = 0; i < allElements.size(); i++) {
            String currentTag = ((Article) allElements.get(i)).getTags().get(classTag).get(0);
            if (i < allElements.size() * trainToTestRatio) {
                trainElements.add(allElements.get(i));
                trainElementsForTags.get(currentTag).add(allElements.get(i));
            } else {
                testElements.add(allElements.get(i));
                testElementsForTags.get(currentTag).add(allElements.get(i));
            }
        }
    }

    public List<Object> getTrainElements() {
        return trainElements;
    }

    public List<Object> getTrainElementsForTag(String tag) {
        return trainElementsForTags.get(tag);
    }

    public List<Object> getTestElements() {
        return testElements;
    }

    public List<Object> getTestElementsForTag(String tag) {
        return testElementsForTags.get(tag);
    }

    public List<String> getTags() {
        return tags;
    }
}
