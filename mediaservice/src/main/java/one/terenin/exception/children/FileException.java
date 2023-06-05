package one.terenin.exception.children;

import one.terenin.exception.BaseException;
import one.terenin.exception.common.ErrorCode;

public class FileException extends BaseException {

    public FileException(ErrorCode errorCode) {
        super(errorCode);
    }
}
