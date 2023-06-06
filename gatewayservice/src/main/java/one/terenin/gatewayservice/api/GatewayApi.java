package one.terenin.gatewayservice.api;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import one.terenin.forumservice.grpc.ForumCreationRequest;
import one.terenin.forumservice.grpc.ForumGetRequest;
import one.terenin.forumservice.grpc.ForumResponse;
import one.terenin.forumservice.grpc.ForumSuccessDeletion;
import one.terenin.forumservice.grpc.MessageRequest;
import one.terenin.gatewayservice.dto.authuser.AuthRequest;
import one.terenin.gatewayservice.dto.authuser.AuthResponse;
import one.terenin.gatewayservice.dto.authuser.UserForm;
import one.terenin.gatewayservice.dto.file.FileRequest;
import one.terenin.gatewayservice.dto.file.FileResponse;
import one.terenin.gatewayservice.dto.photo.PhotoRequest;
import one.terenin.gatewayservice.dto.photo.PhotoResponse;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RequestMapping("/api/v1/gateway")
public interface GatewayApi {

    @ApiOperation(value = "Registration", nickname = "register", response = UserForm.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Registered success", response = UserForm.class),
            @ApiResponse(code = 400, message = "Не получилось(")})
    @PostMapping("/register")
    Mono<UserForm> register(@RequestBody UserForm user);

    @ApiOperation(value = "Log in", nickname = "login", response = AuthResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Logged in", response = AuthResponse.class),
            @ApiResponse(code = 400, message = "Ups...(")})
    @GetMapping("/login")
    Mono<AuthResponse> login(@RequestBody AuthRequest request);

    @ApiOperation(value = "Parsing and update lifetime of JWT by URI", nickname = "parse", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Parsed success", response = String.class),
            @ApiResponse(code = 400, message = "Ups...(")})
    @GetMapping("/parse-and-update/{token}")
    Mono<String> parseJwt(@PathVariable("token") String token);

    @ApiOperation(value = "Uploading photo by URI", nickname = "uploadPhoto", response = PhotoResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Uploaded photo success", response = PhotoResponse.class),
            @ApiResponse(code = 400, message = "Ups...(")})
    @PostMapping("/upload-photo")
    Mono<PhotoResponse> upload(@RequestBody PhotoRequest request,
                               @RequestPart("file") Mono<FilePart> filePartMono);

    @ApiOperation(value = "Downloading photo by URI", nickname = "downloadPhoto", response = Mono.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Download photo success", response = Mono.class),
            @ApiResponse(code = 400, message = "Ups...(")})
    @GetMapping("/download-photo/{fileName}")
    Mono<Void> downloadPhoto(@PathVariable String fileName,
                            ServerHttpResponse serverHttpResponse);

    @ApiOperation(value = "Uploading file by URI", nickname = "uploadFile", response = FileResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Uploaded file success", response = FileResponse.class),
            @ApiResponse(code = 400, message = "Ups...(")})
    @PostMapping("/upload-file")
    Mono<FileResponse> upload(@RequestBody FileRequest request,
                              @RequestPart("file") Mono<FilePart> filePartMono);

    @ApiOperation(value = "Downloading file by URI", nickname = "downloadFile", response = Mono.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Download photo success", response = Mono.class),
            @ApiResponse(code = 400, message = "Ups...(")})
    @GetMapping("/download-file/{fileName}")
    Mono<Void> downloadFile(@PathVariable String fileName,
                            ServerHttpResponse serverHttpResponse);

    @ApiOperation(value = "Upload photo and bind it with user", nickname = "bindPhoto", response = UserForm.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Binding photo success", response = UserForm.class),
            @ApiResponse(code = 400, message = "Ups...(")})
    @PatchMapping("/photo/{username}")
    Mono<UserForm> bindPhoto(@PathVariable("username") String username,
                             @RequestBody PhotoRequest request,
                             @RequestPart("file") Mono<FilePart> filePartMono);

    @ApiOperation(value = "Upload file and bind it with user", nickname = "bindFIle", response = UserForm.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Binding file success", response = UserForm.class),
            @ApiResponse(code = 400, message = "Ups...(")})
    @PatchMapping("/file/{username}")
    Mono<UserForm> bindFile(@PathVariable("username") String username,
                            @RequestBody FileRequest request,
                            @RequestPart("file") Mono<FilePart> filePartMono);
    @ApiOperation(value = "Delete photo and bind it with user", nickname = "deletePhotoAndBind", response = UserForm.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Delete photo success", response = UserForm.class),
            @ApiResponse(code = 400, message = "Ups...(")})
    @DeleteMapping("/photo/{username}")
    Mono<UserForm> deletePhoto(@PathVariable("username") String username,
                               @RequestParam UUID photoId);
    @ApiOperation(value = "Delete file and bind it with user", nickname = "deleteFileAndBind", response = UserForm.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Delete file success", response = UserForm.class),
            @ApiResponse(code = 400, message = "Ups...(")})
    @DeleteMapping("/file/{username}")
    Mono<UserForm> deleteFile(@PathVariable("username") String username,
                              @RequestParam UUID fileId);
    @ApiOperation(value = "Delete user and bind it with user", nickname = "deleteUserAndBind", response = UserForm.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Delete user success", response = UserForm.class),
            @ApiResponse(code = 400, message = "Ups...(")})
    @DeleteMapping("/password/{username}")
    Mono<UserForm> delete(@PathVariable String username);

    @ApiOperation(value = "Delete user and bind it with user", nickname = "updatePhotoAndBind", response = UserForm.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Delete user success", response = UserForm.class),
            @ApiResponse(code = 400, message = "Ups...(")})
    @PatchMapping("/photo/{username}")
    Mono<UserForm> updatePhoto(@PathVariable("username") String username,
                               @RequestParam UUID photoId);

    @ApiOperation(value = "Update file and bind it with user", nickname = "updateFileAndBind", response = UserForm.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Delete user success", response = UserForm.class),
            @ApiResponse(code = 400, message = "Ups...(")})
    @PatchMapping("/file/{username}")
    Mono<UserForm> updateFile(@PathVariable("username") String username,
                              @RequestParam UUID fileId);

    @ApiOperation(value = "Update user and bind it with user", nickname = "updateUserAndBind", response = UserForm.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Delete user success", response = UserForm.class),
            @ApiResponse(code = 400, message = "Ups...(")})
    @PatchMapping("/username/{username}")
    Mono<UserForm> updateUsername(@PathVariable("username") String username,
                                  @RequestParam String newUsername);

    @ApiOperation(value = "Delete user and bind it with user", nickname = "updatePasswordAndBind", response = UserForm.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Delete user success", response = UserForm.class),
            @ApiResponse(code = 400, message = "Ups...(")})
    @PatchMapping("/password/{username}")
    Mono<UserForm> updatePassword(@PathVariable("username") String username,
                                  @RequestParam String newPassword);

    @ApiOperation(value = "Delete user and bind it with user", nickname = "updateUserAndBind", response = UserForm.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Delete user success", response = UserForm.class),
            @ApiResponse(code = 400, message = "Ups...(")})
    @PatchMapping("/password/{username}")
    Mono<UserForm> update(@PathVariable String username,
                          @RequestBody UserForm form);

    @ApiOperation(value = "Delete user and bind it with user", nickname = "createForum", response = UserForm.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Delete user success", response = UserForm.class),
            @ApiResponse(code = 400, message = "Ups...(")})
    @PostMapping("/create")
    Mono<ForumResponse> createForum(@RequestBody ForumCreationRequest request);

    @ApiOperation(value = "Delete user and bind it with user", nickname = "findForum", response = UserForm.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Delete user success", response = UserForm.class),
            @ApiResponse(code = 400, message = "Ups...(")})
    @GetMapping("/find")
    Mono<ForumResponse> findForum(@RequestBody ForumGetRequest request);

    @ApiOperation(value = "Delete user and bind it with user", nickname = "deleteForum", response = UserForm.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Delete user success", response = UserForm.class),
            @ApiResponse(code = 400, message = "Ups...(")})
    @DeleteMapping("/delete")
    Mono<ForumSuccessDeletion> deleteForum(@RequestBody ForumGetRequest request);

    @ApiOperation(value = "Delete user and bind it with user", nickname = "sendMessage", response = UserForm.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Delete user success", response = UserForm.class),
            @ApiResponse(code = 400, message = "Ups...(")})
    @PostMapping("/send")
    Flux<ForumResponse> sendMessage(@RequestBody MessageRequest request);

    @ApiOperation(value = "Delete user and bind it with user", nickname = "deleteF", response = UserForm.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Delete user success", response = UserForm.class),
            @ApiResponse(code = 400, message = "Ups...(")})
    @DeleteMapping("/delete")
    Flux<ForumResponse> delete(@RequestBody MessageRequest request);

}
