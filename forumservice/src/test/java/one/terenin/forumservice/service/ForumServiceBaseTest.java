package one.terenin.forumservice.service;

import io.grpc.stub.StreamObserver;
import one.terenin.forumservice.entity.ForumEntity;
import one.terenin.forumservice.entity.MessageEntity;
import one.terenin.forumservice.grpc.ForumCreationRequest;
import one.terenin.forumservice.grpc.ForumGetRequest;
import one.terenin.forumservice.grpc.ForumResponse;
import one.terenin.forumservice.grpc.ForumSuccessDeletion;
import one.terenin.forumservice.grpc.MessageRequest;
import one.terenin.forumservice.mapper.ForumMapper;
import one.terenin.forumservice.mapper.ForumMapperImpl;
import one.terenin.forumservice.mapper.MessageMapper;
import one.terenin.forumservice.mapper.MessageMapperImpl;
import one.terenin.forumservice.repository.ForumRepository;
import one.terenin.forumservice.repository.MessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.anyOf;
import static org.junit.jupiter.api.Assertions.*;
import static reactor.core.publisher.Mono.when;

class ForumServiceBaseTest {

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private ForumRepository forumRepository;

    private final MessageMapper messageMapper = new MessageMapperImpl();

    private final ForumMapper forumMapper = new ForumMapperImpl();

    private ForumServiceBase forumServiceBase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        forumServiceBase = new ForumServiceBase(messageRepository, forumRepository, messageMapper, forumMapper);
    }

    @Test
    void createForum() {
        ForumCreationRequest request = ForumCreationRequest.newBuilder().setTitle("Test Forum").build();
        ForumEntity entity = ForumEntity.builder().title("Test Forum").build();
        ForumResponse expectedResponse = ForumResponse.newBuilder().setTitle("Test Forum").build();

        StreamObserver<ForumResponse> responseObserver = new StreamObserver<>() {
            @Override
            public void onNext(ForumResponse value) {
                StepVerifier.create(Mono.just(value))
                        .expectNext(expectedResponse)
                        .verifyComplete();
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
            }

            @Override
            public void onCompleted() {
            }
        };

        forumServiceBase.createForum(request, responseObserver);
    }

    private Object anyOf(Class<ForumEntity> forumEntityClass) {
        return null;
    }

    @Test
    void findForum() {
        ForumGetRequest request = ForumGetRequest.newBuilder().setTitle("Test Forum").build();
        ForumEntity entity = ForumEntity.builder().title("Test Forum").build();
        ForumResponse expectedResponse = ForumResponse.newBuilder().setTitle("Test Forum").build();

        when(forumRepository.findByTitle(any(String.class).toString())).thenReturn(Mono.just(entity));
        when((Publisher<?>) forumMapper.map(ForumCreationRequest.newBuilder().build())).thenReturn(expectedResponse);

        StreamObserver<ForumResponse> responseObserver = new StreamObserver<>() {
            @Override
            public void onNext(ForumResponse value) {
                StepVerifier.create(Mono.just(value))
                        .expectNext(expectedResponse)
                        .verifyComplete();
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
            }

            @Override
            public void onCompleted() {
            }
        };

        forumServiceBase.findForum(request, responseObserver);
    }

    @Test
    void deleteForum() {
        ForumGetRequest request = ForumGetRequest.newBuilder().setTitle("Test Forum").build();
        ForumSuccessDeletion expectedResponse = ForumSuccessDeletion.newBuilder().setTitle("Test Forum").build();

        StreamObserver<ForumSuccessDeletion> responseObserver = new StreamObserver<>() {
            @Override
            public void onNext(ForumSuccessDeletion value) {
                StepVerifier.create(Mono.just(value))
                        .expectNext(expectedResponse)
                        .verifyComplete();
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
            }

            @Override
            public void onCompleted() {
            }
        };

        forumServiceBase.deleteForum(request, responseObserver);
    }

    @Test
    void sendMessage() {
        MessageRequest request = MessageRequest.newBuilder()
                .setForumDestinationId(UUID.randomUUID().toString())
                .setForumDestinationTitle("Test Forum")
                .setContent("Test Message")
                .build();

        MessageEntity entity = MessageEntity.builder()
                .forum(UUID.fromString(request.getForumDestinationId()))
                .forumTitle(request.getForumDestinationTitle())
                .content(request.getContent())
                .messageId(UUID.randomUUID())
                .build();

        ForumEntity forumEntity = ForumEntity.builder()
                .title(request.getForumDestinationTitle())
                .messages(null)
                .build();

        ForumResponse expectedResponse = ForumResponse.newBuilder().setTitle(request.getForumDestinationTitle()).build();


        StreamObserver<ForumResponse> responseObserver = new StreamObserver<>() {
            @Override
            public void onNext(ForumResponse value) {
                StepVerifier.create(Mono.just(value))
                        .expectNext(expectedResponse)
                        .verifyComplete();
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
            }

            @Override
            public void onCompleted() {
            }
        };

        StreamObserver<MessageRequest> messageRequestStreamObserver = forumServiceBase.sendMessage(responseObserver);
        messageRequestStreamObserver.onNext(request);
        messageRequestStreamObserver.onCompleted();
    }
}