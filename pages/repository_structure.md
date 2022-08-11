# ProblemManagement Repository Structure

This project is structured after the standard template, with api, infrastructure and logic.

```
BuildingManagement
    └── src	
        ├── main			
        |   ├── java
        |   |   └── edu.kit.tm.cm.smartcampus	
        |   |       └── problemmanagement
        |   |           ├── api	
        |   |           |   ├── configuration
        |   |           |   |   └── converter
        |   |           |   ├── error
        |   |           |   └── controller
        |   |           ├── logic
        |   |           |   ├── model
        |   |           |   └── operations
        |   |           |       ├── filter
        |   |           |       ├── settings
        |   |           |       └── utils
        |   |           └── infrastructure
        |   |               ├── connector
        |   |               |   ├── building
        |   |               |   |   └── dto
        |   |               |   ├── problem
        |   |               |   |   └── dto
        |   |               |   └── error        
        |   |               └── service	
        |   ├── resources
        |   └── proto
        └── test
            ├── java
            |   └── edu.kit.tm.cm.smartcampus
            └── resources
```