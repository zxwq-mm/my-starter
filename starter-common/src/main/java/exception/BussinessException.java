package exception;

/**
 * 统一定义异常
 */
public class BussinessException extends RuntimeException {

    private String errorCode;

    /**
     * 构造函数
     */
    public BussinessException(String errorCode, String errorMessage, Throwable cause) {
        super(errorMessage, cause);
        this.errorCode = errorCode;
    }

    /**
     * 构造函数
     */
    public BussinessException(String errorMessage) {
        this(null, errorMessage, null);
    }

    /**
     * 构造函数
     */
    public BussinessException(String errorCode, String errorMessage) {
        this(errorCode, errorMessage, null);
    }

}
