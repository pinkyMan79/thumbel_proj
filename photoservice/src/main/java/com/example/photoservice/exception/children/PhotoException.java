package com.example.photoservice.exception.children;


import com.example.photoservice.exception.BaseException;
import com.example.photoservice.exception.common.ErrorCode;

public class PhotoException extends BaseException {

    public PhotoException(ErrorCode errorCode) {
        super(errorCode);
    }
}
