package ballboy.model;

import ballboy.model.entities.memento.Caretaker;
import ballboy.model.entities.memento.GameState;

/**
 * The base interface for interacting with the Ballboy model
 */
public interface GameEngine {
    /**
     * Return the currently loaded level
     *
     * @return The current level
     */
    Level getCurrentLevel();

    /**
     * Start the level
     */
    void startLevel();

    /**
     * Increases the bounce height of the current hero.
     *
     * @return boolean True if the bounce height of the hero was successfully boosted.
     */
    boolean boostHeight();

    /**
     * Reduces the bounce height of the current hero.
     *
     * @return boolean True if the bounce height of the hero was successfully dropped.
     */
    boolean dropHeight();

    /**
     * Applies a left movement to the current hero.
     *
     * @return True if the hero was successfully moved left.
     */
    boolean moveLeft();

    /**
     * Applies a right movement to the current hero.
     *
     * @return True if the hero was successfully moved right.
     */
    boolean moveRight();

    /**
     * Instruct the model to progress forward in time by one increment.
     */
    void tick();

    /**
     * get the gameover state
     * @return true if game is over, otherwise false
     */
    boolean isGameOver();

    /**
     * Get the score totals from previous levels
     * @return
     */
    int[] getPreviousScores();

    /**
     * Load a previous gamestate
     * @param gameState the gamestate to load
     */
    void load(GameState gameState);

    /**
     * Save the gamestate
     * @return the gamestate saved
     */
    GameState save();

    /**
     * Get the caretaker used to store the gameState
     * @return the caretaker
     */
    Caretaker getGameStateCaretaker();
}
