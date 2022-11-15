package exception;

import java.util.Optional;

/**
 * 错误编码枚举行为
 */
public interface ErrorCodeEnumOperation {

    /**
     * 错误编码
     */
    String getCode();

    /**
     * 错误信息
     */
    String getMessage();

    /**
     * 输出参数化的错误信息
     */
    default String outMessage(Object... errorMessageParams) {
        return Optional.ofNullable(errorMessageParams).map(e -> String.format(this.getMessage(), e)).orElse(this.getMessage());
    }

}
