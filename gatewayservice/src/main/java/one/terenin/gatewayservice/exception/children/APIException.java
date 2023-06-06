package one.terenin.gatewayservice.exception.children;


import one.terenin.gatewayservice.exception.BaseException;
import one.terenin.gatewayservice.exception.common.ErrorCode;

public class APIException extends BaseException {

    public APIException(ErrorCode errorCode) {
        super(errorCode);
    }
}
