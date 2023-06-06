package one.terenin.gatewayservice.service.inter;

import com.google.protobuf.Message;
import one.terenin.forumservice.grpc.ForumCreationRequest;
import one.terenin.forumservice.grpc.ForumGetRequest;
import one.terenin.forumservice.grpc.ForumResponse;
import one.terenin.forumservice.grpc.ForumSuccessDeletion;
import one.terenin.forumservice.grpc.MessageRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ForumGrpcClient {

    Mono<ForumResponse> createForum(ForumCreationRequest request);
    Mono<ForumResponse> findForum(ForumGetRequest request);
    Mono<ForumSuccessDeletion> deleteForum(ForumGetRequest request);
    Flux<ForumResponse> sendMessage(MessageRequest request);
    Flux<ForumResponse> delete(MessageRequest request);

    /*rpc createForum(ForumCreationRequest) returns (ForumResponse) {};
    rpc findForum(ForumGetRequest) returns (ForumResponse) {};
    rpc deleteForum(ForumGetRequest) returns (ForumSuccessDeletion) {};
    rpc sendMessage(stream MessageRequest) returns (stream ForumResponse) {};
    rpc deleteMessage(stream MessageRequest) returns (stream ForumResponse) {};*/

}
