package edu.kit.tm.cm.smartcampus.problemmanagement.logic.model;

import edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.exception.InvalidStateChangeRequestException;

import java.util.Collection;
import java.util.List;

/**
 * The enum problem state.
 */
public enum ProblemState {
  /**
   * Accepted state.
   */
ACCEPTED {
    @Override
    public Collection<StateOperation> getPossibleOperations() {
      return List.of(StateOperation.APPROACH, StateOperation.DECLINE);
    }

    @Override
    public ProblemState accept() {
      throw new InvalidStateChangeRequestException(StateOperation.ACCEPT.name());
    }

    @Override
    public ProblemState approach() {
      return IN_PROGRESS;
    }

    @Override
    public ProblemState close() {
      throw new InvalidStateChangeRequestException(StateOperation.CLOSE.name());
    }

    @Override
    public ProblemState hold() {
      throw new InvalidStateChangeRequestException(StateOperation.HOLD.name());
    }

    @Override
    public ProblemState decline() {
      return DECLINED;
    }
  },
  /**
   * Declined state.
   */
DECLINED {
    @Override
    public Collection<StateOperation> getPossibleOperations() {
      return List.of(StateOperation.CLOSE, StateOperation.ACCEPT);
    }

    @Override
    public ProblemState accept() {
      return ACCEPTED;
    }

    @Override
    public ProblemState approach() {
      throw new InvalidStateChangeRequestException(StateOperation.APPROACH.name());
    }

    @Override
    public ProblemState close() {
      return CLOSED;
    }

    @Override
    public ProblemState hold() {
      throw new InvalidStateChangeRequestException(StateOperation.HOLD.name());
    }

    @Override
    public ProblemState decline() {
      throw new InvalidStateChangeRequestException(StateOperation.DECLINE.name());
    }
  },
  /**
   * In progress state.
   */
IN_PROGRESS {
    @Override
    public Collection<StateOperation> getPossibleOperations() {
      return List.of(StateOperation.CLOSE, StateOperation.HOLD);
    }

    @Override
    public ProblemState accept() {
      throw new InvalidStateChangeRequestException(StateOperation.ACCEPT.name());
    }

    @Override
    public ProblemState approach() {
      throw new InvalidStateChangeRequestException(StateOperation.APPROACH.name());
    }

    @Override
    public ProblemState close() {
      return CLOSED;
    }

    @Override
    public ProblemState hold() {
      return ACCEPTED;
    }

    @Override
    public ProblemState decline() {
      throw new InvalidStateChangeRequestException(StateOperation.DECLINE.name());
    }
  },
  /**
   * Open state.
   */
OPEN {
    @Override
    public Collection<StateOperation> getPossibleOperations() {
      return List.of(StateOperation.DECLINE, StateOperation.ACCEPT);
    }

    @Override
    public ProblemState accept() {
      return ACCEPTED;
    }

    @Override
    public ProblemState approach() {
      throw new InvalidStateChangeRequestException(StateOperation.APPROACH.name());
    }

    @Override
    public ProblemState close() {
      throw new InvalidStateChangeRequestException(StateOperation.CLOSE.name());
    }

    @Override
    public ProblemState hold() {
      throw new InvalidStateChangeRequestException(StateOperation.HOLD.name());
    }

    @Override
    public ProblemState decline() {
      return DECLINED;
    }
  },
  /**
   * Closed state.
   */
CLOSED {
    @Override
    public Collection<StateOperation> getPossibleOperations() {
      return List.of();
    }

    @Override
    public ProblemState accept() {
      throw new InvalidStateChangeRequestException(StateOperation.ACCEPT.name());
    }

    @Override
    public ProblemState approach() {
      throw new InvalidStateChangeRequestException(StateOperation.APPROACH.name());
    }

    @Override
    public ProblemState close() {
      throw new InvalidStateChangeRequestException(StateOperation.CLOSE.name());
    }

    @Override
    public ProblemState hold() {
      throw new InvalidStateChangeRequestException(StateOperation.HOLD.name());
    }

    @Override
    public ProblemState decline() {
      throw new InvalidStateChangeRequestException(StateOperation.DECLINE.name());
    }
  };

  /**
   * Parses problem state for a given ordinal number.
   *
   * @param value the value to parse a problem state from
   * @return the problem state parsed
   */
public static ProblemState forNumber(int value) {
    return switch (value) {
      case 1 -> DECLINED;
      case 2 -> OPEN;
      case 3 -> CLOSED;
      case 4 -> IN_PROGRESS;
      case 5 -> ACCEPTED;
      default -> null;
    };
  }

  /**
   * Gets possible operations.
   *
   * @return the possible operations
   */
public abstract Collection<StateOperation> getPossibleOperations();

  /**
   * Accept problem state.
   *
   * @return the problem state
   */
public abstract ProblemState accept();

  /**
   * Approach problem state.
   *
   * @return the problem state
   */
public abstract ProblemState approach();

  /**
   * Close problem state.
   *
   * @return the problem state
   */
public abstract ProblemState close();

  /**
   * Hold problem state.
   *
   * @return the problem state
   */
public abstract ProblemState hold();

  /**
   * Decline problem state.
   *
   * @return the problem state
   */
public abstract ProblemState decline();
}
