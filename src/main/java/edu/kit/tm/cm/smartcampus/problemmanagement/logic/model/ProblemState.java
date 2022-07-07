package edu.kit.tm.cm.smartcampus.problemmanagement.logic.model;

import java.util.Collection;
import java.util.List;

public enum ProblemState {
  ACCEPTED{
    @Override
    public Collection<StateOperation> getPossibleOperations() {
      return List.of(StateOperation.APPROACH, StateOperation.DECLINE);
    }

    @Override
    public ProblemState accept() {
      return ACCEPTED;
    }

    @Override
    public ProblemState approach() {
      return IN_PROGRESS;
    }

    @Override
    public ProblemState close() {
      return ACCEPTED;
    }

    @Override
    public ProblemState hold() {
      return ACCEPTED;
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
      return DECLINED;
    }

    @Override
    public ProblemState close() {
      return CLOSED;
    }

    @Override
    public ProblemState hold() {
      return DECLINED;
    }

    @Override
    public ProblemState decline() {
      return DECLINED;
    }
  },
  IN_PROGRESS {
    @Override
    public Collection<StateOperation> getPossibleOperations() {
      return List.of(StateOperation.CLOSE, StateOperation.HOLD);
    }

    @Override
    public ProblemState accept() {
      return IN_PROGRESS;
    }

    @Override
    public ProblemState approach() {
      return IN_PROGRESS;
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
      return IN_PROGRESS;
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
      return OPEN;
    }

    @Override
    public ProblemState close() {
      return OPEN;
    }

    @Override
    public ProblemState hold() {
      return OPEN;
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
      return CLOSED;
    }

    @Override
    public ProblemState approach() {
      return CLOSED;
    }

    @Override
    public ProblemState close() {
      return CLOSED;
    }

    @Override
    public ProblemState hold() {
      return CLOSED;
    }

    @Override
    public ProblemState decline() {
      return CLOSED;
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
