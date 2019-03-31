import extracting.MainExtractor;
import parsing.Article;
import parsing.Read;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Read read = new Read();

        List<Article> lst = read.readTag("src\\main\\resources\\sgm\\reut2-000.sgm", "PLACES");

        List<String> vector = MainExtractor.createVector(lst, 2);
        for(String word : vector) {
            //System.out.println(word);
        }

        /*for (Article art:
             lst) {
            art.printAll();
        }*/
    }
}
