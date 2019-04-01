package parsing;

import java.util.ArrayList;
import java.util.List;

public class ReadAll {
    public List<Article> readAll(String folderPath, String tag) {
        List<Article> articles = new ArrayList<>();
        Read read = new Read();
        for (int i=0;i<22;i++) {
            if (i < 10) articles.addAll(read.readTag(folderPath+"reut2-00"+Integer.toString(i)+".sgm", tag));
            else articles.addAll(read.readTag(folderPath+"reut2-0"+Integer.toString(i)+".sgm", tag));
        }
        return articles;
    }
}
