package cn.lianrf.utils.db.exception;

/**
 * @version: v1.0
 * @date: 2019/12/31
 * @author: lianrf
 */
public class AllocationException extends RuntimeException{
    public AllocationException() {
    }

    public AllocationException(String message) {
        super(message);
    }

    public AllocationException(String message, Throwable cause) {
        super(message, cause);
    }

    public AllocationException(Throwable cause) {
        super(cause);
    }

    public AllocationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
