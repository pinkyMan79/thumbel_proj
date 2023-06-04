package com.example.photoservice.controller;

import com.example.photoservice.api.PhotoApi;
import com.example.photoservice.dto.PhotoRequest;
import com.example.photoservice.dto.PhotoResponse;
import com.example.photoservice.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class PhotoController implements PhotoApi {

    private final PhotoService service;

    @Override
    public Mono<PhotoResponse> upload(PhotoRequest request, Mono<FilePart> filePartMono) {
        return service.doUpload(request, filePartMono);
    }

    @Override
    public Mono<Void> downloadFile(String fileName, ServerHttpResponse serverHttpResponse) {
        return service.doDownload(fileName, serverHttpResponse);
    }
}
