package edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations;

import edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.Problem;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.filter.Filter;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.filter.filters.ProblemStateFilter;
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

  private static final Collection<Problem.State> ALL_PROBLEM_STATES =
      List.of(Problem.State.values());
  private static final Collection<Problem.State> SOME_PROBLEM_STATES =
      List.of(Problem.State.ACCEPTED, Problem.State.DECLINED, Problem.State.IN_PROGRESS);
  private static final Collection<Problem.State> NO_PROBLEM_STATES = List.of();

  private static final Map<String, Problem> testProblemsMap = new HashMap<>();

  // test object collections
  private static Collection<Problem> testProblems;

  @BeforeAll
  static void setUp() {
    buildTestCollections();
  }

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

    testProblems = testProblemsMap.values();
  }

  @ParameterizedTest
  @ArgumentsSource(ProblemFilterArgumentsProvider.class)
  void whenFilter_thenFilter(
      Collection<Problem> expected, Filter<Problem> filter, Collection<Problem> collection) {
    Assertions.assertTrue(expected.containsAll(filter.filter(collection)));
  }

  private static class ProblemFilterArgumentsProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
      return Stream.of(
          Arguments.of(testProblems, new ProblemStateFilter(ALL_PROBLEM_STATES), testProblems),
          Arguments.of(
              List.of(
                  testProblemsMap.get(ACCEPTED),
                  testProblemsMap.get(DECLINED),
                  testProblemsMap.get(IN_PROGRESS)),
              new ProblemStateFilter(SOME_PROBLEM_STATES),
              testProblems),
          Arguments.of(List.of(), new ProblemStateFilter(NO_PROBLEM_STATES), testProblems));
    }
  }
}
