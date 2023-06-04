package one.terenin.exception.children;

import one.terenin.exception.BaseException;
import one.terenin.exception.common.ErrorCode;

public class AuthenticationException extends BaseException {

    public AuthenticationException(ErrorCode errorCode) {
        super(errorCode);
    }
}
