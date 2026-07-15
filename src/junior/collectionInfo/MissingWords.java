package junior.collectionInfo;

import java.util.ArrayList;
import java.util.List;

/*
    刷題: 兩個字串比對, 找出遺失的字

    ex-1:
    s = "I am using HackerRank to improve programming";
    t = "am HackerRank to improve";
    result = ["I", "using", "programming"];

    ex-2:
    s = "Python is an easy to learn powerful programming langurage";
    t = "programming Python elegant";
    result = ["Python", "is", "an", easy, "to", "learn", "powerful", "langurage"];
*/
public class MissingWords {
    public static List<String> missingWords(String s, String t) {
        String[] sourceWords = s.split("\\s+");
        String[] targetWords = t.split("\\s+");

        List<String> missingWords = new ArrayList<>();

        int j = 0;

        for (int i = 0; i < sourceWords.length; i++) {
            if (
                j < targetWords.length
                && sourceWords[i].equals(targetWords[j])
            ) {
                j++;
            } else {
                missingWords.add(sourceWords[i]);
            }
        }

        return missingWords;
    }

    public static void main(String[] args) {
        String s = "Python is an easy to learn powerful programming langurage";
        String t = "programming Python elegant";

        List<String> result = missingWords(s, t);

        for (String word : result) {
            System.out.println(word);
        }
    }
}
