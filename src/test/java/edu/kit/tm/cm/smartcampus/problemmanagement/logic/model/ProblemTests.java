package edu.kit.tm.cm.smartcampus.problemmanagement.logic.model;

import edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.connector.BuildingConnector;
import edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.connector.ClientBuildingConnector;
import edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.connector.ClientProblemConnector;
import edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.connector.ProblemConnector;
import edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.exception.InternalServerErrorException;
import edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.exception.InvalidStateChangeRequestException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.Mockito;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ProblemTests {

  public static final String OPEN = "OPEN";
  public static final String CLOSED = "CLOSED";
  public static final String DECLINED = "DECLINED";
  public static final String IN_PROGRESS = "IN_PROGRESS";
  public static final String ACCEPTED = "ACCEPTED";
  private static final BuildingConnector buildingConnector =
      Mockito.mock(ClientBuildingConnector.class);
  private static final ProblemConnector problemConnector =
      Mockito.mock(ClientProblemConnector.class);
  private static Map<String, Problem> testProblemsMap;
  private static Problem testProblem;
  private static Notification testNotification;

  @BeforeAll
  public static void BeforeAll() {
    initializeResources();
    when(buildingConnector.createNotification(any(Notification.class)))
        .thenReturn(testNotification);
    when(problemConnector.updateProblem(any(Problem.class))).thenReturn(testProblem);
  }

  private static void initializeResources() {
    testProblem = new Problem();
    testProblem.setCreationTime(new Timestamp(1));
    testProblem.setReferenceIdentificationNumber("b-1");
    testProblem.setState(Problem.State.OPEN);
    testProblem.setDescription("");
    testProblem.setReporter("");
    testProblem.setTitle("");
    testProblem.setIdentificationNumber("p-1");
    testProblem.setNotificationIdentificationNumber("n-1");

    testNotification = new Notification();
    testNotification = testProblem.extractNotification();
    testNotification.setIdentificationNumber("n-1");

    Problem problem1 = new Problem();
    problem1.setState(Problem.State.OPEN);
    Problem problem2 = new Problem();
    problem2.setState(Problem.State.CLOSED);
    Problem problem3 = new Problem();
    problem3.setState(Problem.State.DECLINED);
    Problem problem4 = new Problem();
    problem4.setState(Problem.State.IN_PROGRESS);
    Problem problem5 = new Problem();
    problem5.setState(Problem.State.ACCEPTED);

    testProblemsMap = new HashMap<>();
    testProblemsMap.put(OPEN, problem1);
    testProblemsMap.put(CLOSED, problem2);
    testProblemsMap.put(DECLINED, problem3);
    testProblemsMap.put(IN_PROGRESS, problem4);
    testProblemsMap.put(ACCEPTED, problem5);
  }

  private static void reset() {
    for (Map.Entry<String, Problem> entry : testProblemsMap.entrySet()) {
      entry.getValue().setState(Problem.State.valueOf(entry.getKey()));
    }
  }

  @ParameterizedTest
  @ArgumentsSource(ProblemStateChangeValidArgumentProvider.class)
  void whenValidOperation_thenExecuteStateChange(
      Problem problem, Problem.State.Operation operation, Problem.State expected) {
    reset();
    operation.apply(problem, buildingConnector, problemConnector);
    Assertions.assertEquals(expected, problem.getState());
  }

  @ParameterizedTest
  @ArgumentsSource(ProblemStateChangeInvalidArgumentProvider.class)
  void whenInvalidOperation_thenThrowsInvalidStateChangeRequestException(
      Problem problem, Problem.State.Operation operation) {
    Assertions.assertThrows(
        InvalidStateChangeRequestException.class,
        () -> operation.apply(problem, buildingConnector, problemConnector));
  }

  @ParameterizedTest
  @ArgumentsSource(ProblemStateArgumentsProvider.class)
  void whenFilterRoomsByFilterValues_thenFilterRooms(
      Collection<Problem.State.Operation> expected, Problem.State problemState) {
    Assertions.assertTrue(problemState.possibleOperations().containsAll(expected));
  }

  private static class ProblemStateChangeInvalidArgumentProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
      return Stream.of(
          Arguments.of(testProblemsMap.get(ACCEPTED), Problem.State.Operation.ACCEPT),
          Arguments.of(testProblemsMap.get(ACCEPTED), Problem.State.Operation.CLOSE),
          Arguments.of(testProblemsMap.get(ACCEPTED), Problem.State.Operation.HOLD),
          Arguments.of(testProblemsMap.get(DECLINED), Problem.State.Operation.APPROACH),
          Arguments.of(testProblemsMap.get(DECLINED), Problem.State.Operation.HOLD),
          Arguments.of(testProblemsMap.get(DECLINED), Problem.State.Operation.DECLINE),
          Arguments.of(testProblemsMap.get(IN_PROGRESS), Problem.State.Operation.APPROACH),
          Arguments.of(testProblemsMap.get(IN_PROGRESS), Problem.State.Operation.ACCEPT),
          Arguments.of(testProblemsMap.get(IN_PROGRESS), Problem.State.Operation.DECLINE),
          Arguments.of(testProblemsMap.get(OPEN), Problem.State.Operation.CLOSE),
          Arguments.of(testProblemsMap.get(OPEN), Problem.State.Operation.APPROACH),
          Arguments.of(testProblemsMap.get(OPEN), Problem.State.Operation.HOLD),
          Arguments.of(testProblemsMap.get(CLOSED), Problem.State.Operation.ACCEPT),
          Arguments.of(testProblemsMap.get(CLOSED), Problem.State.Operation.DECLINE),
          Arguments.of(testProblemsMap.get(CLOSED), Problem.State.Operation.CLOSE),
          Arguments.of(testProblemsMap.get(CLOSED), Problem.State.Operation.APPROACH),
          Arguments.of(testProblemsMap.get(CLOSED), Problem.State.Operation.HOLD));
    }
  }

  private static class ProblemStateChangeValidArgumentProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
      return Stream.of(
          Arguments.of(
              testProblemsMap.get(ACCEPTED),
              Problem.State.Operation.DECLINE,
              Problem.State.DECLINED),
          Arguments.of(
              testProblemsMap.get(ACCEPTED),
              Problem.State.Operation.APPROACH,
              Problem.State.IN_PROGRESS),
          Arguments.of(
              testProblemsMap.get(DECLINED),
              Problem.State.Operation.ACCEPT,
              Problem.State.ACCEPTED),
          Arguments.of(
              testProblemsMap.get(DECLINED), Problem.State.Operation.CLOSE, Problem.State.CLOSED),
          Arguments.of(
              testProblemsMap.get(IN_PROGRESS),
              Problem.State.Operation.HOLD,
              Problem.State.ACCEPTED),
          Arguments.of(
              testProblemsMap.get(IN_PROGRESS),
              Problem.State.Operation.CLOSE,
              Problem.State.CLOSED),
          Arguments.of(
              testProblemsMap.get(OPEN), Problem.State.Operation.ACCEPT, Problem.State.ACCEPTED),
          Arguments.of(
              testProblemsMap.get(OPEN), Problem.State.Operation.DECLINE, Problem.State.DECLINED));
    }
  }

  private static class ProblemStateArgumentsProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
      return Stream.of(
          Arguments.of(
              List.of(Problem.State.Operation.DECLINE, Problem.State.Operation.ACCEPT),
              Problem.State.OPEN),
          Arguments.of(
              List.of(Problem.State.Operation.DECLINE, Problem.State.Operation.APPROACH),
              Problem.State.ACCEPTED),
          Arguments.of(
              List.of(Problem.State.Operation.CLOSE, Problem.State.Operation.ACCEPT),
              Problem.State.DECLINED),
          Arguments.of(
              List.of(Problem.State.Operation.CLOSE, Problem.State.Operation.HOLD),
              Problem.State.IN_PROGRESS),
          Arguments.of(List.of(), Problem.State.CLOSED));
    }
  }
}
