import java.io.*;
import java.util.*;

// Article class
class Article {
    private int articleId;
    private String title;
    private String content;
    private int wordCount;

    public Article(int articleId, String title, String content) {
        this.articleId = articleId;
        this.title = title;
        this.content = content;
        this.wordCount = countWords(content);
    }

    private int countWords(String text) {
        if (text == null || text.trim().isEmpty()) {
            return 0;
        }
        return text.trim().split("\\s+").length;
    }

    public int getArticleId() {
        return articleId;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public int getWordCount() {
        return wordCount;
    }
}


// Article Repository
class ArticleRepository {
    private List<Article> articles;

    public ArticleRepository() {
        articles = new ArrayList<>();
    }

    public void addArticle(Article article) {
        articles.add(article);
    }

    public List<Article> search(String keyword) {
        List<Article> result = new ArrayList<>();

        keyword = keyword.toLowerCase();

        for (Article article : articles) {

            String title = article.getTitle().toLowerCase();
            String content = article.getContent().toLowerCase();

            if (title.contains(keyword) || content.contains(keyword)) {
                result.add(article);
            }
        }

        return result;
    }
}


// Main Class
public class CorpusLoader {

    // Read article from file
    public static Article readArticle(File file, int articleId) {

        String title;
        StringBuilder content = new StringBuilder();

        try {
            BufferedReader reader =
                    new BufferedReader(new FileReader(file));

            // First line is the title
            title = reader.readLine();

            // Remaining lines are content
            String line;

            while ((line = reader.readLine()) != null) {
                content.append(line).append(" ");
            }

            reader.close();

            return new Article(
                    articleId,
                    title,
                    content.toString().trim()
            );

        } catch (IOException e) {

            System.out.println(
                    "Error reading file: " + file.getName()
            );

            return null;
        }
    }


    public static void main(String[] args) {

        System.out.println("=====================================");
        System.out.println("       TEXTHACK QUERY PROCESSOR");
        System.out.println("=====================================");

        // Create repository
        ArticleRepository repository =
                new ArticleRepository();

        // Corpus folder
        File corpusFolder = new File("Corpus");

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

        // Article IDs start from 101
        int articleId = 101;

        // Load articles
        for (File file : files) {

            Article article =
                    readArticle(file, articleId);

            if (article != null) {

                repository.addArticle(article);
                articleId++;
            }
        }

        // Get keyword
        Scanner scanner = new Scanner(System.in);
System.out.print("Enter keyword to search : ");

        String keyword = scanner.nextLine().trim();

        // Search
        List<Article> results =
                repository.search(keyword);

        // Display results
        System.out.println();
        System.out.println("Matching Articles");

        System.out.println(
                "----------------------------------------"
        );

        if (results.isEmpty()) {

            System.out.println(
                    "No matching articles found."
            );

        } else {

            for (Article article : results) {

                System.out.println(
                        "Article ID : " +
                        article.getArticleId()
                );

                System.out.println(
                        "Title : " +
                        article.getTitle()
                );

                System.out.println(
                        "Word Count : " +
                        article.getWordCount()
                );

                System.out.println("Content :");

                System.out.println(
                        article.getContent()
                );

                System.out.println(
                        "----------------------------------------"
                );
            }
        }

        scanner.close();
    }
}