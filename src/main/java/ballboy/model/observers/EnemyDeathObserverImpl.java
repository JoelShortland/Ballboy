package ballboy.model.observers;

import ballboy.model.Entity;
import ballboy.model.Level;
import ballboy.model.entities.DynamicEntityImpl;
import ballboy.model.entities.collision.EnemyCollisionStrategy;

/**
 * Whenever an enemy dies, notify the enemies score
 */
public class EnemyDeathObserverImpl implements EnemyObserver{
    private DynamicEntityImpl enemy;

    public EnemyDeathObserverImpl(Entity enemy) {
        if (enemy.getCollisionStrategy() instanceof EnemyCollisionStrategy){
            ((DynamicEntityImpl) enemy).attach(this);
            this.enemy = (DynamicEntityImpl) enemy;
        } else{
            System.out.println("Tried to attach an observer to a non enemy");
            System.exit(-1);
        }

    }

    @Override
    public void update(){
        enemy.notifyScore();
    }
}
