package exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 异常信息
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorInfo {

    /**
     * 异常code
     */
    private String code;

    /**
     * 异常message
     */
    private String message;
}
