package edu.kit.tm.cm.smartcampus.problemmanagement.api.controller;

import edu.kit.tm.cm.proto.*;
import edu.kit.tm.cm.smartcampus.problemmanagement.api.error.GrpcServerErrorHandler;
import edu.kit.tm.cm.smartcampus.problemmanagement.api.utility.GrpcObjectReader;
import edu.kit.tm.cm.smartcampus.problemmanagement.api.utility.GrpcObjectWriter;
import edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.service.ProblemManagementService;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.Problem;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.StateOperation;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.filter.options.FilterOptions;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Controller;

import java.util.Collection;

@Controller
public class ProblemManagementController extends ProblemManagementGrpc.ProblemManagementImplBase {

  private final ProblemManagementService problemManagementService;

  public ProblemManagementController(ProblemManagementService problemManagementService) {
    this.problemManagementService = problemManagementService;
  }

  @Override
  public void listProblems(
      ListProblemsRequest request, StreamObserver<ListProblemsResponse> responseObserver) {
    GrpcServerErrorHandler<ListProblemsRequest, ListProblemsResponse> grpcServerErrorHandler =
        new GrpcServerErrorHandler<>(responseObserver);
    ListProblemsResponse response =
        grpcServerErrorHandler.execute(
            x -> {
              ProblemFilterOptions problemFilterOptions = request.getProblemFilterOptions();
              FilterOptions filterOptions = GrpcObjectReader.read(problemFilterOptions);
              Collection<Problem> problems =
                  this.problemManagementService.listProblems(filterOptions);
              Collection<GrpcProblem> grpcProblems = GrpcObjectWriter.writeProblems(problems);

              return ListProblemsResponse.newBuilder().addAllProblems(grpcProblems).build();
            },
            request);
    grpcServerErrorHandler.onNext(response);
    grpcServerErrorHandler.onCompleted();
  }

  @Override
  public void getProblem(
      GetProblemRequest request, StreamObserver<GetProblemResponse> responseObserver) {
    GrpcServerErrorHandler<GetProblemRequest, GetProblemResponse> grpcServerErrorHandler =
        new GrpcServerErrorHandler<>(responseObserver);
    GetProblemResponse response =
        grpcServerErrorHandler.execute(
            x -> {
              String identificationNumber = request.getIdentificationNumber();
              Problem problem = this.problemManagementService.getProblem(identificationNumber);
              GrpcProblem grpcProblem = GrpcObjectWriter.write(problem);
              Collection<StateOperation> stateOperations =
                  problem.getProblemState().getPossibleOperations();
              Collection<GrpcStateOperation> grpcStateOperations = GrpcObjectWriter.writeStateOperations(stateOperations);

              return GetProblemResponse.newBuilder()
                  .setProblem(grpcProblem)
                  .addAllPossibleStateOperations(grpcStateOperations)
                  .build();
            },
            request);
    grpcServerErrorHandler.onNext(response);
    grpcServerErrorHandler.onCompleted();
  }

  @Override
  public void createProblem(
      CreateProblemRequest request, StreamObserver<CreateProblemResponse> responseObserver) {
    GrpcServerErrorHandler<CreateProblemRequest, CreateProblemResponse> grpcServerErrorHandler =
        new GrpcServerErrorHandler<>(responseObserver);
    CreateProblemResponse response =
        grpcServerErrorHandler.execute(
            x -> {
              GrpcProblem grpcRequestProblem = request.getProblem();
              Problem requestProblem = GrpcObjectReader.read(grpcRequestProblem);
              Problem responseProblem = this.problemManagementService.createProblem(requestProblem);
              GrpcProblem grpcResponseProblem = GrpcObjectWriter.write(responseProblem);

              return CreateProblemResponse.newBuilder().setProblem(grpcResponseProblem).build();
            },
            request);
    grpcServerErrorHandler.onNext(response);
    grpcServerErrorHandler.onCompleted();
  }

  @Override
  public void updateProblem(
      UpdateProblemRequest request, StreamObserver<UpdateProblemResponse> responseObserver) {
    GrpcServerErrorHandler<UpdateProblemRequest, UpdateProblemResponse> grpcServerErrorHandler =
        new GrpcServerErrorHandler<>(responseObserver);
    UpdateProblemResponse response =
        grpcServerErrorHandler.execute(
            x -> {
              String identificationNumber = request.getIdentificationNumber();
              GrpcProblem grpcRequestProblem = request.getProblem();
              Problem requestProblem = GrpcObjectReader.read(grpcRequestProblem);
              requestProblem.setIdentificationNumber(identificationNumber);
              Problem responseProblem = this.problemManagementService.updateProblem(requestProblem);
              GrpcProblem grpcResponseProblem = GrpcObjectWriter.write(responseProblem);

              return UpdateProblemResponse.newBuilder().setProblem(grpcResponseProblem).build();
            },
            request);
    grpcServerErrorHandler.onNext(response);
    grpcServerErrorHandler.onCompleted();
  }

  @Override
  public void removeProblem(
      RemoveProblemRequest request, StreamObserver<RemoveProblemResponse> responseObserver) {
    GrpcServerErrorHandler<RemoveProblemRequest, RemoveProblemResponse> grpcServerErrorHandler =
        new GrpcServerErrorHandler<>(responseObserver);
    RemoveProblemResponse response =
        grpcServerErrorHandler.execute(
            x -> {
              String identificationNumber = request.getIdentificationNumber();
              this.problemManagementService.removeProblem(identificationNumber);

              return RemoveProblemResponse.newBuilder().build();
            },
            request);
    grpcServerErrorHandler.onNext(response);
    grpcServerErrorHandler.onCompleted();
  }

  @Override
  public void acceptProblem(
      ChangeStateRequest request, StreamObserver<ChangeStateResponse> responseObserver) {
    GrpcServerErrorHandler<ChangeStateRequest, ChangeStateResponse> grpcServerErrorHandler =
        new GrpcServerErrorHandler<>(responseObserver);
    ChangeStateResponse response =
        grpcServerErrorHandler.execute(
            x -> {
              String identificationNumber = request.getIdentificationNumber();
              this.problemManagementService.acceptProblem(identificationNumber);

              return ChangeStateResponse.newBuilder().build();
            },
            request);
    grpcServerErrorHandler.onNext(response);
    grpcServerErrorHandler.onCompleted();
  }

  @Override
  public void holdProblem(
      ChangeStateRequest request, StreamObserver<ChangeStateResponse> responseObserver) {
    GrpcServerErrorHandler<ChangeStateRequest, ChangeStateResponse> grpcServerErrorHandler =
        new GrpcServerErrorHandler<>(responseObserver);
    ChangeStateResponse response =
        grpcServerErrorHandler.execute(
            x -> {
              String identificationNumber = request.getIdentificationNumber();
              this.problemManagementService.holdProblem(identificationNumber);

              return ChangeStateResponse.newBuilder().build();
            },
            request);
    grpcServerErrorHandler.onNext(response);
    grpcServerErrorHandler.onCompleted();
  }

  @Override
  public void closeProblem(
      ChangeStateRequest request, StreamObserver<ChangeStateResponse> responseObserver) {
    GrpcServerErrorHandler<ChangeStateRequest, ChangeStateResponse> grpcServerErrorHandler =
        new GrpcServerErrorHandler<>(responseObserver);
    ChangeStateResponse response =
        grpcServerErrorHandler.execute(
            x -> {
              String identificationNumber = request.getIdentificationNumber();
              this.problemManagementService.closeProblem(identificationNumber);

              return ChangeStateResponse.newBuilder().build();
            },
            request);
    grpcServerErrorHandler.onNext(response);
    grpcServerErrorHandler.onCompleted();
  }

  @Override
  public void approachProblem(
      ChangeStateRequest request, StreamObserver<ChangeStateResponse> responseObserver) {
    GrpcServerErrorHandler<ChangeStateRequest, ChangeStateResponse> grpcServerErrorHandler =
        new GrpcServerErrorHandler<>(responseObserver);
    ChangeStateResponse response =
        grpcServerErrorHandler.execute(
            x -> {
              String identificationNumber = request.getIdentificationNumber();
              this.problemManagementService.approachProblem(identificationNumber);

              return ChangeStateResponse.newBuilder().build();
            },
            request);
    grpcServerErrorHandler.onNext(response);
    grpcServerErrorHandler.onCompleted();
  }

  @Override
  public void declineProblem(
      ChangeStateRequest request, StreamObserver<ChangeStateResponse> responseObserver) {
    GrpcServerErrorHandler<ChangeStateRequest, ChangeStateResponse> grpcServerErrorHandler =
        new GrpcServerErrorHandler<>(responseObserver);
    ChangeStateResponse response =
        grpcServerErrorHandler.execute(
            x -> {
              String identificationNumber = request.getIdentificationNumber();
              this.problemManagementService.declineProblem(identificationNumber);

              return ChangeStateResponse.newBuilder().build();
            },
            request);
    grpcServerErrorHandler.onNext(response);
    grpcServerErrorHandler.onCompleted();
  }
}
