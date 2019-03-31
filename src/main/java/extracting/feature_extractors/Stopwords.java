package extracting.feature_extractors;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Stopwords {
    private static Stopwords instance;
    private List<String> listOfStopwords;

    private Stopwords() {
        listOfStopwords = new ArrayList<>();
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File("src\\main\\resources\\stopwords.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assert scanner != null;
        while(scanner.hasNext()) {
            listOfStopwords.add(scanner.next());
        }
    }

    public static synchronized Stopwords getInstance() {
        if(instance == null) {
            instance = new Stopwords();
        }

        return instance;
    }

    public List<String> getListOfStopwords() {
        return listOfStopwords;
    }
}
