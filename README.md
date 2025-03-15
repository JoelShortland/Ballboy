A Java program that I made as part of a USYD SOFT2201 Project. It is a 2D platformer that makes use of structured java code, using a number of OOP design principles, including: Factory, Observer & Memento.

Original readme from assignment below:

# How to run code
- Code runs with gradle build > gradle run

# Feature List
- Level Transition, you can specify how many levels to run in the json file and it ticks through. Implemented by added a level list to GameEngineImpl that ticks through after each level completion.
- Squarecat Entity that kills enemies constantly moves around ballboy, can be specified in the json file (image, sidelength and distance from ballboy)
- Score Position and colour implemented in json, though its intended for all 3 to always be visible at once for simplicity
- Save/Load press s to save and l to load. Follows functionality listed in specs.
- Doesnt save camera position as its not required

# Design Patterns
## Observer
Involved classes:
- Subject: DynamicEntityImpl
- Observer: EnemyDeathObserverImpl
- AbstractSubject: DynamicEntity
- AbstractObserver: EnemyObserver

## Memento
Involved classes:
- Caretaker (stores GameState)
- Memento: GameState
- LevelState is a part of gamestate, used to follow abstraction and increase readability
- ConstraintSolver: GameEngine

