# Cincuentazo (50zo Game)

Cincuentazo is a small card game project implemented as a desktop application using JavaFX. It is a poker-like survival game in which human and AI players use cards to keep the table sum at or below 50. The project is designed and implemented following MVC architecture, event-driven programming, multithreading for concurrency, robust exception handling, and unit testing with JUnit 5.

---

## Table of Contents

- Project overview
- Game rules
  - Objective
  - Card values
  - Preparation (setup)
  - Turn flow
  - Deck and table management
  - Elimination and end of game
- Technical goals and learning objectives
- Architecture & design
- Requirements
- Installation
- Running the game
- How to play / Controls
- Testing
- Contributing
- Code style, documentation & exceptions
- Authors 


---

## Project overview

Cincuentazo (English: "Fifty-something") is a turn-based card game for 2–4 players (1 human + 1–3 AI opponents). Each player maintains a hand of four cards and plays cards on their turn to modify a running sum on the table. The running sum must never exceed 50; players who cannot play a card without exceeding 50 are eliminated. The last remaining player wins.

This mini-project is intended as an exercise to apply:
- JavaFX UI development (layouts and event handling)
- MVC architecture (Model–View–Controller)
- Event-driven programming with mouse and keyboard events
- Threading for concurrency (e.g., AI timers, turn manager)
- Custom exceptions and robust error handling
- JUnit 5 automated unit tests
- Professional code management with Git and GitHub
- Javadoc documentation in English

---

## Game rules

### Objective
Be the last player remaining in the game.

### Card values and behavior
- Cards numbered 2 through 8 and the 10: add their face value to the table sum (e.g., 2 adds 2, 10 adds 10).
- Card 9: neutral — neither adds nor subtracts (no effect).
- Face cards J, Q, K: subtract 10 from the table sum.
- Ace (A): can count as either +1 or +10 — player (or AI) chooses the value that is most advantageous without exceeding 50.

The main rule: the table sum must not exceed 50. If a play would cause the sum to go above 50 (>50), that play is invalid and cannot be chosen.

### Preparation (setup)
1. From a shuffled deck, deal 4 random cards to each player:
   - AI players receive their 4 cards face-down.
   - The human player receives their 4 cards face-up (visible).
2. Place one random card face-up on the table; this card initializes the table sum.
   - Note: the table sum may start as 0 (if the initial card is a 9), 1 (if the initial card is an Ace counted as 1), or -10 (if the initial card is J/Q/K).
3. The remaining cards remain as the draw deck (face-down).

The human player starts the first turn.

### Turn flow
- Players take turns in clockwise order.
- On a player's turn:
  1. The player selects a card from their hand that, when applied to the current table sum (added or subtracted using the card's rules), does not cause the table sum to exceed 50.
  2. The chosen card is placed face-up on the table on top of the previous table card and the table sum is updated accordingly (apply +, −, or neutral).
  3. The player then draws the top card from the draw deck so that they always end their turn with 4 cards in hand.
- If a player cannot play any card from their hand without making the table sum exceed 50, that player is eliminated immediately.

### Deck and table management
- If the draw deck becomes empty, shuffle the table cards except for the last-played card (the current top of the table) and place them face-down to replenish the draw deck. The table sum remains unchanged.
- When a player is eliminated, their remaining cards are sent to the bottom of the draw deck and become available for future draws.

### End of game
The last player who has not been eliminated wins the game.

---


## Requirements

- Java Development Kit (JDK) 11 or higher (JDK 17 recommended).
- JavaFX 11+ (or matching JavaFX version for your JDK).
- Build tool: Gradle or Maven (project distribution may specify one).
- JUnit 5 for unit testing.
- Git for version control.

---

## Installation

1. Clone the repository:
   git clone https://github.com/alejo2807/50zo-game.git
2. Enter the project directory:
   cd 50zo-game
3. Install dependencies and build (examples — use the build tool used in the project):
   - Gradle (recommended):
     - ./gradlew build
     - ./gradlew run
   - Maven:
     - mvn clean install
     - mvn javafx:run

Note: Ensure JavaFX modules are available to your chosen build configuration. If using a modular setup, configure VM options to include JavaFX modules.

---

## Running the game (development)

- From the IDE:
  - Import the project as a Gradle/Maven project.
  - Configure JavaFX SDK in project settings.
  - Run the main application class.
- From command line (example with Gradle):
  - ./gradlew run
- From command line (example with Maven + javafx-maven-plugin):
  - mvn javafx:run

Adjust commands to match the actual artifact and main class in the repository.

---

## How to play 

Objective: survive by playing cards that keep the table sum ≤ 50. The human player may start with their 4 face-up cards visible.

Suggested controls (actual controls may vary per implementation):
- Mouse:
  - Click a card in your hand to select and play it.
  - Click deck to view top card (if a feature exists).

Game tips:
- Use Ace intelligently (1 or 10) depending on current table sum.
- J/Q/K reduce the table sum by 10 — useful if the sum is close to 50.
- 9 is neutral — useful as a pause move to avoid busting.
- Maintain awareness of the number of cards left in the draw deck to anticipate re-shuffles.

---

## Testing

- Unit tests implemented with JUnit 5 should cover:
  - Card value calculations and Ace logic (1 vs 10).
  - Deck shuffle and draw behavior.
  - Table sum updates and validation (no >50).
  - Elimination conditions and deck replenishment rules.
- Run tests:
  - Gradle: ./gradlew test
  - Maven: mvn test

Create at least three test classes that together assert core game mechanics and edge cases.

---

## Contributing

Thank you for considering contributing!

Basic workflow:
1. Fork the repository.
2. Create a feature branch: git checkout -b feature/my-feature
3. Commit changes with clear messages: git commit -m "Add AI decision logic"
4. Push and open a Pull Request.

Please:
- Follow existing code style and layout conventions.
- Add or update Javadoc for public classes/methods.
- Add unit tests for new logic.
- Document breaking changes in the PR description.

---

## Code style, documentation & exceptions

- Document public API and important methods with Javadoc in English.
- Use custom exceptions to represent game-specific errors:
  - Example checked exceptions (e.g., InvalidPlayException).
  - Example unchecked exceptions for programmer errors where appropriate.
- Use code formatting tools (e.g., Checkstyle, Spotless, or editorconfig) as configured by the project.

---

## Authors 

- Authors: Juan - David - Brandon.

For questions or bug reports, open an issue in the GitHub repository.

---

## Notes & implementation hints

- When implementing AI players, give them a short thinking delay (use a separate thread) to improve UX. Ensure UI updates are done on the JavaFX Application Thread.
- When reshuffling table cards back into the deck, preserve the current top-of-table card and recalculate or preserve the table sum accordingly (do not modify the current table sum when reshuffling).
- Ensure thread-safe access to shared game state (deck, table, players) using synchronization or higher-level concurrency utilities.
- Design unit tests to simulate edge conditions: empty deck, multiple consecutive eliminations, Ace choices near the 50 threshold.

---
