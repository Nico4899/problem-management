package edu.kit.tm.cm.smartcampus.problemmanagement.api.controller;

import edu.kit.tm.cm.proto.*;
import edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.exception.InvalidArgumentsException;
import edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.exception.ResourceNotFoundException;
import edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.service.ProblemManagementService;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.Problem;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.ProblemState;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.StateOperation;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.filter.options.FilterOption;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.filter.options.FilterOptions;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Controller;

import java.sql.Timestamp;
import java.util.Collection;

@Controller
public class ProblemManagementController extends ProblemManagementGrpc.ProblemManagementImplBase {

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
  private static final String SUCCESSFUL_MESSAGE = "Operation was successful!";

  private static final boolean SUCCESSFUL = true;
  private static final boolean UNSUCCESSFUL = false;

  private final ProblemManagementService problemManagementService;

  public ProblemManagementController(ProblemManagementService problemManagementService) {
    this.problemManagementService = problemManagementService;
  }

  @Override
  public void listProblems(
      ListProblemsRequest request, StreamObserver<ListProblemsResponse> responseObserver) {

    try {

      ProblemFilterOptions problemFilterOptions = request.getProblemFilterOptions();
      FilterOptions filterOptions = this.readProblemFilterOptions(problemFilterOptions);
      Collection<Problem> problems = this.problemManagementService.listProblems(filterOptions);
      GrpcProblems grpcProblems = this.writeProblems(problems);
      ResponseMessage responseMessage = this.writeResponseMessage(SUCCESSFUL_MESSAGE, SUCCESSFUL);

      ListProblemsResponse response =
          ListProblemsResponse.newBuilder()
              .setProblems(grpcProblems)
              .setResponseMessage(responseMessage)
              .build();

      responseObserver.onNext(response);
      responseObserver.onCompleted();

    } catch (InvalidArgumentsException invalidArgumentsException) {

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

    String identificationNumber = request.getIdentificationNumber();

    try {

      Problem problem = this.problemManagementService.getProblem(identificationNumber);
      GrpcProblem grpcProblem = this.writeProblem(problem);
      ResponseMessage responseMessage = this.writeResponseMessage(SUCCESSFUL_MESSAGE, SUCCESSFUL);
      Collection<StateOperation> stateOperations =
          problem.getProblemState().getPossibleOperations();
      GrpcStateOperations grpcStateOperations = this.writeStateOperations(stateOperations);

      GetProblemResponse response =
          GetProblemResponse.newBuilder()
              .setProblem(grpcProblem)
              .setResponseMessage(responseMessage)
              .setPossibleStateOperations(grpcStateOperations)
              .build();

      responseObserver.onNext(response);
      responseObserver.onCompleted();

    } catch (InvalidArgumentsException invalidArgumentsException) {

      String message = String.format(invalidArgumentsException.getMessage(), GET_PROBLEM);
      ResponseMessage responseMessage = this.writeResponseMessage(message, UNSUCCESSFUL);

      GetProblemResponse response =
          GetProblemResponse.newBuilder().setResponseMessage(responseMessage).build();
      responseObserver.onNext(response);
      responseObserver.onCompleted();

    } catch (ResourceNotFoundException resourceNotFoundException) {

      String message =
          String.format(resourceNotFoundException.getMessage(), request.getIdentificationNumber());
      ResponseMessage responseMessage = this.writeResponseMessage(message, UNSUCCESSFUL);

      GetProblemResponse response =
          GetProblemResponse.newBuilder().setResponseMessage(responseMessage).build();
      responseObserver.onNext(response);
      responseObserver.onCompleted();
    }
  }

  @Override
  public void createProblem(
      CreateProblemRequest request, StreamObserver<CreateProblemResponse> responseObserver) {

    try {

      GrpcProblem grpcRequestProblem = request.getProblem();
      Problem requestProblem = this.readProblem(grpcRequestProblem);
      Problem responseProblem = this.problemManagementService.createProblem(requestProblem);
      GrpcProblem grpcResponseProblem = writeProblem(responseProblem);
      ResponseMessage responseMessage = this.writeResponseMessage(SUCCESSFUL_MESSAGE, SUCCESSFUL);

      CreateProblemResponse response =
          CreateProblemResponse.newBuilder()
              .setProblem(grpcResponseProblem)
              .setResponseMessage(responseMessage)
              .build();

      responseObserver.onNext(response);
      responseObserver.onCompleted();

    } catch (InvalidArgumentsException invalidArgumentsException) {

      String message = String.format(invalidArgumentsException.getMessage(), CREATE_PROBLEM);
      ResponseMessage responseMessage = this.writeResponseMessage(message, UNSUCCESSFUL);

      CreateProblemResponse response =
          CreateProblemResponse.newBuilder().setResponseMessage(responseMessage).build();

      responseObserver.onNext(response);
      responseObserver.onCompleted();
    }
  }

  @Override
  public void updateProblem(
      UpdateProblemRequest request, StreamObserver<UpdateProblemResponse> responseObserver) {

    String identificationNumber = request.getIdentificationNumber();

    try {

      GrpcProblem grpcRequestProblem = request.getProblem();
      Problem requestProblem = this.readProblem(grpcRequestProblem);
      requestProblem.setIdentificationNumber(identificationNumber);
      Problem responseProblem = this.problemManagementService.updateProblem(requestProblem);
      GrpcProblem grpcResponseProblem = this.writeProblem(responseProblem);
      ResponseMessage responseMessage = this.writeResponseMessage(SUCCESSFUL_MESSAGE, SUCCESSFUL);

      UpdateProblemResponse response =
          UpdateProblemResponse.newBuilder()
              .setProblem(grpcResponseProblem)
              .setResponseMessage(responseMessage)
              .build();

      responseObserver.onNext(response);
      responseObserver.onCompleted();

    } catch (InvalidArgumentsException invalidArgumentsException) {

      String message = String.format(invalidArgumentsException.getMessage(), UPDATE_PROBLEM);
      ResponseMessage responseMessage = this.writeResponseMessage(message, UNSUCCESSFUL);

      UpdateProblemResponse response =
          UpdateProblemResponse.newBuilder().setResponseMessage(responseMessage).build();

      responseObserver.onNext(response);
      responseObserver.onCompleted();

    } catch (ResourceNotFoundException resourceNotFoundException) {

      String message = String.format(resourceNotFoundException.getMessage(), identificationNumber);
      ResponseMessage responseMessage = this.writeResponseMessage(message, UNSUCCESSFUL);

      UpdateProblemResponse response =
          UpdateProblemResponse.newBuilder().setResponseMessage(responseMessage).build();

      responseObserver.onNext(response);
      responseObserver.onCompleted();
    }
  }

  @Override
  public void removeProblem(
      RemoveProblemRequest request, StreamObserver<RemoveProblemResponse> responseObserver) {

    String identificationNumber = request.getIdentificationNumber();

    try {

      this.problemManagementService.removeProblem(identificationNumber);
      ResponseMessage responseMessage = this.writeResponseMessage(SUCCESSFUL_MESSAGE, SUCCESSFUL);

      RemoveProblemResponse response =
          RemoveProblemResponse.newBuilder().setResponseMessage(responseMessage).build();

      responseObserver.onNext(response);
      responseObserver.onCompleted();

    } catch (InvalidArgumentsException invalidArgumentsException) {

      String message = String.format(invalidArgumentsException.getMessage(), REMOVE_PROBLEM);
      ResponseMessage responseMessage = this.writeResponseMessage(message, UNSUCCESSFUL);

      RemoveProblemResponse response =
          RemoveProblemResponse.newBuilder().setResponseMessage(responseMessage).build();

      responseObserver.onNext(response);
      responseObserver.onCompleted();

    } catch (ResourceNotFoundException resourceNotFoundException) {

      String message = String.format(resourceNotFoundException.getMessage(), identificationNumber);
      ResponseMessage responseMessage = this.writeResponseMessage(message, UNSUCCESSFUL);

      RemoveProblemResponse response =
          RemoveProblemResponse.newBuilder().setResponseMessage(responseMessage).build();

      responseObserver.onNext(response);
      responseObserver.onCompleted();
    }
  }

  @Override
  public void acceptProblem(
      ChangeStateRequest request, StreamObserver<ChangeStateResponse> responseObserver) {

    String identificationNumber = request.getIdentificationNumber();

    try {

      this.problemManagementService.acceptProblem(identificationNumber);
      ResponseMessage responseMessage = this.writeResponseMessage(SUCCESSFUL_MESSAGE, SUCCESSFUL);

      ChangeStateResponse response =
          ChangeStateResponse.newBuilder().setResponseMessage(responseMessage).build();

      responseObserver.onNext(response);
      responseObserver.onCompleted();

    } catch (InvalidArgumentsException invalidArgumentsException) {

      String message = String.format(invalidArgumentsException.getMessage(), ACCEPT_PROBLEM);
      ResponseMessage responseMessage = this.writeResponseMessage(message, UNSUCCESSFUL);

      ChangeStateResponse response =
          ChangeStateResponse.newBuilder().setResponseMessage(responseMessage).build();

      responseObserver.onNext(response);
      responseObserver.onCompleted();

    } catch (ResourceNotFoundException resourceNotFoundException) {

      String message = String.format(resourceNotFoundException.getMessage(), identificationNumber);
      ResponseMessage responseMessage = this.writeResponseMessage(message, UNSUCCESSFUL);

      ChangeStateResponse response =
          ChangeStateResponse.newBuilder().setResponseMessage(responseMessage).build();

      responseObserver.onNext(response);
      responseObserver.onCompleted();
    }
  }

  @Override
  public void holdProblem(
      ChangeStateRequest request, StreamObserver<ChangeStateResponse> responseObserver) {

    String identificationNumber = request.getIdentificationNumber();

    try {

      this.problemManagementService.holdProblem(identificationNumber);
      ResponseMessage responseMessage = this.writeResponseMessage(SUCCESSFUL_MESSAGE, SUCCESSFUL);

      ChangeStateResponse response =
          ChangeStateResponse.newBuilder().setResponseMessage(responseMessage).build();

      responseObserver.onNext(response);
      responseObserver.onCompleted();

    } catch (InvalidArgumentsException invalidArgumentsException) {

      String message = String.format(invalidArgumentsException.getMessage(), HOLD_PROBLEM);
      ResponseMessage responseMessage = this.writeResponseMessage(message, UNSUCCESSFUL);

      ChangeStateResponse response =
          ChangeStateResponse.newBuilder().setResponseMessage(responseMessage).build();

      responseObserver.onNext(response);
      responseObserver.onCompleted();

    } catch (ResourceNotFoundException resourceNotFoundException) {

      String message = String.format(resourceNotFoundException.getMessage(), identificationNumber);
      ResponseMessage responseMessage = this.writeResponseMessage(message, UNSUCCESSFUL);

      ChangeStateResponse response =
          ChangeStateResponse.newBuilder().setResponseMessage(responseMessage).build();

      responseObserver.onNext(response);
      responseObserver.onCompleted();
    }
  }

  @Override
  public void closeProblem(
      ChangeStateRequest request, StreamObserver<ChangeStateResponse> responseObserver) {

    String identificationNumber = request.getIdentificationNumber();

    try {

      this.problemManagementService.closeProblem(identificationNumber);
      ResponseMessage responseMessage = this.writeResponseMessage(SUCCESSFUL_MESSAGE, SUCCESSFUL);

      ChangeStateResponse response =
          ChangeStateResponse.newBuilder().setResponseMessage(responseMessage).build();

      responseObserver.onNext(response);
      responseObserver.onCompleted();

    } catch (InvalidArgumentsException invalidArgumentsException) {

      String message = String.format(invalidArgumentsException.getMessage(), CLOSE_PROBLEM);
      ResponseMessage responseMessage = this.writeResponseMessage(message, UNSUCCESSFUL);

      ChangeStateResponse response =
          ChangeStateResponse.newBuilder().setResponseMessage(responseMessage).build();

      responseObserver.onNext(response);
      responseObserver.onCompleted();

    } catch (ResourceNotFoundException resourceNotFoundException) {

      String message = String.format(resourceNotFoundException.getMessage(), identificationNumber);
      ResponseMessage responseMessage = this.writeResponseMessage(message, UNSUCCESSFUL);

      ChangeStateResponse response =
          ChangeStateResponse.newBuilder().setResponseMessage(responseMessage).build();

      responseObserver.onNext(response);
      responseObserver.onCompleted();
    }
  }

  @Override
  public void approachProblem(
      ChangeStateRequest request, StreamObserver<ChangeStateResponse> responseObserver) {

    String identificationNumber = request.getIdentificationNumber();

    try {

      this.problemManagementService.approachProblem(identificationNumber);
      ResponseMessage responseMessage = this.writeResponseMessage(SUCCESSFUL_MESSAGE, SUCCESSFUL);

      ChangeStateResponse response =
          ChangeStateResponse.newBuilder().setResponseMessage(responseMessage).build();

      responseObserver.onNext(response);
      responseObserver.onCompleted();

    } catch (InvalidArgumentsException invalidArgumentsException) {

      String message = String.format(invalidArgumentsException.getMessage(), APPROACH_PROBLEM);
      ResponseMessage responseMessage = this.writeResponseMessage(message, UNSUCCESSFUL);

      ChangeStateResponse response =
          ChangeStateResponse.newBuilder().setResponseMessage(responseMessage).build();

      responseObserver.onNext(response);
      responseObserver.onCompleted();

    } catch (ResourceNotFoundException resourceNotFoundException) {

      String message = String.format(resourceNotFoundException.getMessage(), identificationNumber);
      ResponseMessage responseMessage = this.writeResponseMessage(message, UNSUCCESSFUL);

      ChangeStateResponse response =
          ChangeStateResponse.newBuilder().setResponseMessage(responseMessage).build();

      responseObserver.onNext(response);
      responseObserver.onCompleted();
    }
  }

  @Override
  public void declineProblem(
      ChangeStateRequest request, StreamObserver<ChangeStateResponse> responseObserver) {

    String identificationNumber = request.getIdentificationNumber();

    try {

      this.problemManagementService.declineProblem(identificationNumber);
      ResponseMessage responseMessage = this.writeResponseMessage(SUCCESSFUL_MESSAGE, SUCCESSFUL);

      ChangeStateResponse response =
          ChangeStateResponse.newBuilder().setResponseMessage(responseMessage).build();

      responseObserver.onNext(response);
      responseObserver.onCompleted();

    } catch (InvalidArgumentsException invalidArgumentsException) {

      String message = String.format(invalidArgumentsException.getMessage(), DECLINE_PROBLEM);
      ResponseMessage responseMessage = this.writeResponseMessage(message, UNSUCCESSFUL);

      ChangeStateResponse response =
          ChangeStateResponse.newBuilder().setResponseMessage(responseMessage).build();

      responseObserver.onNext(response);
      responseObserver.onCompleted();

    } catch (ResourceNotFoundException resourceNotFoundException) {

      String message = String.format(resourceNotFoundException.getMessage(), identificationNumber);
      ResponseMessage responseMessage = this.writeResponseMessage(message, UNSUCCESSFUL);

      ChangeStateResponse response =
          ChangeStateResponse.newBuilder().setResponseMessage(responseMessage).build();

      responseObserver.onNext(response);
      responseObserver.onCompleted();
    }
  }

  private Problem readProblem(GrpcProblem grpcProblem) {

    String problemReporter = grpcProblem.getProblemReporter();
    String problemDescription = grpcProblem.getProblemDescription();
    String problemTitle = grpcProblem.getProblemTitle();
    Timestamp creationTime = new Timestamp(grpcProblem.getCreationTime().getNanos());
    String referenceIdentificationNumber = grpcProblem.getReferenceIdentificationNumber();
    GrpcProblemState grpcProblemState = grpcProblem.getProblemState();
    ProblemState problemState = readProblemState(grpcProblemState);

    return Problem.builder()
        .problemReporter(problemReporter)
        .problemState(problemState)
        .problemDescription(problemDescription)
        .problemTitle(problemTitle)
        .creationTime(creationTime)
        .referenceIdentificationNumber(referenceIdentificationNumber)
        .build();
  }

  private FilterOptions readProblemFilterOptions(ProblemFilterOptions problemFilterOptions) {
    FilterOption<ProblemState> problemStateFilterOption =
        readProblemStateFilterOption(problemFilterOptions.getProblemStateFilterMapping());

    return FilterOptions.builder().problemStateFilterOption(problemStateFilterOption).build();
  }

  private FilterOption<ProblemState> readProblemStateFilterOption(
      ProblemStateFilterMapping problemStateFilterMapping) {
    boolean selected = problemStateFilterMapping.getSelected();
    Collection<ProblemState> filterValues =
        problemStateFilterMapping.getProblemStateList().stream()
            .map(this::readProblemState)
            .toList();

    return FilterOption.<ProblemState>builder()
        .selected(selected)
        .filterValues(filterValues)
        .build();
  }

  private ProblemState readProblemState(GrpcProblemState grpcProblemState) {
    return ProblemState.forNumber(grpcProblemState.ordinal() + 1);
  }

  private GrpcProblem writeProblem(Problem problem) {

    return GrpcProblem.newBuilder()
        .setCreationTime(
            com.google.protobuf.Timestamp.newBuilder()
                .setNanos(problem.getCreationTime().getNanos())
                .build())
        .setProblemDescription(problem.getProblemDescription())
        .setProblemReporter(problem.getProblemReporter())
        .setProblemTitle(problem.getProblemTitle())
        .setReferenceIdentificationNumber(problem.getReferenceIdentificationNumber())
        .setProblemState(writeProblemState(problem.getProblemState()))
        .setIdentificationNumber(problem.getIdentificationNumber())
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
