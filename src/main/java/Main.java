import parsing.Article;
import parsing.Read;

import java.util.List;

import extracting.NElementsSelector;
import extracting.feature_extractors.Stopwords;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Map<String, Float> oko = new HashMap<>();
        oko.put("a",1f);
        oko.put("b",2f);
        oko.put("c",3f);
        oko.put("d",4f);
        oko.put("e",1f);
        oko.put("f",3f);
        oko.put("g",2f);
        List<String> e = NElementsSelector.selectN(oko,3);
        System.out.println(Stopwords.getInstance().getListOfStopwords().stream().anyMatch(t -> t.equals("the")));
        e.forEach(System.out::println);
        System.out.println("Hello");
        Read read = new Read();

        List<Article> lst = read.readTag("src\\main\\resources\\sgm\\reut2-000.sgm", "PLACES");

        for (Article art:
             lst) {
            art.printAll();
        }
    }
}
