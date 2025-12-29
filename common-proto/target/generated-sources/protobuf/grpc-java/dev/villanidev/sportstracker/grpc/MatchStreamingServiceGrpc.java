package dev.villanidev.sportstracker.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 * <pre>
 * Streaming service for per-match events
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.65.1)",
    comments = "Source: match_tracker.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class MatchStreamingServiceGrpc {

  private MatchStreamingServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "matchtracker.MatchStreamingService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<dev.villanidev.sportstracker.grpc.SubscribeMatchEventsRequest,
      dev.villanidev.sportstracker.grpc.MatchEventUpdate> getSubscribeMatchEventsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SubscribeMatchEvents",
      requestType = dev.villanidev.sportstracker.grpc.SubscribeMatchEventsRequest.class,
      responseType = dev.villanidev.sportstracker.grpc.MatchEventUpdate.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<dev.villanidev.sportstracker.grpc.SubscribeMatchEventsRequest,
      dev.villanidev.sportstracker.grpc.MatchEventUpdate> getSubscribeMatchEventsMethod() {
    io.grpc.MethodDescriptor<dev.villanidev.sportstracker.grpc.SubscribeMatchEventsRequest, dev.villanidev.sportstracker.grpc.MatchEventUpdate> getSubscribeMatchEventsMethod;
    if ((getSubscribeMatchEventsMethod = MatchStreamingServiceGrpc.getSubscribeMatchEventsMethod) == null) {
      synchronized (MatchStreamingServiceGrpc.class) {
        if ((getSubscribeMatchEventsMethod = MatchStreamingServiceGrpc.getSubscribeMatchEventsMethod) == null) {
          MatchStreamingServiceGrpc.getSubscribeMatchEventsMethod = getSubscribeMatchEventsMethod =
              io.grpc.MethodDescriptor.<dev.villanidev.sportstracker.grpc.SubscribeMatchEventsRequest, dev.villanidev.sportstracker.grpc.MatchEventUpdate>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SubscribeMatchEvents"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  dev.villanidev.sportstracker.grpc.SubscribeMatchEventsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  dev.villanidev.sportstracker.grpc.MatchEventUpdate.getDefaultInstance()))
              .setSchemaDescriptor(new MatchStreamingServiceMethodDescriptorSupplier("SubscribeMatchEvents"))
              .build();
        }
      }
    }
    return getSubscribeMatchEventsMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static MatchStreamingServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<MatchStreamingServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<MatchStreamingServiceStub>() {
        @java.lang.Override
        public MatchStreamingServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new MatchStreamingServiceStub(channel, callOptions);
        }
      };
    return MatchStreamingServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static MatchStreamingServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<MatchStreamingServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<MatchStreamingServiceBlockingStub>() {
        @java.lang.Override
        public MatchStreamingServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new MatchStreamingServiceBlockingStub(channel, callOptions);
        }
      };
    return MatchStreamingServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static MatchStreamingServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<MatchStreamingServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<MatchStreamingServiceFutureStub>() {
        @java.lang.Override
        public MatchStreamingServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new MatchStreamingServiceFutureStub(channel, callOptions);
        }
      };
    return MatchStreamingServiceFutureStub.newStub(factory, channel);
  }

  /**
   * <pre>
   * Streaming service for per-match events
   * </pre>
   */
  public interface AsyncService {

    /**
     * <pre>
     * Subscribe to a single match's event stream.
     * Semantics:
     * - On connect: server sends full historical events for the match
     * - Then: streams new events as they occur until client disconnects
     * </pre>
     */
    default void subscribeMatchEvents(dev.villanidev.sportstracker.grpc.SubscribeMatchEventsRequest request,
        io.grpc.stub.StreamObserver<dev.villanidev.sportstracker.grpc.MatchEventUpdate> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSubscribeMatchEventsMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service MatchStreamingService.
   * <pre>
   * Streaming service for per-match events
   * </pre>
   */
  public static abstract class MatchStreamingServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return MatchStreamingServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service MatchStreamingService.
   * <pre>
   * Streaming service for per-match events
   * </pre>
   */
  public static final class MatchStreamingServiceStub
      extends io.grpc.stub.AbstractAsyncStub<MatchStreamingServiceStub> {
    private MatchStreamingServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MatchStreamingServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new MatchStreamingServiceStub(channel, callOptions);
    }

    /**
     * <pre>
     * Subscribe to a single match's event stream.
     * Semantics:
     * - On connect: server sends full historical events for the match
     * - Then: streams new events as they occur until client disconnects
     * </pre>
     */
    public void subscribeMatchEvents(dev.villanidev.sportstracker.grpc.SubscribeMatchEventsRequest request,
        io.grpc.stub.StreamObserver<dev.villanidev.sportstracker.grpc.MatchEventUpdate> responseObserver) {
      io.grpc.stub.ClientCalls.asyncServerStreamingCall(
          getChannel().newCall(getSubscribeMatchEventsMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service MatchStreamingService.
   * <pre>
   * Streaming service for per-match events
   * </pre>
   */
  public static final class MatchStreamingServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<MatchStreamingServiceBlockingStub> {
    private MatchStreamingServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MatchStreamingServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new MatchStreamingServiceBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * Subscribe to a single match's event stream.
     * Semantics:
     * - On connect: server sends full historical events for the match
     * - Then: streams new events as they occur until client disconnects
     * </pre>
     */
    public java.util.Iterator<dev.villanidev.sportstracker.grpc.MatchEventUpdate> subscribeMatchEvents(
        dev.villanidev.sportstracker.grpc.SubscribeMatchEventsRequest request) {
      return io.grpc.stub.ClientCalls.blockingServerStreamingCall(
          getChannel(), getSubscribeMatchEventsMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service MatchStreamingService.
   * <pre>
   * Streaming service for per-match events
   * </pre>
   */
  public static final class MatchStreamingServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<MatchStreamingServiceFutureStub> {
    private MatchStreamingServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MatchStreamingServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new MatchStreamingServiceFutureStub(channel, callOptions);
    }
  }

  private static final int METHODID_SUBSCRIBE_MATCH_EVENTS = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_SUBSCRIBE_MATCH_EVENTS:
          serviceImpl.subscribeMatchEvents((dev.villanidev.sportstracker.grpc.SubscribeMatchEventsRequest) request,
              (io.grpc.stub.StreamObserver<dev.villanidev.sportstracker.grpc.MatchEventUpdate>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getSubscribeMatchEventsMethod(),
          io.grpc.stub.ServerCalls.asyncServerStreamingCall(
            new MethodHandlers<
              dev.villanidev.sportstracker.grpc.SubscribeMatchEventsRequest,
              dev.villanidev.sportstracker.grpc.MatchEventUpdate>(
                service, METHODID_SUBSCRIBE_MATCH_EVENTS)))
        .build();
  }

  private static abstract class MatchStreamingServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    MatchStreamingServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return dev.villanidev.sportstracker.grpc.MatchTracker.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("MatchStreamingService");
    }
  }

  private static final class MatchStreamingServiceFileDescriptorSupplier
      extends MatchStreamingServiceBaseDescriptorSupplier {
    MatchStreamingServiceFileDescriptorSupplier() {}
  }

  private static final class MatchStreamingServiceMethodDescriptorSupplier
      extends MatchStreamingServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    MatchStreamingServiceMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (MatchStreamingServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new MatchStreamingServiceFileDescriptorSupplier())
              .addMethod(getSubscribeMatchEventsMethod())
              .build();
        }
      }
    }
    return result;
  }
}
