package ballboy.model;

import ballboy.model.entities.memento.LevelState;
import ballboy.view.Score;
import javafx.scene.paint.Color;

import java.util.List;

/**
 * The base interface for a Ballboy level.
 */
public interface Level {

    /**
     * Return a List of the currently existing Entities.
     *
     * @return The list of current entities for this level
     */
    List<Entity> getEntities();

    /**
     * The height of the level
     *
     * @return The height (should be in the same format as Entity sizes)
     */
    double getLevelHeight();

    /**
     * The width of the level
     *
     * @return The width (should be in the same format as Entity sizes)
     */
    double getLevelWidth();

    /**
     * @return double The height of the hero.
     */
    double getHeroHeight();

    /**
     * @return double The width of the hero.
     */
    double getHeroWidth();

    /**
     * @return double The vertical position of the floor.
     */
    double getFloorHeight();

    /**
     * @return Color The current configured color of the floor.
     */
    Color getFloorColor();

    /**
     * @return double The current level gravity.
     */
    double getGravity();

    /**
     * Instruct the level to progress forward in time by one increment.
     */
    void update();

    /**
     * The current x position of the hero. This is useful for views so they can follow the hero.
     *
     * @return The hero x position (should be in the same format as Entity sizes)
     */
    double getHeroX();

    /**
     * The current y position of the hero. This is useful for views so they can follow the hero.
     *
     * @return The hero y position (should be in the same format as Entity sizes)
     */
    double getHeroY();

    /**
     * Increase the height the bouncing hero can reach. This could be the vertical acceleration of the hero, unless
     * the current level has special behaviour.
     *
     * @return true if successful
     */
    boolean boostHeight();

    /**
     * Reduce the height the bouncing hero can reach. This could be the vertical acceleration of the hero, unless the
     * current level has special behaviour.
     *
     * @return true if successful
     */
    boolean dropHeight();

    /**
     * Move the hero left or accelerate the hero left, depending on the current level's desired behaviour
     *
     * @return true if successful
     */
    boolean moveLeft();

    /**
     * Move the hero right or accelerate the hero right, depending on the current level's desired behaviour
     *
     * @return true if successful
     */
    boolean moveRight();

    /**
     * @param entity The entity to be checked.
     * @return boolean True if the provided entity is the current hero.
     */
    boolean isHero(Entity entity);

    /**
     * @param entity The entity to be checked.
     * @return boolean True if the provided entity is the finish of this level.
     */
    boolean isFinish(Entity entity);

    /*
     * Currently, this will just reset the hero to its starting position.
     */
    void resetHero();

    /**
     * Finishes the level.
     */
    void finish();

    /**
     * Return true if the level is over, otherwise false
     * @return
     */
    boolean isFinished();

    /**
     * Kill the provided entity
     * @param e1 the entity to kill
     */
    void killEntity(Entity e1);

    /**
     * Get the score list of this level
     * @return the score list of this level
     */
    List<Score> getScores();

    /**
     * Save the level state
     * @return the level's state
     */
    LevelState saveState();

    /**
     * Load a previous level state
     * @param gameState the state to load
     */
    void loadState(LevelState gameState);
}
