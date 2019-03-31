import parsing.Article;
import parsing.Read;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello");
        Read read = new Read();

        List<Article> lst = read.readTag("C:\\Users\\macie\\OneDrive\\Studia\\Rok III\\Semestr VI\\Komputerowe systemy rozpoznawania\\zadanie1\\src\\main\\resources\\sgm\\reut2-000.sgm", "PLACES");

        for (Article art:
             lst) {
            art.printAll();
        }
    }
}
