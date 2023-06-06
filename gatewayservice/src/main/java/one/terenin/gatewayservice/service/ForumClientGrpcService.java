package one.terenin.gatewayservice.service;

import com.google.common.util.concurrent.ListenableFuture;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.MethodDescriptor;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import one.terenin.forumservice.grpc.ForumCreationRequest;
import one.terenin.forumservice.grpc.ForumGetRequest;
import one.terenin.forumservice.grpc.ForumResponse;
import one.terenin.forumservice.grpc.ForumServiceGrpc;
import one.terenin.forumservice.grpc.ForumSuccessDeletion;
import one.terenin.forumservice.grpc.MessageRequest;
import one.terenin.forumservice.grpc.MessageResponse;
import one.terenin.gatewayservice.config.propertysource.GrpcPropertySource;
import one.terenin.gatewayservice.exception.children.APIException;
import one.terenin.gatewayservice.exception.common.ErrorCode;
import one.terenin.gatewayservice.service.inter.ForumGrpcClient;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class ForumClientGrpcService implements ForumGrpcClient {

    private GrpcPropertySource propertySource;

    @Override
    public Mono<ForumResponse> createForum(ForumCreationRequest request) {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(propertySource.getServiceHost(), propertySource.getServiceGrpcPort())
                .usePlaintext()
                .build();
        ForumServiceGrpc.ForumServiceBlockingStub client = ForumServiceGrpc.newBlockingStub(channel)
                .withDeadlineAfter(10, TimeUnit.SECONDS);;
        ForumResponse forum = client.createForum(request);
        return Mono.just(forum);
    }

    @SneakyThrows
    @Override
    public Mono<ForumResponse> findForum(ForumGetRequest request) {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(propertySource.getServiceHost(), propertySource.getServiceGrpcPort())
                .usePlaintext()
                .build();
        ForumServiceGrpc.ForumServiceFutureStub client = ForumServiceGrpc.newFutureStub(channel)
                .withDeadlineAfter(10, TimeUnit.SECONDS);
        ListenableFuture<ForumResponse> forum = client.findForum(request);
        if (forum.isDone()){
            return Mono.just(forum.get());
        }else {
            return Mono.error(new APIException(ErrorCode.FAILED));
        }
    }

    @Override
    public Mono<ForumSuccessDeletion> deleteForum(ForumGetRequest request) {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(propertySource.getServiceHost(), propertySource.getServiceGrpcPort())
                .usePlaintext()
                .build();
        ForumServiceGrpc.ForumServiceBlockingStub client = ForumServiceGrpc.newBlockingStub(channel)
                .withDeadlineAfter(10, TimeUnit.SECONDS);
        ForumSuccessDeletion forumSuccessDeletion = client.deleteForum(request);
        return Mono.just(forumSuccessDeletion).switchIfEmpty(Mono.error(new APIException(ErrorCode.FAILED)));
    }

    @SneakyThrows
    @Override
    public Flux<ForumResponse> sendMessage(MessageRequest request) {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(propertySource.getServiceHost(), propertySource.getServiceGrpcPort())
                .build();
        ForumServiceGrpc.ForumServiceStub forumServiceStub = ForumServiceGrpc.newStub(channel);
        final ForumResponse[] response = {null};
        StreamObserver<MessageRequest> messageRequestStreamObserver = forumServiceStub.sendMessage(new StreamObserver<ForumResponse>() {
            @Override
            public void onNext(ForumResponse value) {
                value.getMessagesList().add(MessageResponse.newBuilder()
                        .setContent(request.getContent())
                        .setSenderLogin(request.getSenderLogin())
                        .setForumDestinationTitle(value.getTitle())
                        .build());
                response[0] = value;
                log.info("send message to server");
            }

            @Override
            public void onError(Throwable t) {
                log.error("{}{}", "Server grpc process failed", t.getMessage());
            }

            @Override
            public void onCompleted() {
                log.info("Server stream completed");
            }
        });
        messageRequestStreamObserver.onNext(request);
        messageRequestStreamObserver.onCompleted();
        /*MethodDescriptor<MessageRequest, ForumResponse> sendMessageMethod = ForumServiceGrpc.getSendMessageMethod();
        MethodDescriptor.Marshaller<MessageRequest> requestMarshaller = sendMessageMethod.getRequestMarshaller();
        InputStream stream = requestMarshaller.stream(request);
        MessageRequest parse = requestMarshaller.parse(stream);
        MethodDescriptor.Marshaller<ForumResponse> responseMarshaller = sendMessageMethod.getResponseMarshaller();*/
        return Flux.just(response[0])
                .subscribeOn(Schedulers.parallel())
                .switchIfEmpty(Mono.error(new APIException(ErrorCode.FAILED)));
    }

    @Override
    @SuppressWarnings("unrealised")
    public Flux<ForumResponse> delete(MessageRequest request) {
        return null;
    }
}
