package ballboy.model.entities;

import ballboy.model.Entity;
import ballboy.model.entities.behaviour.BehaviourStrategy;
import ballboy.model.entities.collision.CollisionStrategy;
import ballboy.model.entities.utilities.AxisAlignedBoundingBox;
import ballboy.model.entities.utilities.Vector2D;
import ballboy.view.Score;
import javafx.scene.image.Image;

/**
 * A static entity.
 */
public class StaticEntityImpl extends StaticEntity {
    private final AxisAlignedBoundingBox volume;
    private final Entity.Layer layer;
    private final Image image;
    private final Score.Colour colour = null;

    public StaticEntityImpl(
            AxisAlignedBoundingBox volume,
            Entity.Layer layer,
            Image image
    ) {
        this.volume = volume;
        this.layer = layer;
        this.image = image;
    }

    @Override
    public Entity copy(){
        return new StaticEntityImpl(volume, layer, image);
    }

    @Override
    public Image getImage() {
        return this.image;
    }

    @Override
    public Vector2D getPosition() {
        return new Vector2D(volume.getLeftX(), volume.getTopY());
    }

    @Override
    public double getHeight() {
        return volume.getHeight();
    }

    @Override
    public double getWidth() {
        return volume.getWidth();
    }

    @Override
    public Entity.Layer getLayer() {
        return this.layer;
    }

    @Override
    public AxisAlignedBoundingBox getVolume() {
        return this.volume;
    }

    @Override
    public CollisionStrategy getCollisionStrategy() {
        return null;
    }

    @Override
    public void die(){
        return;
    }

    @Override
    public BehaviourStrategy getBehaviourStrategy(){
        return null;
    }
}
