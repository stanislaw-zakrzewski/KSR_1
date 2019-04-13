package data_management;

import data_management.Article;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Read {
    public static List<Article> readTag(String fileName, String tag) {
        String line;
        FileReader fileReader = null;
        StringBuilder txt = new StringBuilder();
        StringBuilder tit = new StringBuilder();
        StringBuilder bod = new StringBuilder();
        List<Article> articles = new ArrayList<Article>();
        boolean readableTag = false;
        boolean readableTitle = false;
        boolean readableBody = false;
        StringBuilder tg = new StringBuilder();

        try {
            fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while ((line = bufferedReader.readLine()) != null) {
                if (line.length() != 0) {
                    int i = 0;
                    while (i < line.length()) {
                        if (readableTag) {
                            if (line.charAt(i) == '<' && line.charAt(i + 1) == '/' && line.charAt(i + 2) == 'D') {
                                txt.append(" ");
                            } else if (line.charAt(i) != '<') {
                                txt.append(line.charAt(i));
                            }
                        }
                        if (readableTitle) {
                            if (line.charAt(i) == '<' && line.charAt(i + 1) == '/' && line.charAt(i + 2) == 'T') {
                                tit.append(" ");
                            } else if (line.charAt(i) != '<') {
                                tit.append(line.charAt(i));
                            }
                        }
                        if (readableBody) {
                            if (line.charAt(i) == '<' && line.charAt(i + 1) == '/' && line.charAt(i + 2) == 'B') {
                                bod.append(" ");
                            } else if (line.charAt(i) != '<') {
                                bod.append(line.charAt(i));
                            }
                        }
                        if (line.charAt(i) == '<') {
                            i++;
                            while (line.charAt(i) != '>') {
                                tg.append(line.charAt(i));
                                i++;
                            }
                            if (tg.toString().equals(tag)) {
                                readableTag = true;
                            }
                            if (tg.toString().equals(("/" + tag))) {
                                readableTag = false;
                            }
                            if (tg.toString().equals("TITLE")) {
                                readableTitle = true;
                            }
                            if (tg.toString().equals(("/" + "TITLE"))) {
                                readableTitle = false;
                            }
                            if (tg.toString().equals("BODY")) {
                                readableBody = true;
                            }
                            if (tg.toString().equals(("/" + "BODY"))) {
                                readableBody = false;
                            }
                            if (tg.toString().equals("/" + "REUTERS")) {
                                List<String> retList = new ArrayList<String>(Arrays.asList(txt.toString().split(" ")));
                                articles.add(new Article(retList, tit.toString(), bod.toString()));
                                txt = new StringBuilder();
                                tit = new StringBuilder();
                                bod = new StringBuilder();
                            }
                            tg = new StringBuilder();
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
