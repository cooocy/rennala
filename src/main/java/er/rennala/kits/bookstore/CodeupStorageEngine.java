package er.rennala.kits.bookstore;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import er.rennala.z.RennalaException;

import java.util.Map;
import java.util.Objects;

public class CodeupStorageEngine implements StorageEngine {

    private final String baseUrl;

    private final String token;

    /**
     * <p> New CodeupStorageEngine.
     *
     * @param baseUrl e.g., "https://openapi-rdc.aliyuncs.com/oapi/v1/codeup/organizations/6540ea6e940b4e1cb0ceb4b3/repositories/6375636/files/"
     * @param token   the access token for authentication
     */
    public CodeupStorageEngine(String baseUrl,
                               String token) {
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
        return Map.of("x-yunxiao-token", token);
    }

    @Override
    public String parseContent(String responseBody) {
        JSON json = JSONUtil.parse(responseBody);
        Object content = json.getByPath("content");
        if (Objects.isNull(content) || !(content instanceof String)) {
            throw new RennalaException("[RNA-Bookstore] Pull Failed. Response Body Content is empty");
        }
        String decodedContent = Base64.decodeStr((String) content);
        if (StrUtil.isEmpty(decodedContent)) {
            throw new RennalaException("[RNA-Bookstore] Pull Failed. Response Body Content is empty");
        }
        return decodedContent;
    }

}
