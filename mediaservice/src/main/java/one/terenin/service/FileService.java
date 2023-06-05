package one.terenin.service;

import one.terenin.dto.FileRequest;
import one.terenin.dto.FileResponse;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.server.ServerHttpResponse;
import reactor.core.publisher.Mono;

public interface FileService {

    Mono<FileResponse> doUpload(FileRequest request, Mono<FilePart> filePartMono);

    Mono<Void> doDownload(String fileName, ServerHttpResponse serverHttpResponse);

}
