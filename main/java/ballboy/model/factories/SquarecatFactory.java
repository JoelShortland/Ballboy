package ballboy.model.factories;

import ballboy.ConfigurationParseException;
import ballboy.model.Entity;
import ballboy.model.Level;
import ballboy.model.entities.DynamicEntityImpl;
import ballboy.model.entities.behaviour.OrbitAllyBehaviourStrategy;
import ballboy.model.entities.behaviour.PassiveEntityBehaviourStrategy;
import ballboy.model.entities.collision.BallboyCollisionStrategy;
import ballboy.model.entities.collision.CatCollisionStrategy;
import ballboy.model.entities.utilities.*;
import javafx.scene.image.Image;
import org.json.simple.JSONObject;

/**
 * A factory that creates squarecats
 */
public class SquarecatFactory implements EntityFactory{
    @Override
    public Entity createEntity(Level level, JSONObject entityConfig) {
        try {
            //get ballboy center and subtract radius
            double radius = ((Number) entityConfig.get("radius")).doubleValue();
            double startX = level.getHeroX() - radius;
            double startY = level.getHeroY() - radius;


            String imageName = (String) entityConfig.getOrDefault("image", "blank.png");
            double sideLength = ((Number) entityConfig.get("sideLength")).doubleValue();

            Vector2D startingPosition = new Vector2D(startX, startY);

            KinematicState kinematicState = new KinematicStateImpl.KinematicStateBuilder()
                    .setPosition(startingPosition)
                    .build();

            AxisAlignedBoundingBox volume = new AxisAlignedBoundingBoxImpl(
                    startingPosition,
                    sideLength,
                    sideLength
            );

            return new DynamicEntityImpl(
                    kinematicState,
                    volume,
                    Entity.Layer.FOREGROUND,
                    new Image(imageName),
                    new CatCollisionStrategy(level),
                    new OrbitAllyBehaviourStrategy(level, radius, sideLength),
                    null
            );

        } catch (Exception e) {
            throw new ConfigurationParseException(
                    String.format("Invalid cat entity configuration | %s | %s", entityConfig, e));
        }
    }
}
