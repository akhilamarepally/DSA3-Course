import java.io.*;
import java.util.*;

class Article {

    int id;
    String title;
    String content;
    int wordCount;

    // Constructor
    public Article(int id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;

        // Count words
        this.wordCount = content.trim().split("\\s+").length;
    }

    // Display article details
    public void display() {
        System.out.println("-------------------------------------------");
        System.out.println("Article ID : " + id);
        System.out.println("Title      : " + title);
        System.out.println("Word Count : " + wordCount);
        System.out.println("Content    : ");
        System.out.println(content);
        System.out.println("-------------------------------------------");
        System.out.println();
    }
}

public class CorpusLoader {

    public static void main(String[] args) {

        // Article Repository
        ArrayList<Article> repository = new ArrayList<>();

        // Five corpus files
        String[] files = {
            "a1.txt",
            "a2.txt",
            "a3.txt",
            "a4.txt",
            "a5.txt"
        };

        int id = 101;
        int totalWords = 0;

        // Read all files
        for (String fileName : files) {

            try {

                // Corpus folder
                File file = new File("corpus/" + fileName);

                BufferedReader br = new BufferedReader(
                    new FileReader(file)
                );

                // Read title
                String title = br.readLine();

                // Skip blank line
                br.readLine();

                // Read article content
                StringBuilder content = new StringBuilder();

                String line;

                while ((line = br.readLine()) != null) {
                    content.append(line).append(" ");
                }

                // Create Article object
                Article article = new Article(
                    id,
                    title,
                    content.toString().trim()
                );

                // Add article to repository
                repository.add(article);

                // Update statistics
                totalWords += article.wordCount;

                id++;

                br.close();

            } catch (IOException e) {

                System.out.println(
                    "Cannot read file : " + fileName
                );
            }
        }

        // Display Repository
        System.out.println("======================================");
        System.out.println("      TEXTHACK ARTICLE REPOSITORY");
        System.out.println("======================================");
        System.out.println();

        for (Article article : repository) {
            article.display();
        }

        // Display Repository Statistics
        System.out.println("Repository Statistics");
        System.out.println("----------------------");
        System.out.println(
            "Total Articles Loaded : " + repository.size()
        );
        System.out.println(
            "Total Words           : " + totalWords
        );
    }
}