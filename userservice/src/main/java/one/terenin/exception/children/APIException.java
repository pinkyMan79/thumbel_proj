package one.terenin.exception.children;

import one.terenin.exception.BaseException;
import one.terenin.exception.common.ErrorCode;

public class APIException extends BaseException {

    public APIException(ErrorCode errorCode) {
        super(errorCode);
    }
}
