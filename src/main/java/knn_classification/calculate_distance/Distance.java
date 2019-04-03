package knn_classification.calculate_distance;

import java.util.List;

public interface Distance {
    float calculate(List<Float> vector1, List<Float> vector2);
}
