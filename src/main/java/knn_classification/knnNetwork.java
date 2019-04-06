package knn_classification;

import extracting.NElementsSelector;
import knn_classification.calculate_distance.Distance;
import parsing.Article;

import java.util.*;

public class knnNetwork {
    private Map<Object, List<Float>> vectors;
    private Map<Object, String> answer;
    private final int vectorSize;
    private final List<String> tags;
    private Map<String, Integer> tagsCount;


    public knnNetwork(int vectorSize, List<String> tags) {
        vectors = new HashMap<>();
        this.vectorSize = vectorSize;
        this.tags = tags;
        answer = new HashMap<>();
        tagsCount = new HashMap<>();
        for(String tag : tags) {
            tagsCount.put(tag, 0);
        }
    }

    public void addVector(Object objectToAdd, List<Float> vectorToAdd) {
        if (vectorToAdd.size() == vectorSize) {
            vectors.put(objectToAdd, vectorToAdd);
        }
        String tag = ((Article)objectToAdd).getTags().get(0);
        tagsCount.replace(tag, tagsCount.get(tag) + 1);
    }

    public Map<Object, String> classify(int k, float uncoveredLabelsPercent, Distance distance) {
        vectors.keySet().forEach(key -> answer.put(key, ""));

        uncoverNLabels(uncoveredLabelsPercent);

        for (Object o : answer.keySet()) {
            if (answer.get(o).equals("")) {
                answer.replace(o, getLabel(o, distance, k));
            }
        }

        return answer;
    }

    private void uncoverNLabels(float n) {
        Map<String, Integer> uncoveredLabelsCount = new HashMap<>();
        tags.forEach(tag -> uncoveredLabelsCount.put(tag, (int)(tagsCount.get(tag) * n)));
        for (Object o : vectors.keySet()) {
            String tag = ((Article) o).getTags().get(0);
            if (uncoveredLabelsCount.containsKey(tag)) {
                if (uncoveredLabelsCount.get(tag) == 1) {
                    uncoveredLabelsCount.remove(tag);
                } else {
                    uncoveredLabelsCount.replace(tag, uncoveredLabelsCount.get(tag) - 1);
                }
                answer.replace(o, tag);
            }
        }
    }

    private String getLabel(Object object, Distance distance, int k) {
        if (!answer.get(object).equals("")) {
            return answer.get(object);
        }

        Map<Object, Float> distances = new HashMap<>();


        for (Object o : vectors.keySet()) {
            if (!answer.get(o).equals("")) {
                distances.put(o, distance.calculate(vectors.get(object), vectors.get(o)));
            }
        }

        int w = distances.size() -k;
        if (w <=0) w = 1;
        List<Object> toRemove = NElementsSelector.selectN(distances, w);
        for (Object rm : toRemove) {
            distances.remove(rm);
        }

        Map<String, List<Float>> kElements = new HashMap<>();
        for (String tag : tags) {
            kElements.put(tag, new ArrayList<>());
        }
        for (Object o : distances.keySet()) {
            kElements.get(answer.get(o)).add(distances.get(o));
        }

        int max = 0;
        for (String key : kElements.keySet()) {
            if (kElements.get(key).size() > max) {
                max = kElements.get(key).size();
            }
        }

        toRemove = new ArrayList<>();
        for (String key : kElements.keySet()) {
            if (kElements.get(key).size() < max) {
                toRemove.add(key);
            }
        }
        for (Object rm : toRemove) {
            kElements.remove(rm);
        }

        if (kElements.size() == 1) {
            return (String) kElements.keySet().toArray()[0];
        } else {
            Map<String, Float> closest = new HashMap<>();
            for (Object key : kElements.keySet()) {
                closest.put((String) key, sumList(kElements.get(key)));
            }
            String min = (String)(closest.keySet().toArray()[0]);
            for (String key : closest.keySet()) {
                if (closest.get(key) < closest.get(min)) {
                    min = key;
                }
            }
            return min;
        }
    }

    private Float sumList(List<Float> list) {
        float ret = 0;
        for (Float f : list) {
            ret += f;
        }
        return ret;
    }
}
