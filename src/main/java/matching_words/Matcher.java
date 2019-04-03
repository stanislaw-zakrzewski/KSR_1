package matching_words;

import java.util.List;

public interface Matcher {
    List<Float> generateVector(List<Object> vector, Object element);
}
