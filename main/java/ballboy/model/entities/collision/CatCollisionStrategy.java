package ballboy.model.entities.collision;

import ballboy.model.Entity;
import ballboy.model.Level;
import ballboy.model.entities.DynamicEntity;

/**
 * Collision strategy used by square cat
 */
public class CatCollisionStrategy implements CollisionStrategy{

    private final Level level;

    public CatCollisionStrategy(Level level) {
        this.level = level;
    }

    @Override
    public void collideWith(Entity currentEntity, Entity hitEntity) {
        if (hitEntity.getCollisionStrategy() instanceof EnemyCollisionStrategy){
            level.killEntity(hitEntity);
        }
    }
}
