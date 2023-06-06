package one.terenin.gatewayservice.controller;

import one.terenin.forumservice.grpc.ForumCreationRequest;
import one.terenin.forumservice.grpc.ForumGetRequest;
import one.terenin.forumservice.grpc.ForumResponse;
import one.terenin.forumservice.grpc.ForumSuccessDeletion;
import one.terenin.forumservice.grpc.MessageRequest;
import one.terenin.gatewayservice.api.GatewayApi;
import one.terenin.gatewayservice.dto.authuser.AuthRequest;
import one.terenin.gatewayservice.dto.authuser.AuthResponse;
import one.terenin.gatewayservice.dto.authuser.UserForm;
import one.terenin.gatewayservice.dto.file.FileRequest;
import one.terenin.gatewayservice.dto.file.FileResponse;
import one.terenin.gatewayservice.dto.photo.PhotoRequest;
import one.terenin.gatewayservice.dto.photo.PhotoResponse;
import one.terenin.gatewayservice.exception.children.APIException;
import one.terenin.gatewayservice.exception.common.ErrorCode;
import one.terenin.gatewayservice.service.ForumClientGrpcService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
public class GatewayController implements GatewayApi {

    private final WebClient authClient;
    private final WebClient userClient;
    private final WebClient fileClient;
    private final WebClient photoClient;
    private final ForumClientGrpcService forumServiceGrpc;

    public GatewayController(@Qualifier("authServiceWebClient") WebClient authClient,
                             @Qualifier("userServiceWebClient") WebClient userClient,
                             @Qualifier("mediaServiceWebClient") WebClient fileClient,
                             @Qualifier("photoServiceWebClient") WebClient photoClient,
                             ForumClientGrpcService forumServiceGrpc) {
        this.authClient = authClient;
        this.userClient = userClient;
        this.fileClient = fileClient;
        this.photoClient = photoClient;
        this.forumServiceGrpc = forumServiceGrpc;
    }

    @Override
    public Mono<UserForm> register(UserForm user) {
        return authClient
                .post()
                .uri("register")
                .body(user, UserForm.class)
                .retrieve()
                .bodyToMono(UserForm.class)
                .switchIfEmpty(
                        Mono.error(new APIException(ErrorCode.FAILED))
                );
    }

    @Override
    public Mono<AuthResponse> login(AuthRequest request) {
        return authClient
                .post()
                .uri("login")
                .body(request, AuthRequest.class)
                .retrieve()
                .bodyToMono(AuthResponse.class)
                .switchIfEmpty(
                        Mono.error(new APIException(ErrorCode.FAILED))
                );;
    }

    @Override
    public Mono<String> parseJwt(String token) {
        return authClient
                .get()
                .uri("/parse-and-update/" + token)
                .retrieve()
                .bodyToMono(String.class);
    }

    @Override
    public Mono<PhotoResponse> upload(PhotoRequest request, Mono<FilePart> filePartMono) {
        return photoClient
                .post()
                .uri("/upload")
                .body(request, PhotoRequest.class)
                .retrieve()
                .bodyToMono(PhotoResponse.class)
                .switchIfEmpty(
                        Mono.error(new APIException(ErrorCode.FAILED))
                );;
    }

    @Override
    public Mono<Void> downloadPhoto(String fileName, ServerHttpResponse serverHttpResponse) {
        return photoClient.get()
                .uri("/download/{filename}", fileName)
                .accept(MediaType.IMAGE_JPEG)
                .retrieve()
                .bodyToMono(Void.class)
                .switchIfEmpty(
                        Mono.error(new APIException(ErrorCode.FAILED))
                );;
                /*.flatMap(bytes -> {
                    serverHttpResponse.getHeaders().setContentType(MediaType.IMAGE_JPEG);
                    serverHttpResponse.getHeaders().setContentLength(bytes.length);
                    return serverHttpResponse.writeWith(Mono.just(serverHttpResponse.bufferFactory().wrap(bytes)));
                });*/

    }

    @Override
    public Mono<FileResponse> upload(FileRequest request, Mono<FilePart> filePartMono) {
        return fileClient
                .post()
                .uri("/upload")
                .body(request, FileRequest.class)
                .retrieve()
                .bodyToMono(FileResponse.class)
                .switchIfEmpty(
                        Mono.error(new APIException(ErrorCode.FAILED))
                );;
    }

    @Override
    public Mono<Void> downloadFile(String fileName, ServerHttpResponse serverHttpResponse) {
        return photoClient.get()
                .uri("/download/{filename}", fileName)
                .accept(MediaType.asMediaType(MimeType.valueOf("audio/mp3")))
                .retrieve()
                .bodyToMono(Void.class)
                .switchIfEmpty(
                        Mono.error(new APIException(ErrorCode.FAILED))
                );;
    }

    @Override
    public Mono<UserForm> bindPhoto(String username, PhotoRequest request, Mono<FilePart> filePartMono) {
        UUID photoId = photoClient
                .post()
                .uri("/upload")
                .body(request, PhotoRequest.class)
                .retrieve()
                .bodyToMono(PhotoResponse.class).map(PhotoResponse::getId)
                .switchIfEmpty(Mono.error(new APIException(ErrorCode.FAILED)))
                .block();

        return userClient.patch()
                .uri("/bind/photo/{username}/{fileId}", username, photoId.toString())
                .retrieve()
                .bodyToMono(UserForm.class)
                .switchIfEmpty(
                        Mono.error(new APIException(ErrorCode.FAILED))
                );;

    }

    @Override
    public Mono<UserForm> bindFile(String username, FileRequest request, Mono<FilePart> filePartMono) {
        UUID block = fileClient
                .post()
                .uri("/upload")
                .body(request, FileRequest.class)
                .retrieve()
                .bodyToMono(FileResponse.class).map(FileResponse::getFileId)
                .switchIfEmpty(Mono.error(new APIException(ErrorCode.FAILED)))
                .block();

        return userClient.patch()
                .uri("/bind/photo/{username}/{fileId}", username, block)
                .retrieve()
                .bodyToMono(UserForm.class)
                .switchIfEmpty(
                        Mono.error(new APIException(ErrorCode.FAILED))
                );;
    }

    @Override
    public Mono<ForumResponse> createForum(ForumCreationRequest request) {
        return forumServiceGrpc.createForum(request)
                .switchIfEmpty(
                        Mono.error(new APIException(ErrorCode.FAILED))
                );
    }

    @Override
    public Mono<ForumResponse> findForum(ForumGetRequest request) {
        return forumServiceGrpc.findForum(request)
                .switchIfEmpty(
                        Mono.error(new APIException(ErrorCode.FAILED))
                );
    }

    @Override
    public Mono<ForumSuccessDeletion> deleteForum(ForumGetRequest request) {
        return forumServiceGrpc.deleteForum(request).switchIfEmpty(
                Mono.error(new APIException(ErrorCode.FAILED))
        );
    }

    @Override
    public Flux<ForumResponse> sendMessage(MessageRequest request) {
        return forumServiceGrpc.sendMessage(request).switchIfEmpty(
                Mono.error(new APIException(ErrorCode.FAILED))
        );
    }

    @Override
    public Flux<ForumResponse> delete(MessageRequest request) {
        return forumServiceGrpc.delete(request).switchIfEmpty(
                Mono.error(new APIException(ErrorCode.FAILED))
        );
    }

    @Override
    public Mono<UserForm> deletePhoto(String username, UUID photoId) {
        return null;
    }

    @Override
    public Mono<UserForm> deleteFile(String username, UUID fileId) {
        return null;
    }

    @Override
    public Mono<UserForm> delete(String username) {
        return null;
    }

    @Override
    public Mono<UserForm> updatePhoto(String username, UUID photoId) {
        return null;
    }

    @Override
    public Mono<UserForm> updateFile(String username, UUID fileId) {
        return null;
    }

    @Override
    public Mono<UserForm> updateUsername(String username, String newUsername) {
        return null;
    }

    @Override
    public Mono<UserForm> updatePassword(String username, String newPassword) {
        return null;
    }

    @Override
    public Mono<UserForm> update(String username, UserForm form) {
        return null;
    }

}
