import java.io.*;
import java.util.*;

public class RabinKarpSearch {

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
    // RABIN-KARP PATTERN SEARCH
    // ---------------------------------------------------
    public static List<Integer> rabinKarpSearch(
            String text, String pattern) {

        List<Integer> positions = new ArrayList<>();

        text = text.toLowerCase();
        pattern = pattern.toLowerCase();

        int n = text.length();
        int m = pattern.length();

        if (m == 0 || m > n) {
            return positions;
        }

        // Base value
        int base = 256;

        // Prime number for modulo hashing
        int prime = 101;

        int patternHash = 0;
        int textHash = 0;

        int highPower = 1;

        // Calculate base^(m-1) % prime
        for (int i = 0; i < m - 1; i++) {
            highPower = (highPower * base) % prime;
        }

        // Calculate initial hashes
        for (int i = 0; i < m; i++) {

            patternHash =
                    (base * patternHash +
                            pattern.charAt(i)) % prime;

            textHash =
                    (base * textHash +
                            text.charAt(i)) % prime;
        }

        // Slide pattern over text
        for (int i = 0; i <= n - m; i++) {

            // If hash values match
            if (patternHash == textHash) {

                boolean match = true;

                // Verify characters to avoid hash collision
                for (int j = 0; j < m; j++) {

                    if (text.charAt(i + j)
                            != pattern.charAt(j)) {

                        match = false;
                        break;
                    }
                }

                if (match) {
                    positions.add(i);
                }
            }

            // Calculate next window hash
            if (i < n - m) {

                textHash =
                        (base *
                                (textHash -
                                        text.charAt(i)
                                                * highPower)
                                + text.charAt(i + m))
                                % prime;

                // Make hash positive
                if (textHash < 0) {
                    textHash += prime;
                }
            }
        }

        return positions;
    }


    // ---------------------------------------------------
    // READ ARTICLE FROM FILE
    // ---------------------------------------------------
    public static Article readArticle(
            File file, int articleId) {

        String title = "";
        StringBuilder content =
                new StringBuilder();

        try {

            BufferedReader reader =
                    new BufferedReader(
                            new FileReader(file));

            // First line = title
            title = reader.readLine();

            // Remaining lines = content
            String line;

            while ((line = reader.readLine()) != null) {

                content.append(line)
                       .append(" ");
            }

            reader.close();

        } catch (IOException e) {

            System.out.println(
                    "Error reading file: "
                            + file.getName());

            return null;
        }

        return new Article(
                articleId,
                title,
                content.toString().trim());
    }


    // ---------------------------------------------------
    // FIND COMMON PATTERNS BETWEEN TWO ARTICLES
    // ---------------------------------------------------
    public static Set<String> findCommonWords(
            Article article1,
            Article article2) {

        Set<String> words1 =
                new HashSet<>();

        Set<String> commonWords =
                new TreeSet<>();

        String[] firstWords =
                article1.content
                        .toLowerCase()
                        .split("\\W+");

        String[] secondWords =
                article2.content
                        .toLowerCase()
                        .split("\\W+");

        for (String word : firstWords) {

            if (!word.isEmpty()) {
                words1.add(word);
            }
        }

        for (String word : secondWords) {

            if (!word.isEmpty() &&
                    words1.contains(word)) {

                commonWords.add(word);
            }
        }

        return commonWords;
    }


    // ---------------------------------------------------
    // MAIN METHOD
    // ---------------------------------------------------
    public static void main(String[] args) {

        System.out.println(
                "====================================="
        );

        System.out.println(
                "       TEXTHACK RABIN-KARP SEARCH"
        );

        System.out.println(
                "====================================="
        );


        // Corpus folder
        File corpusFolder =
                new File("Corpus");


        // Check Corpus folder
        if (!corpusFolder.exists() ||
                !corpusFolder.isDirectory()) {

            System.out.println(
                    "Corpus folder not found."
            );

            return;
        }


        // Get all text files
        File[] files =
                corpusFolder.listFiles(
                        (directory, name) ->
                                name.toLowerCase()
                                        .endsWith(".txt")
                );


        if (files == null ||
                files.length == 0) {

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
        List<Article> articles =
                new ArrayList<>();


        // Article IDs start from 101
        int articleId = 101;


        // Read all articles
        for (File file : files) {

            Article article =
                    readArticle(
                            file,
                            articleId
                    );

            if (article != null) {

                articles.add(article);

                articleId++;
            }
        }


        // Get keyword from user
        Scanner scanner =
                new Scanner(System.in);

        System.out.print(
                "Enter keyword to search : "
        );

        String keyword =
                scanner.nextLine().trim();


        if (keyword.isEmpty()) {

            System.out.println(
                    "Keyword cannot be empty."
            );

            scanner.close();
            return;
        }


        // ---------------------------------------------------
        // RABIN-KARP SEARCH
        // ---------------------------------------------------

        System.out.println();

        System.out.println(
                "====================================="
        );

        System.out.println(
                "       RABIN-KARP PATTERN SEARCH"
        );

        System.out.println(
                "====================================="
        );


        int totalOccurrences = 0;


        for (Article article : articles) {

            // Search title + content
            String text =
                    article.title +
                    " " +
                    article.content;


            List<Integer> positions =
                    rabinKarpSearch(
                            text,
                            keyword
                    );


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
                            "Pattern found at position : "
                                    + position
                    );
                }


                System.out.println(
                        "Total occurrences : " +
                        positions.size()
                );

                System.out.println(
                        "----------------------------------------"
                );


                totalOccurrences +=
                        positions.size();
            }
        }


        if (totalOccurrences == 0) {

            System.out.println(
                    "No matching pattern found."
            );
        }


        // ---------------------------------------------------
        // DOCUMENT SIMILARITY
        // ---------------------------------------------------

        System.out.println();

        System.out.println(
                "====================================="
        );

        System.out.println(
                "       DOCUMENT SIMILARITY"
        );

        System.out.println(
                "====================================="
        );


        if (articles.size() >= 2) {

            for (int i = 0;
                    i < articles.size() - 1;
                    i++) {

                for (int j = i + 1;
                        j < articles.size();
                        j++) {

                    Set<String> commonWords =
                            findCommonWords(
                                    articles.get(i),
                                    articles.get(j)
                            );


                    System.out.println(
                            "Article " +
                            articles.get(i).articleId +
                            " & Article " +
                            articles.get(j).articleId
                    );


                    if (commonWords.isEmpty()) {

                        System.out.println(
                                "Common patterns : None"
                        );

                    } else {

                        System.out.println(
                                "Common patterns : " +
                                commonWords
                        );
                    }

                    System.out.println(
                            "----------------------------------------"
                    );
                }
            }
        }


        scanner.close();
    }
}