# Live Football World Cup Score Board

A little library facilitating registration and summary of results of
currently in progress football matches according to [specification](specification.pdf)

## Set up

### Requirements

* Java 17

### Build

./mvnw clean install

## Assumptions made

* The library is not thread safe: Task was very specific about what it is and what is not a requirement, and highlighted the expectation for the simplicity and the focus on requirements only. As multi-threading was not mentioned as a requirement - it was neither TDDed nor implemented in any shape or form at all.
* API is generic: Knowing the nature of the system it is going to be used by, the specialised API could have been developed, which could be even less-error prone to use.
* Input validation is very basic: Implementing more input validation would not reveal any more of the skill of OO, SOLID, DRY or TDD I master, which task suggested is the crux of completing it. Apart from exact team-name matching in already in-progress Games and null checks (both of which were implemented), there could have been validation implemented for a single country not to appearing multiple times on the board; valid country name validation and similar type of validations, but were not.
