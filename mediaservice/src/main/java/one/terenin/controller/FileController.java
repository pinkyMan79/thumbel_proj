package one.terenin.controller;

import lombok.RequiredArgsConstructor;
import one.terenin.api.FileApi;
import one.terenin.dto.FileRequest;
import one.terenin.dto.FileResponse;
import one.terenin.service.FileService;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class FileController implements FileApi {

    private final FileService service;

    @Override
    public Mono<FileResponse> upload(FileRequest request, Mono<FilePart> filePartMono) {
        return service.doUpload(request, filePartMono);
    }

    @Override
    public Mono<Void> downloadFile(String fileName, ServerHttpResponse serverHttpResponse) {
        return service.doDownload(fileName, serverHttpResponse);
    }
}
