package er.rennala.kits.bookstore;

import cn.hutool.core.util.StrUtil;
import er.rennala.z.BookstoreException;

import java.util.Map;

/**
 * <p> Factory class for creating instances of StorageEngine based on the BOOKSTORE_ENGINE environment variable.
 * <p> It supports GitLab and GitHub storage engines.
 * <p> These environment variables to be used:
 * <p> BOOKSTORE_ENGINE: "gitlab" or "github"
 * <p> BOOKSTORE_GITLAB_URL: the base URL for GitLab storage
 * <p> BOOKSTORE_GITLAB_TOKEN: the access token for GitLab storage
 * <p> BOOKSTORE_GITHUB_URL: the base URL for GitHub storage
 * <p> BOOKSTORE_GITHUB_TOKEN: the access token for GitHub storage
 */
public class StorageEngineFactory {

    private final static String GITLAB = "gitlab";

    private final static String GITHUB = "github";

    private StorageEngineFactory() {
    }

    /**
     * Creates a new instance of StorageEngine based on the BOOKSTORE_ENGINE environment variable.
     *
     * @return a StorageEngine instance
     * @throws BookstoreException if the BOOKSTORE_ENGINE environment variable is not set or is unknown
     */
    public static StorageEngine newStorageEngine() {
        String bookstoreEngine = System.getenv().get("BOOKSTORE_ENGINE");
        if (StrUtil.isEmpty(bookstoreEngine)) {
            throw new BookstoreException("[RNA-Bookstore] BOOKSTORE_ENGINE environment variable is not set");
        }

        if (GITLAB.equals(bookstoreEngine)) {
            return newGitlabStorageEngine();
        }

        if (GITHUB.equals(bookstoreEngine)) {
            return newGithubStorageEngine();
        }

        throw new BookstoreException("[RNA-Bookstore] Unknown BOOKSTORE_ENGINE: " + bookstoreEngine);
    }

    private static GitlabStorageEngine newGitlabStorageEngine() {
        Map<String, String> environments = System.getenv();
        String url = environments.get("BOOKSTORE_GITLAB_URL");
        String token = environments.get("BOOKSTORE_GITLAB_TOKEN");
        if (StrUtil.hasEmpty(url, token)) {
            throw new BookstoreException("[RNA-Bookstore] BOOKSTORE_GITLAB_URL, BOOKSTORE_GITLAB_TOKEN environment variable is not set");
        }
        return new GitlabStorageEngine(url, token);
    }

    private static GitHubStorageEngine newGithubStorageEngine() {
        Map<String, String> environments = System.getenv();
        String url = environments.get("BOOKSTORE_GITHUB_URL");
        String token = environments.get("BOOKSTORE_GITHUB_TOKEN");
        if (StrUtil.hasEmpty(url, token)) {
            throw new BookstoreException("[RNA-Bookstore] BOOKSTORE_GITHUB_URL, BOOKSTORE_GITHUB_TOKEN environment variable is not set");
        }
        return new GitHubStorageEngine(url, token);
    }

}
