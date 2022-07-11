package edu.kit.tm.cm.smartcampus.problemmanagement.logic.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

class ProblemStatePossibleOperationsTests {

  @ParameterizedTest
  @ArgumentsSource(ProblemStateArgumentsProvider.class)
  void filterRoomCollectionAndValuesResultTest(
    Collection<StateOperation> expected, ProblemState problemState) {
    Assertions.assertTrue(problemState.getPossibleOperations().containsAll(expected));
  }

  private static class ProblemStateArgumentsProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
      return Stream.of(
        Arguments.of(List.of(StateOperation.DECLINE, StateOperation.ACCEPT), ProblemState.OPEN),
        Arguments.of(List.of(StateOperation.DECLINE, StateOperation.APPROACH), ProblemState.ACCEPTED),
        Arguments.of(List.of(StateOperation.CLOSE, StateOperation.ACCEPT), ProblemState.DECLINED),
        Arguments.of(List.of(StateOperation.CLOSE, StateOperation.HOLD), ProblemState.IN_PROGRESS),
        Arguments.of(List.of(), ProblemState.CLOSED)
      );
    }
  }
}
