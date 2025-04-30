package er.rennala.kits.bookstore;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import er.rennala.domain.RennalaException;

import java.util.Map;
import java.util.Objects;

public class GitlabStorageEngine implements StorageEngine {

    private final String baseUrl;

    private final String token;

    public GitlabStorageEngine(String baseUrl, String token) {
        this.baseUrl = baseUrl;
        this.token = token;
    }

    @Override
    public String assembleUrl(String fullName) {
        String encodedFUllName = URLUtil.encodeAll(fullName);
        return baseUrl + encodedFUllName + "?ref=main";
    }

    @Override
    public Map<String, String> assembleHeaders() {
        return Map.of("PRIVATE-TOKEN", token);
    }

    @Override
    public String parseContent(String responseBody) {
        JSON json = JSONUtil.parse(responseBody);
        Object content = json.getByPath("content");
        if (Objects.isNull(content) || !(content instanceof String)) {
            throw new RennalaException("[Bookstore] Pull Failed. Response Body Content is empty");
        }
        String decodedContent = Base64.decodeStr((String) content);
        if (StrUtil.isEmpty(decodedContent)) {
            throw new RennalaException("[Bookstore] Pull Failed. Response Body Content is empty");
        }
        return decodedContent;
    }

}
