package com.example.photoservice.api;

import com.example.photoservice.dto.PhotoRequest;
import com.example.photoservice.dto.PhotoResponse;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import reactor.core.publisher.Mono;

@RequestMapping("/api/v1/photo")
public interface PhotoApi {

    @PostMapping("/upload")
    Mono<PhotoResponse> upload(@RequestBody PhotoRequest request,
                               @RequestPart("file") Mono<FilePart> filePartMono);

    @GetMapping("/download/{fileName}")
    Mono<Void> downloadFile(@PathVariable String fileName,
                            ServerHttpResponse serverHttpResponse);

}
