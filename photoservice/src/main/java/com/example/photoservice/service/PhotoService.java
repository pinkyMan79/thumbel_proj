package com.example.photoservice.service;

import com.example.photoservice.dto.PhotoRequest;
import com.example.photoservice.dto.PhotoResponse;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.server.ServerHttpResponse;
import reactor.core.publisher.Mono;

public interface PhotoService {

    Mono<PhotoResponse> doUpload(PhotoRequest request, Mono<FilePart> filePartMono);

    Mono<Void> doDownload(String fileName, ServerHttpResponse serverHttpResponse);

}
