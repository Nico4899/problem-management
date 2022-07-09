package edu.kit.tm.cm.smartcampus.problemmanagement.logic.model;

import edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.exception.InvalidStateChangeRequestException;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public enum ProblemState {
  UNKNOWN_PROBLEM_STATE {
    @Override
    public Collection<StateOperation> getPossibleOperations() {
      return Collections.emptyList();
    }

    @Override
    public ProblemState accept() {
      return null;
    }

    @Override
    public ProblemState approach() {
      return null;
    }

    @Override
    public ProblemState close() {
      return null;
    }

    @Override
    public ProblemState hold() {
      return null;
    }

    @Override
    public ProblemState decline() {
      return null;
    }
  },
  ACCEPTED{
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
  DECLINED {
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

  ProblemState() {
  }

  public abstract Collection<StateOperation> getPossibleOperations();

  public abstract ProblemState accept();

  public abstract ProblemState approach();

  public abstract ProblemState close();

  public abstract ProblemState hold();

  public abstract ProblemState decline();

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
}
