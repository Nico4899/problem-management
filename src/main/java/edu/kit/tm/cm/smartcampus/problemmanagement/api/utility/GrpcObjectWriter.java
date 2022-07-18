package edu.kit.tm.cm.smartcampus.problemmanagement.api.utility;

import edu.kit.tm.cm.proto.GrpcProblem;
import edu.kit.tm.cm.proto.GrpcProblemState;
import edu.kit.tm.cm.proto.GrpcStateOperation;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.Problem;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.ProblemState;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.StateOperation;
import org.springframework.stereotype.Component;

import java.util.Collection;

/** This class provides a collection of logic methods to translate model objects to grpc objects. */
@Component
public class GrpcObjectWriter {

  /** Construct a new grpc object writer. */
  public GrpcObjectWriter() {}

  /**
   * Write a model object to a grpc object.
   *
   * @param problem problem model object
   * @return grpc problem object
   */
  public GrpcProblem write(Problem problem) {
    return GrpcProblem.newBuilder()
        .setCreationTime(
            com.google.protobuf.Timestamp.newBuilder()
                .setNanos(problem.getCreationTime().getNanos())
                .build())
        .setProblemDescription(problem.getProblemDescription())
        .setProblemReporter(problem.getProblemReporter())
        .setProblemTitle(problem.getProblemTitle())
        .setReferenceIdentificationNumber(problem.getReferenceIdentificationNumber())
        .setProblemState(write(problem.getProblemState()))
        .setIdentificationNumber(problem.getIdentificationNumber())
        .build();
  }

  /**
   * Write a model object to a grpc object.
   *
   * @param stateOperations problem state operations model object
   * @return grpc problem state operations object
   */
  public Collection<GrpcStateOperation> writeStateOperations(
      Collection<StateOperation> stateOperations) {
    return stateOperations.stream().map(this::write).toList();
  }

  /**
   * Write a model object to a grpc object.
   *
   * @param stateOperation problem state operation model object
   * @return grpc problem state operation object
   */
  public GrpcStateOperation write(StateOperation stateOperation) {
    return GrpcStateOperation.forNumber(stateOperation.ordinal() + 1);
  }

  /**
   * Write a model object to a grpc object.
   *
   * @param problemState problem state model object
   * @return grpc problem state object
   */
  public GrpcProblemState write(ProblemState problemState) {
    return GrpcProblemState.forNumber(problemState.ordinal() + 1);
  }

  /**
   * Write a model object to a grpc object.
   *
   * @param problems problems model object
   * @return grpc problems object
   */
  public Collection<GrpcProblem> writeProblems(Collection<Problem> problems) {
    return problems.stream().map(this::write).toList();
  }
}
