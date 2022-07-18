package edu.kit.tm.cm.smartcampus.problemmanagement.api.error;

import com.google.protobuf.Message;
import com.google.rpc.ErrorInfo;
import edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.exception.InvalidArgumentsException;
import edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.exception.InvalidStateChangeRequestException;
import edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.exception.ResourceNotFoundException;
import io.grpc.Metadata;
import io.grpc.Status;
import io.grpc.protobuf.ProtoUtils;
import io.grpc.stub.StreamObserver;
import lombok.AllArgsConstructor;

import java.util.function.Function;

/**
 * This class is being used as wrapper class for the {@link StreamObserver} of the building management service.
 * It reduces redundant try catch procedures and ensures error handling in the application. Just as many stream
 * observers are, this is a thread unsafe implementation.
 *
 * @param <S> request generic
 * @param <T> response generic
 */
@AllArgsConstructor
public class GrpcServerErrorHandler<S extends Message, T extends Message> implements StreamObserver<T> {

  private final StreamObserver<T> grpcResponseObserver;

  @Override
  public void onNext(T response) {
    this.grpcResponseObserver.onNext(response);
  }

  @Override
  public void onError(Throwable throwable) {
    if (throwable instanceof InvalidArgumentsException invalidArgumentsException) {
      Metadata trailers = new Metadata();
      ErrorInfo errorInfo = ErrorInfo.newBuilder().setReason(invalidArgumentsException.getMessage()).build();
      Metadata.Key<ErrorInfo> errorInfoTrailerKey = ProtoUtils.keyForProto(errorInfo);
      trailers.put(errorInfoTrailerKey, errorInfo);
      this.grpcResponseObserver.onError(Status.INVALID_ARGUMENT.withCause(invalidArgumentsException).asRuntimeException(trailers));
    } else if (throwable instanceof ResourceNotFoundException resourceNotFoundException) {
      Metadata trailers = new Metadata();
      ErrorInfo errorInfo = ErrorInfo.newBuilder().setReason(resourceNotFoundException.getMessage()).build();
      Metadata.Key<ErrorInfo> errorInfoTrailerKey = ProtoUtils.keyForProto(errorInfo);
      trailers.put(errorInfoTrailerKey, errorInfo);
      this.grpcResponseObserver.onError(Status.NOT_FOUND.withCause(resourceNotFoundException).asRuntimeException(trailers));
    } else if (throwable instanceof InvalidStateChangeRequestException invalidStateChangeRequestException) {
      Metadata trailers = new Metadata();
      ErrorInfo errorInfo = ErrorInfo.newBuilder().setReason(invalidStateChangeRequestException.getMessage()).build();
      Metadata.Key<ErrorInfo> errorInfoTrailerKey = ProtoUtils.keyForProto(errorInfo);
      trailers.put(errorInfoTrailerKey, errorInfo);
      this.grpcResponseObserver.onError(Status.PERMISSION_DENIED.withCause(invalidStateChangeRequestException).asRuntimeException(trailers));
    } else {
      this.grpcResponseObserver.onError(Status.UNKNOWN
        .withDescription(throwable.getMessage())
        .withCause(throwable)
        .asRuntimeException());
    }
  }

  @Override
  public void onCompleted() {
    if (grpcResponseObserver != null) {
      grpcResponseObserver.onCompleted();
    }
  }

  /**
   * This is the wrapper method to run a function and catch all exceptions thrown and send them to handling.
   * If an error occurred, it calls the overridden {@link GrpcServerErrorHandler#onError(Throwable)} method.
   *
   * @param function function to be applied
   * @param request  request from client
   * @return response message
   */
  public T execute(Function<S, T> function, S request) {
    T response = null;
    try {
      response = function.apply(request);
    } catch (Exception exception) {
      this.onError(exception);
    }
    return response;
  }
}
