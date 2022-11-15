package exception;

import org.apache.commons.collections4.CollectionUtils;

import java.util.Collection;

/**
 * 业务断言类，辅助判断抛出业务异常
 */
public class ServicePreconditions {

    private ServicePreconditions() {
    }

    /**
     * 抛出业务异常
     */
    public static void throwException(String code, String msg) {
        throw new BussinessException(code, msg);
    }

    /**
     * 断言集合为空, 抛出自定义errorMessage信息
     */
    public static void isEmpty(Collection<?> coll, ErrorCodeEnumOperation codeEnum, Object... message) {
        if (CollectionUtils.isEmpty(coll)) {
            throwException(codeEnum.getCode(), codeEnum.getMessage());
        }
    }

    /**
     * 断言表达式为true,抛出默认errorMessage信息
     */
    public static void checkTrue(boolean expression, ErrorCodeEnumOperation codeEnum) {
        if (!expression) {
            throwException(codeEnum.getCode(), codeEnum.getMessage());
        }
    }

    /**
     * 断言表达式为true,抛出自定义errorMessage信息
     */
    public static void checkTrue(boolean expression, ErrorCodeEnumOperation codeEunm, Object... message) {
        if (!expression) {
            throwException(codeEunm.getCode(), codeEunm.getMessage().contains("%s") ? codeEunm.outMessage() : codeEunm.getMessage());
        }
    }

}
