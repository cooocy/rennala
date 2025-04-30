package er.rennala.kits.bookstore;

import cn.hutool.core.util.StrUtil;
import er.rennala.domain.BookstoreException;

import java.util.Map;

public class StorageEngineFactory {

    private final static String GITLAB = "gitlab";

    private final static String GITHUB = "github";

    private StorageEngineFactory() {
    }

    public static StorageEngine newStorageEngine() {
        String bookstoreEngine = System.getenv().get("BOOKSTORE_ENGINE");
        if (StrUtil.isEmpty(bookstoreEngine)) {
            throw new BookstoreException("BOOKSTORE_ENGINE environment variable is not set");
        }

        if (GITLAB.equals(bookstoreEngine)) {
            return newGitlabStorageEngine();
        }

        if (GITHUB.equals(bookstoreEngine)) {
            return newGithubStorageEngine();
        }

        throw new BookstoreException("Unknown BOOKSTORE_ENGINE: " + bookstoreEngine);
    }

    private static GitlabStorageEngine newGitlabStorageEngine() {
        Map<String, String> environments = System.getenv();
        String url = environments.get("BOOKSTORE_GITLAB_URL");
        String token = environments.get("BOOKSTORE_GITLAB_TOKEN");
        if (StrUtil.hasEmpty(url, token)) {
            throw new BookstoreException("BOOKSTORE_GITLAB_URL, BOOKSTORE_GITLAB_TOKEN environment variable is not set");
        }
        return new GitlabStorageEngine(url, token);
    }

    private static GitHubStorageEngine newGithubStorageEngine() {
        Map<String, String> environments = System.getenv();
        String url = environments.get("BOOKSTORE_GITHUB_URL");
        String token = environments.get("BOOKSTORE_GITHUB_TOKEN");
        if (StrUtil.hasEmpty(url, token)) {
            throw new BookstoreException("BOOKSTORE_GITHUB_URL, BOOKSTORE_GITHUB_TOKEN environment variable is not set");
        }
        return new GitHubStorageEngine(url, token);
    }

}
