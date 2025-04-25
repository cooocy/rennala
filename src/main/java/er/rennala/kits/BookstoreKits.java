package er.rennala.kits;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import er.rennala.domain.RennalaException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Utility class for interacting with the Bookstore.
 */
public class BookstoreKits {

    private BookstoreKits() {
    }

    /**
     * Pulls the content of a file from the Bookstore using the provided fullName.
     *
     * @param fullName the full name of the file to pull, e.g., "link-melina/app-online.yaml"
     * @return the content of the file as a String
     */
    public static String pullText(String fullName) {
        String bookstoreUrl = System.getenv().getOrDefault("BOOKSTORE_URL", "");
        String bookstoreToken = System.getenv().getOrDefault("BOOKSTORE_TOKEN", "");
        if (StrUtil.hasEmpty(bookstoreUrl, bookstoreToken)) {
            throw new RennalaException("[Bookstore] Please set environment variables BOOKSTORE_URL and BOOKSTORE_TOKEN");
        }
        if (StrUtil.isBlank(fullName)) {
            throw new RennalaException("[Bookstore] Please set fullName");
        }
        try (HttpResponse httpResponse = HttpRequest.get(bookstoreUrl + "/" + fullName).header("Authorization", "token " + bookstoreToken).execute()) {
            if (httpResponse.getStatus() != 200) {
                throw new RennalaException("[Bookstore] Pull Failed. HTTP Code: " + httpResponse.getStatus());
            }
            String body = httpResponse.body();
            if (StrUtil.isBlank(body)) {
                throw new RennalaException("[Bookstore] Pull Failed. Response Body is empty");
            }
            return body;
        } catch (Exception e) {
            throw new RennalaException("[Bookstore] Pull Failed. Error: " + e.getMessage());
        }
    }

    /**
     * Writes the given content to a file with the specified name in the project's root directory; if the file exists, will be overwritten.
     * In this case:
     * cd /path/to/your/project
     * java -jar build/libs/your-jar-file.jar
     * x
     * the project's root dir is: /path/to/your/project
     * and the file will be created in /path/to/your/project
     *
     * @param fileName the name of the file to write to
     * @param content  the content to write into the file
     * @return the absolute path of the file.
     * @throws RennalaException if an I/O error occurs during the write operation
     */
    public static String writeTextToFile(String fileName, String content) {
        try {
            Path projectRoot = Path.of("").toAbsolutePath();
            Path filePath = projectRoot.resolve(fileName);
            Files.writeString(filePath, content);
            return filePath.toAbsolutePath().toString();
        } catch (IOException e) {
            throw new RennalaException("[Bookstore] Write To File Failed. Error: " + e.getMessage());
        }
    }

}
