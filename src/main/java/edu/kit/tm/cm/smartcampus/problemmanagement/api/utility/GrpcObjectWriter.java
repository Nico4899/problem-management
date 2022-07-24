package edu.kit.tm.cm.smartcampus.problemmanagement.api.utility;

import edu.kit.tm.cm.proto.GrpcProblem;
import edu.kit.tm.cm.proto.GrpcProblemState;
import edu.kit.tm.cm.proto.GrpcStateOperation;
import edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.exception.InternalServerErrorException;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.Problem;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Component;

import java.util.Collection;

/** This class provides a collection of logic methods to translate model objects to grpc objects. */
@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class GrpcObjectWriter {

  /**
   * Write a model object to a grpc object.
   *
   * @param problem problem model object
   * @return grpc problem object
   */
  public static GrpcProblem write(Problem problem) {
    return GrpcProblem.newBuilder()
        .setCreationTime(
            com.google.protobuf.Timestamp.newBuilder()
                .setNanos(problem.getCreationTime().getNanos())
                .build())
        .setProblemDescription(problem.getDescription())
        .setProblemReporter(problem.getReporter())
        .setProblemTitle(problem.getTitle())
        .setReferenceIdentificationNumber(problem.getReferenceIdentificationNumber())
        .setProblemState(write(problem.getState()))
        .setIdentificationNumber(problem.getIdentificationNumber())
        .build();
  }

  /**
   * Write a model object to a grpc object.
   *
   * @param stateOperations problem state operations model object
   * @return grpc problem state operations object
   */
  public static Collection<GrpcStateOperation> writeStateOperations(
      Collection<Problem.State.Operation> stateOperations) {
    return stateOperations.stream().map(GrpcObjectWriter::write).toList();
  }

  /**
   * Write a model object to a grpc object.
   *
   * @param stateOperation problem state operation model object
   * @return grpc problem state operation object
   */
  public static GrpcStateOperation write(Problem.State.Operation stateOperation) {
    return Enum.valueOf(GrpcStateOperation.class, stateOperation.name());  }

  /**
   * Write a model object to a grpc object.
   *
   * @param problemState problem state model object
   * @return grpc problem state object
   */
  public static GrpcProblemState write(Problem.State problemState) {
    return Enum.valueOf(GrpcProblemState.class, problemState.name());
  }

  /**
   * Write a model object to a grpc object.
   *
   * @param problems problems model object
   * @return grpc problems object
   */
  public static Collection<GrpcProblem> writeProblems(Collection<Problem> problems) {
    return problems.stream().map(GrpcObjectWriter::write).toList();
  }
}
