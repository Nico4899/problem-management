package edu.kit.tm.cm.smartcampus.problemmanagement.api.controller;

import com.google.protobuf.Timestamp;
import edu.kit.tm.cm.proto.*;
import edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.manager.ProblemManagementManager;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.Problem;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.ProblemState;
import io.grpc.stub.StreamObserver;

import java.util.Collection;

public class ProblemManagementController extends ProblemManagementGrpc.ProblemManagementImplBase {

  private final ProblemManagementManager problemManagementManager;

  public ProblemManagementController(ProblemManagementManager problemManagementManager) {
    this.problemManagementManager = problemManagementManager;
  }

  @Override
  public void listProblems(
      ListProblemsRequest request, StreamObserver<ListProblemsResponse> responseObserver) {}

  @Override
  public void getProblem(
      GetProblemRequest request, StreamObserver<GetProblemResponse> responseObserver) {}

  @Override
  public void createProblem(
      CreateProblemRequest request, StreamObserver<CreateProblemResponse> responseObserver) {}

  @Override
  public void updateProblem(
      UpdateProblemRequest request, StreamObserver<UpdateProblemResponse> responseObserver) {}

  @Override
  public void removeProblem(
      RemoveProblemRequest request, StreamObserver<RemoveProblemResponse> responseObserver) {}

  @Override
  public void acceptProblem(
      ChangeStateRequest request, StreamObserver<ChangeStateResponse> responseObserver) {}

  @Override
  public void holdProblem(
    ChangeStateRequest request, StreamObserver<ChangeStateResponse> responseObserver) {}

  @Override
  public void closeProblem(
    ChangeStateRequest request, StreamObserver<ChangeStateResponse> responseObserver) {}

  @Override
  public void approachProblem(
    ChangeStateRequest request, StreamObserver<ChangeStateResponse> responseObserver) {}

  @Override
  public void declineProblem(
    ChangeStateRequest request, StreamObserver<ChangeStateResponse> responseObserver) {}

  private GrpcProblem writeProblem(Problem problem) {
    return GrpcProblem.newBuilder()
        .setCreationTime(
            Timestamp.newBuilder().setNanos(problem.getCreationTime().getNanos()).build())
        .setProblemDescription(problem.getProblemDescription())
        .setIdentificationNumber(problem.getIdentificationNumber())
        .setProblemReporter(problem.getProblemReporter())
        .setNotificationIdentificationNumber(problem.getNotificationIdentificationNumber())
        .setProblemTitle(problem.getProblemTitle())
        .setReferenceIdentificationNumber(problem.getReferenceIdentificationNumber())
        .setProblemState(writeProblemState(problem.getProblemState()))
        .build();
  }

  private GrpcProblemState writeProblemState(ProblemState problemState) {
    return GrpcProblemState.forNumber(problemState.ordinal() + 1);
  }

  private ResponseMessage writeResponseMessage(String message, boolean successful) {
    return ResponseMessage.newBuilder().setMessage(message).setSuccessful(successful).build();
  }

  private GrpcProblems writeProblems(Collection<Problem> problems) {
    return GrpcProblems.newBuilder()
        .addAllProblems(problems.stream().map(this::writeProblem).toList())
        .build();
  }
}
