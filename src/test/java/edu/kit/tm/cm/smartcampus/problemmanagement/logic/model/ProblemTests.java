package edu.kit.tm.cm.smartcampus.problemmanagement.logic.model;

import edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.connector.BuildingConnector;
import edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.connector.ClientBuildingConnector;
import edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.connector.ClientProblemConnector;
import edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.connector.ProblemConnector;
import edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.exception.InternalServerErrorException;
import edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.exception.InvalidStateChangeRequestException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
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

import static org.mockito.Mockito.when;

class ProblemTests {

  public static final String OPEN = "open";
  public static final String CLOSED = "closed";
  public static final String DECLINED = "declined";
  public static final String IN_PROGRESS = "inProgress";
  public static final String ACCEPTED = "accepted";

  private static final Map<String, Problem> testProblemsMap = new HashMap<>();

  private static final BuildingConnector buildingConnector = Mockito.mock(ClientBuildingConnector.class);
  private static final ProblemConnector problemConnector = Mockito.mock(ClientProblemConnector.class);

  private static Problem testProblem;
  private static Notification testNotification;

  private static void buildTestCollections() {

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

    testProblemsMap.put(OPEN, problem1);
    testProblemsMap.put(CLOSED, problem2);
    testProblemsMap.put(DECLINED, problem3);
    testProblemsMap.put(IN_PROGRESS, problem4);
    testProblemsMap.put(ACCEPTED, problem5);

    testProblem = new Problem();
    testProblem.setCreationTime(new Timestamp(1));
    testProblem.setReferenceIdentificationNumber("b-1");
    testProblem.setState(Problem.State.OPEN);
    testProblem.setDescription("");
    testProblem.setReporter("");
    testProblem.setTitle("");
    testProblem.setIdentificationNumber("p-1");
    testProblem.setNotificationIdentificationNumber("n-1");

    testNotification = testProblem.extractNotification();
    testNotification.setIdentificationNumber("n-1");
  }

  @BeforeAll
  public static void setUp() {
    buildTestCollections();
    when(buildingConnector.createNotification(testProblem.extractNotification())).thenReturn(testNotification);
    when(problemConnector.updateProblem(testProblem)).thenReturn(testProblem);
  }

  // test state changes

  @Test
  void acceptedInvalidProblemStateChangeTest() {
    Problem problem = testProblemsMap.get(ACCEPTED);
    Assertions.assertThrows(
        InvalidStateChangeRequestException.class,
        () -> Problem.State.Operation.ACCEPT.apply(problem, buildingConnector, problemConnector));
    Assertions.assertThrows(
        InvalidStateChangeRequestException.class,
        () -> Problem.State.Operation.CLOSE.apply(problem, buildingConnector, problemConnector));
    Assertions.assertThrows(
        InvalidStateChangeRequestException.class,
        () -> Problem.State.Operation.HOLD.apply(problem, buildingConnector, problemConnector));
  }

  @Test
  void acceptedValidProblemChangeTest() {
    Problem problem = testProblemsMap.get(ACCEPTED);
    Assertions.assertEquals(Problem.State.ACCEPTED, problem.getState());
    Problem.State.Operation.APPROACH.apply(problem, buildingConnector, problemConnector);
    Assertions.assertEquals(Problem.State.IN_PROGRESS, problem.getState());
    problem.setState(Problem.State.ACCEPTED);
    Assertions.assertEquals(Problem.State.ACCEPTED, problem.getState());
    Problem.State.Operation.DECLINE.apply(problem, buildingConnector, problemConnector);
    Assertions.assertEquals(Problem.State.DECLINED, problem.getState());
  }

  @Test
  void declinedInvalidProblemStateChangeTest() {
    Problem problem = testProblemsMap.get(DECLINED);
    Assertions.assertThrows(
        InvalidStateChangeRequestException.class,
        () -> Problem.State.Operation.APPROACH.apply(problem, buildingConnector, problemConnector));
    Assertions.assertThrows(
        InvalidStateChangeRequestException.class,
        () -> Problem.State.Operation.DECLINE.apply(problem, buildingConnector, problemConnector));
    Assertions.assertThrows(
        InvalidStateChangeRequestException.class,
        () -> Problem.State.Operation.HOLD.apply(problem, buildingConnector, problemConnector));
  }

  @Test
  void declinedValidProblemChangeTest() {
    Problem problem = testProblemsMap.get(DECLINED);
    Assertions.assertEquals(Problem.State.DECLINED, problem.getState());
    Problem.State.Operation.ACCEPT.apply(problem, buildingConnector, problemConnector);
    Assertions.assertEquals(Problem.State.ACCEPTED, problem.getState());
    problem.setState(Problem.State.DECLINED);
    Assertions.assertEquals(Problem.State.DECLINED, problem.getState());
    Problem.State.Operation.CLOSE.apply(problem, buildingConnector, problemConnector);
    Assertions.assertEquals(Problem.State.CLOSED, problem.getState());
  }

  @Test
  void closedInvalidProblemStateChangeTest() {
    Problem problem = testProblemsMap.get(CLOSED);
    Assertions.assertThrows(
        InvalidStateChangeRequestException.class,
        () -> Problem.State.Operation.ACCEPT.apply(problem, buildingConnector, problemConnector));
    Assertions.assertThrows(
        InvalidStateChangeRequestException.class,
        () -> Problem.State.Operation.CLOSE.apply(problem, buildingConnector, problemConnector));
    Assertions.assertThrows(
        InvalidStateChangeRequestException.class,
        () -> Problem.State.Operation.HOLD.apply(problem, buildingConnector, problemConnector));
    Assertions.assertThrows(
        InvalidStateChangeRequestException.class,
        () -> Problem.State.Operation.APPROACH.apply(problem, buildingConnector, problemConnector));
    Assertions.assertThrows(
        InvalidStateChangeRequestException.class,
        () -> Problem.State.Operation.DECLINE.apply(problem, buildingConnector, problemConnector));
  }

  @Test
  void inProgressInvalidProblemStateChangeTest() {
    Problem problem = testProblemsMap.get(IN_PROGRESS);
    Assertions.assertThrows(
        InvalidStateChangeRequestException.class,
        () -> Problem.State.Operation.ACCEPT.apply(problem, buildingConnector, problemConnector));
    Assertions.assertThrows(
        InvalidStateChangeRequestException.class,
        () -> Problem.State.Operation.DECLINE.apply(problem, buildingConnector, problemConnector));
    Assertions.assertThrows(
        InvalidStateChangeRequestException.class,
        () -> Problem.State.Operation.APPROACH.apply(problem, buildingConnector, problemConnector));
  }

  @Test
  void inProgressValidProblemChangeTest() {
    Problem problem = testProblemsMap.get(IN_PROGRESS);
    Assertions.assertEquals(Problem.State.IN_PROGRESS, problem.getState());
    Problem.State.Operation.HOLD.apply(problem, buildingConnector, problemConnector);
    Assertions.assertEquals(Problem.State.ACCEPTED, problem.getState());
    problem.setState(Problem.State.IN_PROGRESS);
    Assertions.assertEquals(Problem.State.IN_PROGRESS, problem.getState());
    Problem.State.Operation.CLOSE.apply(problem, buildingConnector, problemConnector);
    Assertions.assertEquals(Problem.State.CLOSED, problem.getState());
  }

  @Test
  void openInvalidProblemStateChangeTest() {
    Problem problem = testProblemsMap.get(OPEN);
    Assertions.assertThrows(
        InvalidStateChangeRequestException.class,
        () -> Problem.State.Operation.CLOSE.apply(problem, buildingConnector, problemConnector));
    Assertions.assertThrows(
        InvalidStateChangeRequestException.class,
        () -> Problem.State.Operation.APPROACH.apply(problem, buildingConnector, problemConnector));
    Assertions.assertThrows(
        InvalidStateChangeRequestException.class,
        () -> Problem.State.Operation.HOLD.apply(problem, buildingConnector, problemConnector));
  }

  @Test
  void openValidProblemChangeTest() {
    Problem problem = testProblemsMap.get(OPEN);
    Assertions.assertEquals(Problem.State.OPEN, problem.getState());
    Problem.State.Operation.ACCEPT.apply(problem, buildingConnector, problemConnector);
    Assertions.assertEquals(Problem.State.ACCEPTED, problem.getState());
    problem.setState(Problem.State.OPEN);
    Assertions.assertEquals(Problem.State.OPEN, problem.getState());
    Problem.State.Operation.DECLINE.apply(problem, buildingConnector, problemConnector);
    Assertions.assertEquals(Problem.State.DECLINED, problem.getState());
  }

  // test possible operations

  @ParameterizedTest
  @ArgumentsSource(ProblemStateArgumentsProvider.class)
  void filterRoomCollectionAndValuesResultTest(
      Collection<Problem.State.Operation> expected, Problem.State problemState) {
    Assertions.assertTrue(problemState.possibleOperations().containsAll(expected));
  }

  // test state for ordinal

  @ParameterizedTest
  @ArgumentsSource(ProblemStateValidNumberArgumentsProvider.class)
  void forOrdinalProblemStateTestValid(Problem.State expected, int ordinal) {
    Assertions.assertEquals(expected, Problem.State.forOrdinal(ordinal));
  }

  @ParameterizedTest
  @ArgumentsSource(ProblemStateInvalidNumberArgumentsProvider.class)
  void forOrdinalProblemStateTestInvalid(Class<Exception> expected, int ordinal) {
    Assertions.assertThrows(expected, () -> Problem.State.forOrdinal(ordinal));
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

  private static class ProblemStateValidNumberArgumentsProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
      return Stream.of(
          Arguments.of(Problem.State.CLOSED, 4),
          Arguments.of(Problem.State.OPEN, 3),
          Arguments.of(Problem.State.IN_PROGRESS, 2),
          Arguments.of(Problem.State.DECLINED, 1),
          Arguments.of(Problem.State.ACCEPTED, 0));
    }
  }

  private static class ProblemStateInvalidNumberArgumentsProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
      return Stream.of(
          Arguments.of(InternalServerErrorException.class, Problem.State.values().length),
          Arguments.of(InternalServerErrorException.class, -1));
    }
  }
}
