package ballboy.model;

import ballboy.model.entities.behaviour.BehaviourStrategy;
import ballboy.model.entities.collision.CollisionStrategy;
import ballboy.model.entities.utilities.AxisAlignedBoundingBox;
import ballboy.model.entities.utilities.Vector2D;
import ballboy.view.Score;
import javafx.scene.image.Image;

public interface Entity {
    /**
     * Returns the current Image used by this Entity. This may change over time, such as for simple animations.
     *
     * @return An Image representing the current state of this Entity
     */
    Image getImage();

    /**
     * @return Vector2 The current position of the entity, being the top left anchor.
     */
    Vector2D getPosition();

    /**
     * Returns the current height of this Entity
     *
     * @return The height in coordinate space (e.g. number of pixels)
     */
    double getHeight();

    /**
     * Returns the current width of this Entity
     *
     * @return The width in coordinate space (e.g. number of pixels)
     */
    double getWidth();

    /**
     * Returns the current 'z' position to draw this entity. Order within each layer is undefined.
     *
     * @return The layer to draw the entity on.
     */
    Layer getLayer();

    /**
     * @return AxisAlignedBoundingBox The enclosing volume of this entity.
     */
    AxisAlignedBoundingBox getVolume();

    /**
     * The set of available layers
     */
    enum Layer {
        BACKGROUND, FOREGROUND, EFFECT
    }

    /**
     * Get the collision strategy used by this entity
     * @return the collision strategy
     */
    CollisionStrategy getCollisionStrategy();

    /**
     * Kill this entity
     */
    void die();

    /**
     * Copy this entity
     * @return the copied entity
     */
    Entity copy();

    /**
     * Get the behaviour strategy used by this entity
     * @return the behaviour strategy
     */
    BehaviourStrategy getBehaviourStrategy();

}
