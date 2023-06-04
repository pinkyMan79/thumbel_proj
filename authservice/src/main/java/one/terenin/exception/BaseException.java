package one.terenin.exception;

import one.terenin.exception.common.ErrorCode;

public class BaseException extends RuntimeException {

    protected ErrorCode errorCode;

    public BaseException(ErrorCode errorCode) {
        super(errorCode.getInfo());
        this.errorCode = errorCode;
    }
}
