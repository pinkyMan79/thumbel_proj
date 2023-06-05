package one.terenin.service.base;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import one.terenin.dto.FileRequest;
import one.terenin.dto.FileResponse;
import one.terenin.exception.children.FileException;
import one.terenin.exception.common.ErrorCode;
import one.terenin.mapper.FileMapper;
import one.terenin.repository.FileRepository;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ZeroCopyHttpOutputMessage;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService implements one.terenin.service.FileService {

    private final FileRepository repository;
    private final FileMapper mapper;
    private Path basePath = Paths.
            get("./src/main/resources/files/");

    @Override
    public Mono<FileResponse> doUpload(FileRequest request, Mono<FilePart> filePartMono) {
        int checkSum = 0;
        filePartMono
                .doOnNext(fp -> log.info
                        ("Received File : " + fp.filename()))
                .flatMap(fp -> fp.
                        transferTo(basePath.resolve(fp.filename())))
                .then()
                .switchIfEmpty(Mono.error(new FileException(ErrorCode.FILE_NOT_UPLOADED)))
                .subscribeOn(Schedulers.parallel())
                .subscribe();
        return repository.save(mapper.map(request).toBuilder()
                        .fileId(UUID.randomUUID())
                        .fileLocation(basePath.toString())
                        .build())
                        .map(mapper::map)
                .switchIfEmpty(Mono.error(new FileException(ErrorCode.FILE_MAP_ERROR)));
    }

    @SneakyThrows
    @Override
    public Mono<Void> doDownload(String fileName, ServerHttpResponse serverHttpResponse) {
        ZeroCopyHttpOutputMessage zeroCopyResponse =
                (ZeroCopyHttpOutputMessage) serverHttpResponse;
        serverHttpResponse.getHeaders().set(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=" + fileName + "");
        serverHttpResponse.getHeaders().setContentType(MediaType.
                APPLICATION_OCTET_STREAM);
        ClassPathResource resource = new ClassPathResource(fileName);
        File file = resource.getFile();
        return zeroCopyResponse.writeWith(file, 0,
                file.length())
                .switchIfEmpty(Mono.error(new FileException(ErrorCode.DOWNLOAD_FAILED)))
                .subscribeOn(Schedulers.parallel());
    }
}
