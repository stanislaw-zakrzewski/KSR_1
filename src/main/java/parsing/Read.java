package parsing;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Read {
    public List<Article> readTag(String fileName, String tag) {
        String line;
        FileReader fileReader = null;
        String txt = "";
        String tit = "";
        String bod = "";
        List<Article> articles = new ArrayList<Article>();
        boolean readableTag = false;
        boolean readableTitle = false;
        boolean readableBody = false;
        String tg = "";

        try {
            fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while ((line = bufferedReader.readLine()) != null) {
                if (line.length() != 0) {
                    int i = 0;
                    while (i < line.length()) {
                        if (readableTag == true) {
                            if (line.charAt(i) == '<' && line.charAt(i + 1) == '/' && line.charAt(i + 2) == 'D') {
                                txt += " ";
                            } else if (line.charAt(i) != '<') {
                                txt += line.charAt(i);
                            }
                        }
                        if (readableTitle == true) {
                            if (line.charAt(i) == '<' && line.charAt(i + 1) == '/' && line.charAt(i + 2) == 'T') {
                                tit += " ";
                            } else if (line.charAt(i) != '<') {
                                tit += line.charAt(i);
                            }
                        }
                        if (readableBody == true) {
                            if (line.charAt(i) == '<' && line.charAt(i + 1) == '/' && line.charAt(i + 2) == 'B') {
                                bod += " ";
                            } else if (line.charAt(i) != '<') {
                                bod += line.charAt(i);
                            }
                        }
                        if (line.charAt(i) == '<') {
                            i++;
                            while (line.charAt(i) != '>') {
                                tg += line.charAt(i);
                                i++;
                            }
                            if (tg.equals(tag)) {
                                readableTag = true;
                            }
                            if (tg.equals(("/" + tag))) {
                                readableTag = false;
                            }
                            if (tg.equals("TITLE")) {
                                readableTitle = true;
                            }
                            if (tg.equals(("/" + "TITLE"))) {
                                readableTitle = false;
                            }
                            if (tg.equals("BODY")) {
                                readableBody = true;
                            }
                            if (tg.equals(("/" + "BODY"))) {
                                readableBody = false;
                            }
                            if (tg.equals("/" + "REUTERS")) {
                                List<String> retList = new ArrayList<String>(Arrays.asList(txt.split(" ")));
                                articles.add(new Article(retList, tit, bod));
                                txt = "";
                                tit = "";
                                bod = "";
                            }
                            tg = "";
                        }
                        i++;
                    }
                }
            }

            bufferedReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return articles;
    }

}
