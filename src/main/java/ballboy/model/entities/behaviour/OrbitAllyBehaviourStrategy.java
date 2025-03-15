package ballboy.model.entities.behaviour;

import ballboy.model.Level;
import ballboy.model.entities.DynamicEntity;
import ballboy.model.entities.utilities.Vector2D;

/**
 * Behaviour of cat, every frame it moves clockwise around the ballboy in the level
 */
public class OrbitAllyBehaviourStrategy implements BehaviourStrategy{
    private final Level level;
    private final double speed;
    private int movementMode=0;
    private int currentModeTicks=0;
    private final int ticksPerLength=50;
    private double xOffset;
    private double yOffset;
    private double radius;
    private double objectWidth;

    public OrbitAllyBehaviourStrategy(Level level, double radius, double objectWidth){
        this.level = level;
        this.speed = radius/((double)(ticksPerLength/2));
        this.radius = radius;
        this.objectWidth = objectWidth;
        xOffset = -radius + level.getHeroWidth()/2 - objectWidth/2;
        yOffset = -radius + level.getHeroHeight()/2 - objectWidth/2;
    }

    public double getxOffset() {
        return xOffset;
    }

    public double getyOffset() {
        return yOffset;
    }

    public int getCurrentModeTicks() {
        return currentModeTicks;
    }

    public int getMovementMode() {
        return movementMode;
    }

    /**
     * Used by save to set the cat to a previous state
     * @param mode mode is 0,1,2,3 since its not edited outside of this function its not important though
     * @param modeTicks the length/50 its moved during this mode
     */
    public void setModeAndTicks(int mode, int modeTicks){
        this.currentModeTicks = modeTicks;
        this.movementMode = mode;
    }

    public void setOffset(double xOffset, double yOffset){
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    @Override
    public void behave(DynamicEntity entity, double frameDurationMilli) {
        //Mode 0 - move right, 1, down, 2 left, 3 up
        if (movementMode == 0){
            xOffset = (-radius + level.getHeroWidth()/2 - objectWidth/2) + speed*currentModeTicks;
            yOffset = -radius + level.getHeroHeight()/2 - objectWidth/2;
        } else if (movementMode == 1){
            xOffset = (-radius + level.getHeroWidth()/2 - objectWidth/2) + speed*ticksPerLength;
            yOffset = -radius + level.getHeroHeight()/2 - objectWidth/2 + speed*currentModeTicks;
        } else if (movementMode == 2){
            xOffset = (radius + level.getHeroWidth()/2 - objectWidth/2) - speed*currentModeTicks;
            yOffset = -radius + level.getHeroHeight()/2 - objectWidth/2 + speed*ticksPerLength;
        } else{
            xOffset = (radius + level.getHeroWidth()/2 - objectWidth/2) - speed*ticksPerLength;
            yOffset = radius + level.getHeroHeight()/2 - objectWidth/2 - speed*currentModeTicks;
        }
        //update movementMode
        currentModeTicks++;
        if (currentModeTicks >= ticksPerLength){
            movementMode++;
            if (movementMode >= 4) { movementMode = 0; }
            currentModeTicks = 0;
        }
        //Update position
        entity.setPosition(new Vector2D(level.getHeroX() + xOffset, level.getHeroY() + yOffset));
    }
}
