# cinema-tickets
Implementation of the DWP cinema ticket purchasing coding exercise.

## Prerequisites
- Java 21
- Maven 4

## Build and test

```
mvn clean verify
```

## Test coverage
After running build they can be found in 
```
target/site/jacoco/index.html
```

## Design note
Ticket purchase validation moved into a dedicated class.  This separates business rule validation from service orchestration.  Each rule produces a specific error message to aid debud.

Ticket prices are defined as constants associated with the ticket type avoiding magic numbers, and allowing simpler future updates.

The payment and reservation services are only called when validation passes.
