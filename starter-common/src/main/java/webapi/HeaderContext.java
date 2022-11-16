package webapi;

import utils.JsonUtils;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * 持有HeaderEnum的线程本地变量，在拦截器中从header中取出, 注入ThreadLocal, 方便使用时获取
 */
public class HeaderContext {

    private static final ThreadLocal<Map<HeaderEnum, String>> THREAD_LOCAL = new ThreadLocal<>();

    private HeaderContext() {
    }

    /**
     * 线程交量放入Map 格式的Header
     */
    public static void putAll(Map<HeaderEnum, String> map) {
        THREAD_LOCAL.set(map);
    }

    /**
     * 清除线程变量
     */
    public static void remove() {
        THREAD_LOCAL.remove();
    }

    /**
     * 根据HeaderEnum从线程交量获取值
     */
    public static String get(HeaderEnum headerEnum) {
        return Objects.nonNull(THREAD_LOCAL.get()) ? THREAD_LOCAL.get().get(headerEnum) : null;
    }

    /**
     * 从线程变量获取全局跟踪号
     */
    public static String getTraceld() {
        return Objects.nonNull(THREAD_LOCAL.get()) ? THREAD_LOCAL.get().get(HeaderEnum.BIZ_TRACE_ID) : null;
    }

    /**
     * 从线程交量获取交易序列号
     */
    public static String getTransSeqNo() {
        return Objects.nonNull(THREAD_LOCAL.get()) ? THREAD_LOCAL.get().get(HeaderEnum.TRANS_SEQ_NO) : null;
    }

    /**
     * 获取所有Header
     */
    public static Map<HeaderEnum, String> getAll() {
        return THREAD_LOCAL.get();
    }

    /**
     * 获取所有Header的json2string格式
     */
    public static String getAllString() {
        return Optional.ofNullable(getAll()).map(JsonUtils::toJsonString).orElse("");
    }
}
