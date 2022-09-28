# ProblemManagement Class Diagram

This diagram is split into three parts: problem, controller and filter. The problem part ist the main datastructure describing a problem and its problemstate as statemachine. It is closer described in [Problem](https://git.scc.kit.edu/cm-tm/cm-team/3.projectwork/pse/domain/1-problem/-/blob/dev/pages/bounded_context_entity_relation_view.md). The Controller and Filter are units to control changes and filter for requests.

![ProblemManagement Class Diagram](../figures/class_diagram/problem_management_class_diagram2.png)

A description of the identification number called "in" in this diagram can be found in the [Ubiquitous Language](https://git.scc.kit.edu/-/ide/project/cm-tm/cm-team/3.projectwork/pse/docsc/tree/english-translation/-/pages/ubiquitous_language.md/).

A state diagram describing transitions and problemstates can be found in [problem entity relation view](https://git.scc.kit.edu/cm-tm/cm-team/3.projectwork/pse/domain/1-problem/-/blob/dev/pages/bounded_context_entity_relation_view.md).
