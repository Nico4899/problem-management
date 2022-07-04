package edu.kit.tm.cm.smartcampus.problemmanagement.api.controller;

import edu.kit.tm.cm.proto.*;
import edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.manager.ProblemManagementManager;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.Problem;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.state.ProblemState;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.state.StateOperation;
import io.grpc.stub.StreamObserver;

import java.sql.Timestamp;
import java.util.Collection;

public class ProblemManagementController extends ProblemManagementGrpc.ProblemManagementImplBase {

  private final ProblemManagementManager problemManagementManager;

  public ProblemManagementController(ProblemManagementManager problemManagementManager) {
    this.problemManagementManager = problemManagementManager;
  }

  @Override
  public void listProblems(
      ListProblemsRequest request, StreamObserver<ListProblemsResponse> responseObserver) {

    ListProblemsResponse response =
        ListProblemsResponse.newBuilder()
            .setProblems(writeProblems(this.problemManagementManager.listProblems()))
            .setResponseMessage(writeResponseMessage("hello", true))
            .build();
    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }

  @Override
  public void getProblem(
      GetProblemRequest request, StreamObserver<GetProblemResponse> responseObserver) {

    GetProblemResponse response =
        GetProblemResponse.newBuilder()
            .setProblem(
                writeProblem(
                    this.problemManagementManager.getProblem(request.getIdentificationNumber())))
            .setResponseMessage(writeResponseMessage("hello", true))
            .build();
    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }

  @Override
  public void createProblem(
      CreateProblemRequest request, StreamObserver<CreateProblemResponse> responseObserver) {

    CreateProblemResponse response =
        CreateProblemResponse.newBuilder()
            .setProblem(
                writeProblem(
                    this.problemManagementManager.createProblem(readProblem(request.getProblem()))))
            .setResponseMessage(writeResponseMessage("hello", true))
            .build();
    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }

  @Override
  public void updateProblem(
      UpdateProblemRequest request, StreamObserver<UpdateProblemResponse> responseObserver) {

    UpdateProblemResponse response =
        UpdateProblemResponse.newBuilder()
            .setProblem(
                writeProblem(
                    this.problemManagementManager.updateProblem(readProblem(request.getProblem()))))
            .setResponseMessage(writeResponseMessage("hello", true))
            .build();
    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }

  @Override
  public void removeProblem(
      RemoveProblemRequest request, StreamObserver<RemoveProblemResponse> responseObserver) {
    this.problemManagementManager.removeProblem(request.getIdentificationNumber());
    RemoveProblemResponse response =
        RemoveProblemResponse.newBuilder()
            .setResponseMessage(writeResponseMessage("hello", true))
            .build();
    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }

  @Override
  public void acceptProblem(
      ChangeStateRequest request, StreamObserver<ChangeStateResponse> responseObserver) {
    this.problemManagementManager.acceptProblem(request.getIdentificationNumber());
    ChangeStateResponse response =
        ChangeStateResponse.newBuilder()
            .setResponseMessage(writeResponseMessage("hello", true))
            .build();
    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }

  @Override
  public void holdProblem(
      ChangeStateRequest request, StreamObserver<ChangeStateResponse> responseObserver) {
    this.problemManagementManager.holdProblem(request.getIdentificationNumber());
    ChangeStateResponse response =
        ChangeStateResponse.newBuilder()
            .setResponseMessage(writeResponseMessage("hello", true))
            .build();
    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }

  @Override
  public void closeProblem(
      ChangeStateRequest request, StreamObserver<ChangeStateResponse> responseObserver) {
    this.problemManagementManager.closeProblem(request.getIdentificationNumber());
    ChangeStateResponse response =
        ChangeStateResponse.newBuilder()
            .setResponseMessage(writeResponseMessage("hello", true))
            .build();
    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }

  @Override
  public void approachProblem(
      ChangeStateRequest request, StreamObserver<ChangeStateResponse> responseObserver) {
    this.problemManagementManager.approachProblem(request.getIdentificationNumber());
    ChangeStateResponse response =
        ChangeStateResponse.newBuilder()
            .setResponseMessage(writeResponseMessage("hello", true))
            .build();
    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }

  @Override
  public void declineProblem(
      ChangeStateRequest request, StreamObserver<ChangeStateResponse> responseObserver) {
    this.problemManagementManager.declineProblem(request.getIdentificationNumber());
    ChangeStateResponse response =
        ChangeStateResponse.newBuilder()
            .setResponseMessage(writeResponseMessage("hello", true))
            .build();
    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }

  // read grpc object to model object

  private Problem readProblem(GrpcProblem grpcProblem) {
    Problem problem = new Problem();
    problem.setProblemDescription(grpcProblem.getProblemDescription());
    problem.setProblemReporter(grpcProblem.getProblemReporter());
    problem.setProblemState(readProblemState(grpcProblem.getProblemState()));
    problem.setProblemTitle(grpcProblem.getProblemTitle());
    problem.setNotificationIdentificationNumber(grpcProblem.getNotificationIdentificationNumber());
    problem.setCreationTime(new Timestamp(grpcProblem.getCreationTime().getNanos()));
    problem.setReferenceIdentificationNumber(grpcProblem.getReferenceIdentificationNumber());
    return problem;
  }

  private ProblemState readProblemState(GrpcProblemState grpcProblemState) {
    return ProblemState.valueOf(grpcProblemState.name());
  }

  // write model object to grpc object

  private GrpcProblem writeProblem(Problem problem) {
    return GrpcProblem.newBuilder()
        .setCreationTime(
            com.google.protobuf.Timestamp.newBuilder()
                .setNanos(problem.getCreationTime().getNanos())
                .build())
        .setProblemDescription(problem.getProblemDescription())
        .setIdentificationNumber(problem.getIdentificationNumber())
        .setProblemReporter(problem.getProblemReporter())
        .setNotificationIdentificationNumber(problem.getNotificationIdentificationNumber())
        .setProblemTitle(problem.getProblemTitle())
        .setReferenceIdentificationNumber(problem.getReferenceIdentificationNumber())
        .setProblemState(writeProblemState(problem.getProblemState()))
        .setPossibleStateOperations(
            writeStateOperations(problem.getProblemState().getPossibleOperations()))
        .build();
  }

  private GrpcStateOperations writeStateOperations(Collection<StateOperation> stateOperations) {
    return GrpcStateOperations.newBuilder()
        .addAllStateOperations(stateOperations.stream().map(this::writeStateOperation).toList())
        .build();
  }

  private GrpcStateOperation writeStateOperation(StateOperation stateOperation) {
    return GrpcStateOperation.forNumber(stateOperation.ordinal() + 1);
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
