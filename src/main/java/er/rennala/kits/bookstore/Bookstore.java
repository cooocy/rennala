package er.rennala.kits.bookstore;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import er.rennala.domain.RennalaException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

/**
 * Utility class for interacting with the Bookstore.
 */
@Slf4j
public class Bookstore {

    private final StorageEngine storageEngine;

    public Bookstore(StorageEngine storageEngine) {
        this.storageEngine = storageEngine;
        log.info("----------------------------------------------------------------------------------");
        log.info("[Bookstore] StorageEngine: {}", storageEngine.getClass().getSimpleName());
        log.info("----------------------------------------------------------------------------------");
    }

    /**
     * Pulls the content of a file from the Bookstore using the provided fullName.
     *
     * @param fullName            the full name of the file to pull, e.g., "link-melina/app-online.yaml"
     * @param timeoutMilliseconds the timeout for the HTTP request in milliseconds
     * @return the content of the file as a String
     * @throws RennalaException if the content of the file is empty or if the HTTP request fails
     */
    public String pullText(String fullName, int timeoutMilliseconds) {
        String url = storageEngine.assembleUrl(fullName);
        Map<String, String> headers = storageEngine.assembleHeaders();

        try (HttpResponse httpResponse = HttpRequest.get(url).headerMap(headers, true).timeout(timeoutMilliseconds).execute()) {
            if (httpResponse.getStatus() != 200) {
                throw new RennalaException("[Bookstore] Pull Failed. HTTP Code: " + httpResponse.getStatus());
            }
            String body = httpResponse.body();
            if (StrUtil.isBlank(body)) {
                throw new RennalaException("[Bookstore] Pull Failed. Response Body is empty");
            }
            return storageEngine.parseContent(body);
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
    public String writeTextToFile(String fileName, String content) {
        try {
            Path projectRoot = Path.of("").toAbsolutePath();
            Path filePath = projectRoot.resolve(fileName);
            Files.writeString(filePath, content);
            String absolutePath = filePath.toAbsolutePath().toString();
            log.info("[Bookstore] Write To File Success. File Path: {}", absolutePath);
            return absolutePath;
        } catch (IOException e) {
            throw new RennalaException("[Bookstore] Write To File Failed. Error: " + e.getMessage());
        }
    }

    /**
     * Downloads a configuration file from the Bookstore and writes it to the project's root directory.
     * This method performs the following steps:
     * 1. Reads the `CONFIGURATION_TAIL` environment variable to determine if the operation should proceed.
     * - If `CONFIGURATION_TAIL` is empty, the method logs a message and exits without performing any action.
     * 2. Replaces the placeholder `:tail` in the `remoteName` parameter with the value of `CONFIGURATION_TAIL`.
     * 3. Extracts the file name from the `remoteName` parameter to determine the local file name.
     * 4. Uses the `pullText` method to fetch the content of the configuration file from the Bookstore.
     * 5. Writes the fetched content to a file in the project's root directory using the `writeTextToFile` method.
     * 6. Logs the success of the operation, including the remote file name and the local file path.
     *
     * @param remoteName          the full name of the remote file to pull, e.g., "link-melina/application-:tail.yaml"
     *                            (can include the `:tail` placeholder to be replaced by `CONFIGURATION_TAIL`)
     * @param timeoutMilliseconds the timeout for the HTTP request in milliseconds
     */
    public void downloadConfiguration(String remoteName, int timeoutMilliseconds) {
        String tail = System.getenv().getOrDefault("CONFIGURATION_TAIL", "");
        if (StrUtil.isEmpty(tail)) {
            log.info("----------------------------------------------------------------------------------");
            log.info("[Bookstore] The CONFIGURATION_TAIL is empty. No Pull Configuration From Bookstore.");
            log.info("----------------------------------------------------------------------------------");
            return;
        }

        // Pull And then Write to project root.
        remoteName = remoteName.replace(":tail", tail);
        String localName = remoteName.lastIndexOf("/") == -1 ? remoteName : remoteName.substring(remoteName.lastIndexOf("/") + 1);
        String configurationBody = pullText(remoteName, timeoutMilliseconds);
        String localFullPath = writeTextToFile(localName, configurationBody);

        log.info("----------------------------------------------------------------------------------");
        log.info("[Bookstore] Pull Configuration From Bookstore OK.");
        log.info("            RemoteName: {}", remoteName);
        log.info("            LocalFullPath: {}", localFullPath);
        log.info("----------------------------------------------------------------------------------");
    }

}
