package edu.kit.tm.cm.smartcampus.problemmanagement.api.utility;

import edu.kit.tm.cm.proto.GrpcProblem;
import edu.kit.tm.cm.proto.GrpcProblemState;
import edu.kit.tm.cm.proto.GrpcStateOperation;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.Problem;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.ProblemState;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.StateOperation;

import java.util.Collection;

public final class GrpcObjectWriter {

  private GrpcObjectWriter() {}

  public static GrpcProblem write(Problem problem) {
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

  public static Collection<GrpcStateOperation> writeStateOperations(
      Collection<StateOperation> stateOperations) {
    return stateOperations.stream().map(GrpcObjectWriter::write).toList();
  }

  public static GrpcStateOperation write(StateOperation stateOperation) {
    return GrpcStateOperation.forNumber(stateOperation.ordinal() + 1);
  }

  public static GrpcProblemState write(ProblemState problemState) {
    return GrpcProblemState.forNumber(problemState.ordinal() + 1);
  }

  public static Collection<GrpcProblem> writeProblems(Collection<Problem> problems) {
    return problems.stream().map(GrpcObjectWriter::write).toList();
  }
}
