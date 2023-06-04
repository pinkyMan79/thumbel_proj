package one.terenin.forumservice.exception;

import one.terenin.forumservice.grpc.ForumErrorCode;

public class ForumException extends RuntimeException{
    public ForumException(ForumErrorCode errorCode) {
        super(errorCode.name());
    }
}
