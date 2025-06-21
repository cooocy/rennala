package er.rennala.kits.bookstore;

import java.util.Map;

/**
 * The HTTP Storage Engine interface for interacting with a remote storage service.
 */
public interface StorageEngine {

    /**
     * Assembles the URL for accessing a resource based on its full name.
     *
     * @param fullName the full name of the file to pull, e.g., "link-melina/app-online.yaml"
     * @return the complete URL as a String
     */
    String assembleUrl(String fullName);

    /**
     * Assembles the headers required for the HTTP request.
     *
     * @return a Map containing header names and their corresponding values
     */
    Map<String, String> assembleHeaders();

    /**
     * Parses the content of the response body to get the content of the file specified by fullName.
     *
     * @param responseBody the body of the HTTP response as a String
     * @return the parsed content as a String
     */
    String parseContent(String responseBody);

}
