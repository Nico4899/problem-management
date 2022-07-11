package edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations;

import edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.Problem;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.ProblemState;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.filter.Filter;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.filter.filters.PSFilter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

class FilterTests {

  public static final String OPEN = "open";
  public static final String CLOSED = "closed";
  public static final String DECLINED = "declined";
  public static final String IN_PROGRESS = "inProgress";
  public static final String ACCEPTED = "accepted";

  private static final Collection<ProblemState> ALL_PROBLEM_STATES = List.of(ProblemState.values());
  private static final Collection<ProblemState> SOME_PROBLEM_STATES =
      List.of(ProblemState.ACCEPTED, ProblemState.DECLINED, ProblemState.IN_PROGRESS);
  private static final Collection<ProblemState> NO_PROBLEM_STATES = List.of();

  private static final Map<String, Problem> testProblemsMap = new HashMap<>();

  // test object collections
  private static Collection<Problem> testProblems;

  @BeforeAll
  static void setUp() {
    buildTestCollections();
  }

  private static void buildTestCollections() {
    testProblemsMap.put(
        OPEN, new Problem(ProblemState.OPEN, null, null, null, null, null, null, null));
    testProblemsMap.put(
        CLOSED, new Problem(ProblemState.CLOSED, null, null, null, null, null, null, null));
    testProblemsMap.put(
        DECLINED, new Problem(ProblemState.DECLINED, null, null, null, null, null, null, null));
    testProblemsMap.put(
        IN_PROGRESS,
        new Problem(ProblemState.IN_PROGRESS, null, null, null, null, null, null, null));
    testProblemsMap.put(
        ACCEPTED, new Problem(ProblemState.ACCEPTED, null, null, null, null, null, null, null));

    testProblems = testProblemsMap.values();
  }

  @ParameterizedTest
  @ArgumentsSource(ProblemFilterArgumentsProvider.class)
  void filterRoomCollectionAndValuesResultTest(
      Collection<Problem> expected, Filter<Problem> filter, Collection<Problem> collection) {
    Assertions.assertTrue(expected.containsAll(filter.filter(collection)));
  }

  private static class ProblemFilterArgumentsProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
      return Stream.of(
          Arguments.of(testProblems, new PSFilter(ALL_PROBLEM_STATES), testProblems),
          Arguments.of(
              List.of(
                  testProblemsMap.get(ACCEPTED),
                  testProblemsMap.get(DECLINED),
                  testProblemsMap.get(IN_PROGRESS)),
              new PSFilter(SOME_PROBLEM_STATES),
              testProblems),
          Arguments.of(List.of(), new PSFilter(NO_PROBLEM_STATES), testProblems));
    }
  }
}
