package one.terenin.api;

import one.terenin.dto.FileRequest;
import one.terenin.dto.FileResponse;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import reactor.core.publisher.Mono;

@RequestMapping("/api/v1/file")
public interface FileApi {

    @PostMapping("/upload")
    Mono<FileResponse> upload(@RequestBody FileRequest request,
                              @RequestPart("file") Mono<FilePart> filePartMono);

    @GetMapping("/download/{fileName}")
    Mono<Void> downloadFile(@PathVariable String fileName,
                            ServerHttpResponse serverHttpResponse);

}
