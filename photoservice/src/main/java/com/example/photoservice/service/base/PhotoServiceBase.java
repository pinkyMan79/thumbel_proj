package com.example.photoservice.service.base;

import com.example.photoservice.dto.PhotoRequest;
import com.example.photoservice.dto.PhotoResponse;
import com.example.photoservice.mapper.PhotoMapper;
import com.example.photoservice.repository.PhotoRepository;
import com.example.photoservice.service.PhotoService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
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
public class PhotoServiceBase implements PhotoService {

    private final PhotoRepository repository;
    private final PhotoMapper mapper;
    private Path basePath = Paths.
            get("./src/main/resources/files/");

    @Override
    public Mono<PhotoResponse> doUpload(PhotoRequest request, Mono<FilePart> filePartMono) {
        int checkSum = 0;
        filePartMono
                .doOnNext(fp -> log.info
                        ("Received File : " + fp.filename()))
                .flatMap(fp -> fp.
                        transferTo(basePath.resolve(fp.filename())))
                .then().subscribeOn(Schedulers.parallel()).subscribe();
        return repository.save(mapper.map(request).toBuilder()
                        .photoId(UUID.randomUUID())
                        .checksum(checkSum).build()).map(mapper::map);
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
                file.length()).subscribeOn(Schedulers.parallel());
    }
}
