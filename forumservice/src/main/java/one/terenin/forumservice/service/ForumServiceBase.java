package one.terenin.forumservice.service;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import one.terenin.forumservice.entity.ForumEntity;
import one.terenin.forumservice.exception.ForumException;
import one.terenin.forumservice.grpc.ForumCreationRequest;
import one.terenin.forumservice.grpc.ForumErrorCode;
import one.terenin.forumservice.grpc.ForumGetRequest;
import one.terenin.forumservice.grpc.ForumResponse;
import one.terenin.forumservice.grpc.ForumServiceGrpc;
import one.terenin.forumservice.grpc.ForumSuccessDeletion;
import one.terenin.forumservice.grpc.MessageRequest;
import one.terenin.forumservice.mapper.ForumMapper;
import one.terenin.forumservice.mapper.MessageMapper;
import one.terenin.forumservice.repository.ForumRepository;
import one.terenin.forumservice.repository.MessageRepository;
import org.lognet.springboot.grpc.GRpcService;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Objects;
import java.util.UUID;

@GRpcService
@RequiredArgsConstructor
public class ForumServiceBase extends ForumServiceGrpc.ForumServiceImplBase {

    private final MessageRepository messageRepository;
    private final ForumRepository forumRepository;
    private final MessageMapper messageMapper;
    private final ForumMapper forumMapper;

    @Override
    public void createForum(ForumCreationRequest request, StreamObserver<ForumResponse> responseObserver) {
        ForumEntity entity = forumMapper.map(request);
        forumRepository
                .save(entity.toBuilder()
                        .forumId(UUID.randomUUID())
                        .messages(null)
                        .build())
                .flatMap(e -> {
                    ForumResponse response = forumMapper.map(e);
                    responseObserver.onNext(response);
                    responseObserver.onCompleted();
                    return Mono.just(response);
                }).switchIfEmpty(Mono
                        .error(new ForumException(ForumErrorCode.INVALID_REQUEST_CREATION_TITLE_IN_USE)))
                .subscribe();
        responseObserver.onCompleted();
    }

    @Override
    public void findForum(ForumGetRequest request, StreamObserver<ForumResponse> responseObserver) {
        forumRepository.findByTitle(request.getTitle()).filter(Objects::nonNull)
                .flatMap(e -> {
                    if (e.getTitle().equals(request.getTitle())){
                        ForumResponse map = forumMapper.map(e);
                        responseObserver.onNext(map);
                        responseObserver.onCompleted();
                        return Mono.just(map);
                    }else {
                        return Mono.error(new ForumException(ForumErrorCode.NOT_FOUND));
                    }
                }).switchIfEmpty(Mono.error(new ForumException(ForumErrorCode.NOT_FOUND)))
                .subscribe();
        responseObserver.onCompleted();
    }

    @Override
    public void deleteForum(ForumGetRequest request, StreamObserver<ForumSuccessDeletion> responseObserver) {
        forumRepository.deleteForumEntityByTitle(request.getTitle()).doOnSuccess(unused -> {
            responseObserver.onNext(ForumSuccessDeletion.newBuilder().setTitle(request.getTitle()).build());
        }).subscribe();
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<MessageRequest> sendMessage(StreamObserver<ForumResponse> responseObserver) {
        return new StreamObserver<>() {
            @Override
            public void onNext(MessageRequest value) {
                messageRepository.save(messageMapper.map(value)
                                .toBuilder()
                                .messageId(UUID.randomUUID())
                                .forum(UUID.fromString(value.getForumDestinationId()))
                                .forumTitle(value.getForumDestinationTitle())
                                .build())
                        .flatMap(e -> forumRepository.findByTitle(e.getForumTitle())
                                .flatMap(forum -> {
                                    forum.getMessages().add(e.getMessageId());
                                    responseObserver.onNext(forumMapper.map(forum));
                                    return forumRepository.save(forum);
                                })
                        )
                        // супер быстрый и классный прям ух
                        .subscribeOn(Schedulers.boundedElastic())
                        .subscribe();
            }

            @Override
            public void onError(Throwable t) {
                responseObserver.onError(t);
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public StreamObserver<MessageRequest> deleteMessage(StreamObserver<ForumResponse> responseObserver) {
        return super.deleteMessage(responseObserver);
    }
}
