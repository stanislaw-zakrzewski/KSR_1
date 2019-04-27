package data_management;

import data_management.Article;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Read {
    public static List<Article> readTag(String fileName, String tag) {
        List<String> possibleTags = new ArrayList<>();
        List<String> possibleTagsEnds = new ArrayList<>();
        possibleTags.add("PLACES");
        possibleTagsEnds.add("/PLACES");
        possibleTags.add("TOPICS");
        possibleTagsEnds.add("/TOPICS");
        possibleTags.add("REVIEWS");
        possibleTagsEnds.add("/REVIEWS");


        String line;
        FileReader fileReader = null;
        List<String> tags = new ArrayList<>();
        List<StringBuilder> tagsValues = new ArrayList<>();
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
                    if (readableBody) {
                        bod.append("\n");
                    }
                    while (i < line.length()) {
                        if (readableTag) {
                            if (line.charAt(i) == '<' && line.charAt(i + 1) == '/' && line.charAt(i + 2) == 'D') {
                                tagsValues.get(tagsValues.size() - 1).append(" ");
                            } else if (line.charAt(i) != '<') {
                                tagsValues.get(tagsValues.size() - 1).append(line.charAt(i));
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
                            if (possibleTags.contains(tg.toString())) {
                                readableTag = true;
                                tagsValues.add(new StringBuilder());
                                tags.add(tg.toString());
                            }
                            if (possibleTagsEnds.contains(tg.toString())) {
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
                                Map<String, List<String>> tagsAndValues = new HashMap<>();
                                for (int j = 0; j < tags.size(); j++) {
                                    tagsAndValues.put(tags.get(j), Arrays.asList(tagsValues.get(j).toString().split(" ")));
                                }
                                articles.add(new Article(tagsAndValues, tit.toString(), bod.toString()));
                                tags = new ArrayList<>();
                                tagsValues = new ArrayList<>();
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
