import java.io.*;
import java.util.*;

public class PatternSearch {

    // Article class
    static class Article {
        int articleId;
        String title;
        String content;

        Article(int articleId, String title, String content) {
            this.articleId = articleId;
            this.title = title;
            this.content = content;
        }
    }


    // ---------------------------------------------------
    // NAIVE PATTERN MATCHING
    // ---------------------------------------------------
    public static List<Integer> naiveSearch(String text, String pattern) {

        List<Integer> positions = new ArrayList<>();

        text = text.toLowerCase();
        pattern = pattern.toLowerCase();

        int n = text.length();
        int m = pattern.length();

        if (m == 0 || m > n) {
            return positions;
        }

        for (int i = 0; i <= n - m; i++) {

            int j = 0;

            while (j < m &&
                    text.charAt(i + j) == pattern.charAt(j)) {

                j++;
            }

            if (j == m) {
                positions.add(i);
            }
        }

        return positions;
    }


    // ---------------------------------------------------
    // BUILD LPS ARRAY FOR KMP
    // ---------------------------------------------------
    public static int[] buildLPS(String pattern) {

        int m = pattern.length();

        int[] lps = new int[m];

        int length = 0;
        int i = 1;

        while (i < m) {

            if (pattern.charAt(i) == pattern.charAt(length)) {

                length++;
                lps[i] = length;
                i++;

            } else {

                if (length != 0) {

                    length = lps[length - 1];

                } else {

                    lps[i] = 0;
                    i++;
                }
            }
        }

        return lps;
    }


    // ---------------------------------------------------
    // KMP PATTERN MATCHING
    // ---------------------------------------------------
    public static List<Integer> kmpSearch(String text, String pattern) {

        List<Integer> positions = new ArrayList<>();

        text = text.toLowerCase();
        pattern = pattern.toLowerCase();

        int n = text.length();
        int m = pattern.length();

        if (m == 0 || m > n) {
            return positions;
        }

        int[] lps = buildLPS(pattern);

        int i = 0;
        int j = 0;

        while (i < n) {

            if (text.charAt(i) == pattern.charAt(j)) {

                i++;
                j++;

                if (j == m) {

                    positions.add(i - j);

                    j = lps[j - 1];
                }

            } else {

                if (j != 0) {

                    j = lps[j - 1];

                } else {

                    i++;
                }
            }
        }

        return positions;
    }


    // ---------------------------------------------------
    // READ ARTICLE FILE
    // ---------------------------------------------------
    public static Article readArticle(File file, int articleId) {

        String title = "";
        StringBuilder content = new StringBuilder();

        try {

            BufferedReader reader =
                    new BufferedReader(new FileReader(file));

            // First line = title
            title = reader.readLine();

            // Remaining lines = content
            String line;

            while ((line = reader.readLine()) != null) {

                content.append(line).append(" ");
            }

            reader.close();

        } catch (IOException e) {

            System.out.println(
                    "Error reading file: " + file.getName()
            );

            return null;
        }

        return new Article(
                articleId,
                title,
                content.toString().trim()
        );
    }


    // ---------------------------------------------------
    // MAIN METHOD
    // ---------------------------------------------------
    public static void main(String[] args) {

        System.out.println("=====================================");
        System.out.println("       TEXTHACK PATTERN SEARCH");
        System.out.println("=====================================");


        // Create Corpus folder reference
        File corpusFolder = new File("Corpus");


        // Check Corpus folder
        if (!corpusFolder.exists() ||
                !corpusFolder.isDirectory()) {

            System.out.println("Corpus folder not found.");
            return;
        }


        // Get all text files
        File[] files = corpusFolder.listFiles(
                (directory, name) ->
                        name.toLowerCase().endsWith(".txt")
        );


        if (files == null || files.length == 0) {

            System.out.println(
                    "No article files found in Corpus folder."
            );

            return;
        }


        // Sort files
        Arrays.sort(
                files,
                Comparator.comparing(File::getName)
        );


        // Store articles
        List<Article> articles = new ArrayList<>();

        int articleId = 101;


        // Read all articles
        for (File file : files) {

            Article article =
                    readArticle(file, articleId);

            if (article != null) {

                articles.add(article);

                articleId++;
            }
        }


        // Take keyword from user
        Scanner scanner = new Scanner(System.in);

        System.out.print(
                "Enter keyword to search : "
        );

        String keyword = scanner.nextLine().trim();


        if (keyword.isEmpty()) {

            System.out.println(
                    "Keyword cannot be empty."
            );

            scanner.close();
            return;
        }


        // ---------------------------------------------------
        // NAIVE PATTERN MATCHING
        // ---------------------------------------------------

        System.out.println();
        System.out.println(
                "====================================="
        );
        System.out.println(
                "       NAIVE PATTERN MATCHING"
        );
        System.out.println(
                "====================================="
        );


        int totalNaiveOccurrences = 0;


        for (Article article : articles) {

            // Search in title + content
            String text =
                    article.title + " " + article.content;

            List<Integer> positions =
                    naiveSearch(text, keyword);


            if (!positions.isEmpty()) {

                System.out.println(
                        "Article ID : " +
                        article.articleId
                );

                System.out.println(
                        "Title : " +
                        article.title
                );


                for (int position : positions) {

                    System.out.println(
                            "Pattern found at position : " +
                            position
                    );
                }


                System.out.println(
                        "Total occurrences : " +
                        positions.size()
                );

                System.out.println();

                totalNaiveOccurrences +=
                        positions.size();
            }
        }


        if (totalNaiveOccurrences == 0) {

            System.out.println(
                    "No occurrences found."
            );
        }


        // ---------------------------------------------------
        // KMP PATTERN MATCHING
        // ---------------------------------------------------

        System.out.println(
                "====================================="
        );

        System.out.println(
                "          KMP PATTERN MATCHING"
        );

        System.out.println(
                "====================================="
        );


        int totalKMPOccurrences = 0;


        for (Article article : articles) {

            // Search in title + content
            String text =
                    article.title + " " + article.content;

            List<Integer> positions =
                    kmpSearch(text, keyword);


            if (!positions.isEmpty()) {

                System.out.println(
                        "Article ID : " +
                        article.articleId
                );

                System.out.println(
                        "Title : " +
                        article.title
                );


                for (int position : positions) {

                    System.out.println(
                            "Pattern found at position : " +
                            position
                    );
                }


                System.out.println(
                        "Total occurrences : " +
                        positions.size()
                );

                System.out.println();

                totalKMPOccurrences +=
                        positions.size();
            }
        }


        if (totalKMPOccurrences == 0) {

            System.out.println(
                    "No occurrences found."
            );
        }


        // ---------------------------------------------------
        // COMPARISON
        // ---------------------------------------------------

        System.out.println(
                "====================================="
        );

        System.out.println(
                "              COMPARISON"
        );

        System.out.println(
                "====================================="
        );

        System.out.println(
                "Naive total occurrences : " +
                totalNaiveOccurrences
        );

        System.out.println(
                "KMP total occurrences   : " +
                totalKMPOccurrences
        );


        if (totalNaiveOccurrences ==
                totalKMPOccurrences) {

            System.out.println(
                    "Both algorithms produced the same results."
            );

        } else {

            System.out.println(
                    "The results are different."
            );
        }


        scanner.close();
    }
}