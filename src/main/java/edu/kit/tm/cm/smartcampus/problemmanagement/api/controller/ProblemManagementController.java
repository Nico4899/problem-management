package edu.kit.tm.cm.smartcampus.problemmanagement.api.controller;

import edu.kit.tm.cm.proto.*;
import edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.exception.InvalidArgumentsException;
import edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.exception.ResourceNotFoundException;
import edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.manager.ProblemManagementManager;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.Problem;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.state.ProblemState;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.state.StateOperation;
import io.grpc.stub.StreamObserver;

import java.sql.Timestamp;
import java.util.Collection;

public class ProblemManagementController extends ProblemManagementGrpc.ProblemManagementImplBase {

  private static final String SUCCESSFUL_MESSAGE = "Operation was successful!";

  // response successful or not
  private static final boolean SUCCESSFUL = true;
  private static final boolean UNSUCCESSFUL = false;

  // operations
  public static final String GET_PROBLEMS = "GetProblems";
  public static final String GET_PROBLEM = "GetProblem";
  public static final String CREATE_PROBLEM = "CreateProblem";
  public static final String UPDATE_PROBLEM = "UpdateProblem";
  public static final String REMOVE_PROBLEM = "RemoveProblem";
  public static final String ACCEPT_PROBLEM = "AcceptProblem";
  public static final String HOLD_PROBLEM = "HoldProblem";
  public static final String CLOSE_PROBLEM = "CloseProblem";
  public static final String APPROACH_PROBLEM = "ApproachProblem";
  public static final String DECLINE_PROBLEM = "DeclineProblem";


  private final ProblemManagementManager problemManagementManager;

  public ProblemManagementController(ProblemManagementManager problemManagementManager) {
    this.problemManagementManager = problemManagementManager;
  }

  @Override
  public void listProblems(
      ListProblemsRequest request, StreamObserver<ListProblemsResponse> responseObserver) {

    try {
      ListProblemsResponse response =
              ListProblemsResponse.newBuilder()
                      .setProblems(writeProblems(this.problemManagementManager.listProblems()))
                      .setResponseMessage(writeResponseMessage(SUCCESSFUL_MESSAGE, SUCCESSFUL))
                      .build();
      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch(InvalidArgumentsException invalidArgumentsException) {
      String message = String.format(invalidArgumentsException.getMessage(), GET_PROBLEMS);
      ResponseMessage responseMessage = this.writeResponseMessage(message, UNSUCCESSFUL);

      ListProblemsResponse response =
              ListProblemsResponse.newBuilder().setResponseMessage(responseMessage).build();

      responseObserver.onNext(response);
      responseObserver.onCompleted();
    }
  }

  @Override
  public void getProblem(
      GetProblemRequest request, StreamObserver<GetProblemResponse> responseObserver) {

    try {
      GetProblemResponse response =
              GetProblemResponse.newBuilder()
                      .setProblem(
                              writeProblem(
                                      this.problemManagementManager.getProblem(request.getIdentificationNumber())))
                      .setResponseMessage(writeResponseMessage(SUCCESSFUL_MESSAGE, SUCCESSFUL))
                      .build();
      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch(InvalidArgumentsException invalidArgumentsException) {
      String message = String.format(invalidArgumentsException.getMessage(), GET_PROBLEM);
      ResponseMessage responseMessage = this.writeResponseMessage(message, UNSUCCESSFUL);

      GetProblemResponse response = GetProblemResponse.newBuilder().setResponseMessage(responseMessage).build();
      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch(ResourceNotFoundException resourceNotFoundException) {
      String message = String.format(resourceNotFoundException.getMessage(), request.getIdentificationNumber());
      ResponseMessage responseMessage = this.writeResponseMessage(message, UNSUCCESSFUL);

      GetProblemResponse response = GetProblemResponse.newBuilder().setResponseMessage(responseMessage).build();
      responseObserver.onNext(response);
      responseObserver.onCompleted();
    }
  }

  @Override
  public void createProblem(
      CreateProblemRequest request, StreamObserver<CreateProblemResponse> responseObserver) {

    try {
      CreateProblemResponse response =
              CreateProblemResponse.newBuilder()
                      .setProblem(
                              writeProblem(
                                      this.problemManagementManager.createProblem(readProblem(request.getProblem()))))
                      .setResponseMessage(writeResponseMessage(SUCCESSFUL_MESSAGE, SUCCESSFUL))
                      .build();
      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch(InvalidArgumentsException invalidArgumentsException) {
      String message = String.format(invalidArgumentsException.getMessage(), CREATE_PROBLEM);
      ResponseMessage responseMessage = this.writeResponseMessage(message, UNSUCCESSFUL);

      CreateProblemResponse response = CreateProblemResponse.newBuilder().setResponseMessage(responseMessage).build();
      responseObserver.onNext(response);
      responseObserver.onCompleted();
    }
  }

  @Override
  public void updateProblem(
      UpdateProblemRequest request, StreamObserver<UpdateProblemResponse> responseObserver) {
    try {
      UpdateProblemResponse response =
              UpdateProblemResponse.newBuilder()
                      .setProblem(
                              writeProblem(
                                      this.problemManagementManager.updateProblem(readProblem(request.getProblem()))))
                      .setResponseMessage(writeResponseMessage(SUCCESSFUL_MESSAGE, SUCCESSFUL))
                      .build();
      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch(InvalidArgumentsException invalidArgumentsException) {
      String message = String.format(invalidArgumentsException.getMessage(), UPDATE_PROBLEM);
      ResponseMessage responseMessage = this.writeResponseMessage(message, UNSUCCESSFUL);

      UpdateProblemResponse response = UpdateProblemResponse.newBuilder().setResponseMessage(responseMessage).build();
      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch(ResourceNotFoundException resourceNotFoundException) {
    String message = String.format(resourceNotFoundException.getMessage(), request.getProblem().getIdentificationNumber());
    ResponseMessage responseMessage = this.writeResponseMessage(message, UNSUCCESSFUL);

    UpdateProblemResponse response = UpdateProblemResponse.newBuilder().setResponseMessage(responseMessage).build();
    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }
  }

  @Override
  public void removeProblem(
      RemoveProblemRequest request, StreamObserver<RemoveProblemResponse> responseObserver) {
    try {
      this.problemManagementManager.removeProblem(request.getIdentificationNumber());
      RemoveProblemResponse response =
              RemoveProblemResponse.newBuilder()
                      .setResponseMessage(writeResponseMessage(SUCCESSFUL_MESSAGE, SUCCESSFUL))
                      .build();
      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch(InvalidArgumentsException invalidArgumentsException) {
      String message = String.format(invalidArgumentsException.getMessage(), REMOVE_PROBLEM);
      ResponseMessage responseMessage = this.writeResponseMessage(message, UNSUCCESSFUL);

      RemoveProblemResponse response = RemoveProblemResponse.newBuilder().setResponseMessage(responseMessage).build();
      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch(ResourceNotFoundException resourceNotFoundException) {
      String message = String.format(resourceNotFoundException.getMessage(), request.getIdentificationNumber());
      ResponseMessage responseMessage = this.writeResponseMessage(message, UNSUCCESSFUL);

      RemoveProblemResponse response = RemoveProblemResponse.newBuilder().setResponseMessage(responseMessage).build();
      responseObserver.onNext(response);
      responseObserver.onCompleted();
    }
  }

  @Override
  public void acceptProblem(
      ChangeStateRequest request, StreamObserver<ChangeStateResponse> responseObserver) {
    try {
    this.problemManagementManager.acceptProblem(request.getIdentificationNumber());
    ChangeStateResponse response =
        ChangeStateResponse.newBuilder()
            .setResponseMessage(writeResponseMessage(SUCCESSFUL_MESSAGE, SUCCESSFUL))
            .build();
    responseObserver.onNext(response);
    responseObserver.onCompleted();
    } catch(InvalidArgumentsException invalidArgumentsException) {
      String message = String.format(invalidArgumentsException.getMessage(), ACCEPT_PROBLEM);
      ResponseMessage responseMessage = this.writeResponseMessage(message, UNSUCCESSFUL);

      ChangeStateResponse response = ChangeStateResponse.newBuilder().setResponseMessage(responseMessage).build();
      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch(ResourceNotFoundException resourceNotFoundException) {
      String message = String.format(resourceNotFoundException.getMessage(), request.getIdentificationNumber());
      ResponseMessage responseMessage = this.writeResponseMessage(message, UNSUCCESSFUL);

      ChangeStateResponse response = ChangeStateResponse.newBuilder().setResponseMessage(responseMessage).build();
      responseObserver.onNext(response);
      responseObserver.onCompleted();
    }
  }

  @Override
  public void holdProblem(
      ChangeStateRequest request, StreamObserver<ChangeStateResponse> responseObserver) {
    try {
    this.problemManagementManager.holdProblem(request.getIdentificationNumber());
    ChangeStateResponse response =
        ChangeStateResponse.newBuilder()
            .setResponseMessage(writeResponseMessage(SUCCESSFUL_MESSAGE, SUCCESSFUL))
            .build();
    responseObserver.onNext(response);
    responseObserver.onCompleted();
    } catch(InvalidArgumentsException invalidArgumentsException) {
      String message = String.format(invalidArgumentsException.getMessage(), HOLD_PROBLEM);
      ResponseMessage responseMessage = this.writeResponseMessage(message, UNSUCCESSFUL);

      ChangeStateResponse response = ChangeStateResponse.newBuilder().setResponseMessage(responseMessage).build();
      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch(ResourceNotFoundException resourceNotFoundException) {
      String message = String.format(resourceNotFoundException.getMessage(), request.getIdentificationNumber());
      ResponseMessage responseMessage = this.writeResponseMessage(message, UNSUCCESSFUL);

      ChangeStateResponse response = ChangeStateResponse.newBuilder().setResponseMessage(responseMessage).build();
      responseObserver.onNext(response);
      responseObserver.onCompleted();
    }
  }

  @Override
  public void closeProblem(
      ChangeStateRequest request, StreamObserver<ChangeStateResponse> responseObserver) {
    try {
    this.problemManagementManager.closeProblem(request.getIdentificationNumber());
    ChangeStateResponse response =
        ChangeStateResponse.newBuilder()
            .setResponseMessage(writeResponseMessage(SUCCESSFUL_MESSAGE, SUCCESSFUL))
            .build();
    responseObserver.onNext(response);
    responseObserver.onCompleted();
    } catch(InvalidArgumentsException invalidArgumentsException) {
      String message = String.format(invalidArgumentsException.getMessage(), CLOSE_PROBLEM);
      ResponseMessage responseMessage = this.writeResponseMessage(message, UNSUCCESSFUL);

      ChangeStateResponse response = ChangeStateResponse.newBuilder().setResponseMessage(responseMessage).build();
      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch(ResourceNotFoundException resourceNotFoundException) {
      String message = String.format(resourceNotFoundException.getMessage(), request.getIdentificationNumber());
      ResponseMessage responseMessage = this.writeResponseMessage(message, UNSUCCESSFUL);

      ChangeStateResponse response = ChangeStateResponse.newBuilder().setResponseMessage(responseMessage).build();
      responseObserver.onNext(response);
      responseObserver.onCompleted();
    }
  }

  @Override
  public void approachProblem(
      ChangeStateRequest request, StreamObserver<ChangeStateResponse> responseObserver) {
    try {
    this.problemManagementManager.approachProblem(request.getIdentificationNumber());
    ChangeStateResponse response =
        ChangeStateResponse.newBuilder()
            .setResponseMessage(writeResponseMessage(SUCCESSFUL_MESSAGE, SUCCESSFUL))
            .build();
    responseObserver.onNext(response);
    responseObserver.onCompleted();
    } catch(InvalidArgumentsException invalidArgumentsException) {
      String message = String.format(invalidArgumentsException.getMessage(), APPROACH_PROBLEM);
      ResponseMessage responseMessage = this.writeResponseMessage(message, UNSUCCESSFUL);

      ChangeStateResponse response = ChangeStateResponse.newBuilder().setResponseMessage(responseMessage).build();
      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch(ResourceNotFoundException resourceNotFoundException) {
      String message = String.format(resourceNotFoundException.getMessage(), request.getIdentificationNumber());
      ResponseMessage responseMessage = this.writeResponseMessage(message, UNSUCCESSFUL);

      ChangeStateResponse response = ChangeStateResponse.newBuilder().setResponseMessage(responseMessage).build();
      responseObserver.onNext(response);
      responseObserver.onCompleted();
    }
  }

  @Override
  public void declineProblem(
      ChangeStateRequest request, StreamObserver<ChangeStateResponse> responseObserver) {
    try {
    this.problemManagementManager.declineProblem(request.getIdentificationNumber());
    ChangeStateResponse response =
        ChangeStateResponse.newBuilder()
            .setResponseMessage(writeResponseMessage(SUCCESSFUL_MESSAGE, SUCCESSFUL))
            .build();
    responseObserver.onNext(response);
    responseObserver.onCompleted();
    } catch(InvalidArgumentsException invalidArgumentsException) {
      String message = String.format(invalidArgumentsException.getMessage(), DECLINE_PROBLEM);
      ResponseMessage responseMessage = this.writeResponseMessage(message, UNSUCCESSFUL);

      ChangeStateResponse response = ChangeStateResponse.newBuilder().setResponseMessage(responseMessage).build();
      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch(ResourceNotFoundException resourceNotFoundException) {
      String message = String.format(resourceNotFoundException.getMessage(), request.getIdentificationNumber());
      ResponseMessage responseMessage = this.writeResponseMessage(message, UNSUCCESSFUL);

      ChangeStateResponse response = ChangeStateResponse.newBuilder().setResponseMessage(responseMessage).build();
      responseObserver.onNext(response);
      responseObserver.onCompleted();
    }
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
    return ProblemState.forNumber(grpcProblemState.ordinal() + 1);
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
