package er.rennala.kits.bookstore;

import java.util.Map;

public class GitHubStorageEngine implements StorageEngine {

    private final String baseUrl;

    private final String token;

    public GitHubStorageEngine(String baseUrl, String token) {
        this.baseUrl = baseUrl;
        this.token = token;
    }

    @Override
    public String assembleUrl(String fullName) {
        return baseUrl + "/" + fullName;
    }

    @Override
    public Map<String, String> assembleHeaders() {
        return Map.of("Authorization", "token " + token);
    }

    @Override
    public String parseContent(String responseBody) {
        return responseBody;
    }

}
