package er.rennala.kits.bookstore;

import java.util.Map;

public interface StorageEngine {

    String assembleUrl(String fullName);

    Map<String, String> assembleHeaders();

    String parseContent(String responseBody);

}
