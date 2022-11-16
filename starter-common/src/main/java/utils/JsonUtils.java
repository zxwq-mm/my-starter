package utils;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;

/**
 * Json 工具类
 */
public final class JsonUtils {

    private static final ObjectMapper OBJECT_MAPPER = Jackson2ObjectMapperBuilder.json().build();

    private JsonUtils() {
        throw new UnsupportedOperationException();
    }

    /**
     * 转换为对象
     */
    public static <T> T fromJson(String json, Class<T> valueType) {
        try {
            return OBJECT_MAPPER.readValue(json, valueType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 转换为对象（失败不抛错）
     */
    public static <T> T fromJsonIgnoreError(String json, Class<T> valueType) {
        try {
            return OBJECT_MAPPER.readValue(json, valueType);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 转换为对象
     */
    public static <T> T fromJson(String json, TypeReference<T> valueTypeRef) {
        try {
            return OBJECT_MAPPER.readValue(json, valueTypeRef);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 转换为字符串
     */
    public static String toJsonString(Object object) {
        try {
            return object == null ? "null" : OBJECT_MAPPER.writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
