package ballboy.model.entities;

import ballboy.model.Entity;
import ballboy.model.entities.behaviour.BehaviourStrategy;
import ballboy.model.entities.collision.CollisionStrategy;
import ballboy.model.entities.collision.EnemyCollisionStrategy;
import ballboy.model.entities.utilities.*;
import ballboy.model.observers.EnemyObserver;
import ballboy.model.observers.EnemyDeathObserverImpl;
import ballboy.view.Score;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;

public class DynamicEntityImpl extends DynamicEntity {
    private final CollisionStrategy collisionStrategy;
    private final BehaviourStrategy behaviourStrategy;
    private final AxisAlignedBoundingBox volume;
    private final Layer layer;
    private final Image image;
    private final KinematicState kinematicState;
    private ArrayList<EnemyObserver> observers = new ArrayList<>();
    private Score relatedScore;

    public DynamicEntityImpl(
            KinematicState kinematicState,
            AxisAlignedBoundingBox volume,
            Layer layer,
            Image image,
            CollisionStrategy collisionStrategy,
            BehaviourStrategy behaviourStrategy,
            Score score
    ) {
        this.kinematicState = kinematicState;
        this.volume = volume;
        this.layer = layer;
        this.image = image;
        this.collisionStrategy = collisionStrategy;
        this.behaviourStrategy = behaviourStrategy;
        this.relatedScore = score;

        //If its an enemy, add an observer and colour
        if (collisionStrategy instanceof EnemyCollisionStrategy) {
            new EnemyDeathObserverImpl(this);
        }
    }

    @Override
    public Entity copy() {
        Vector2D startingPosition = new Vector2D(this.getPosition().getX(), this.getPosition().getY());

        KinematicState kinematicState = new KinematicStateImpl.KinematicStateBuilder()
                .setPosition(startingPosition)
                .setHorizontalVelocity(this.getVelocity().getX())
                .build();


        AxisAlignedBoundingBox volume = new AxisAlignedBoundingBoxImpl(
                startingPosition,
                image.getHeight(),
                image.getWidth()
        );


        return new DynamicEntityImpl(kinematicState, volume, layer, this.getImage(), collisionStrategy, behaviourStrategy, relatedScore);
    }

    @Override
    public void notifyScore(){
        relatedScore.incrementScore();
    }

    @Override
    public void attach(EnemyObserver bo){
        observers.add(bo);
    }

    @Override
    public void notifyAllObservers(){
        for (EnemyObserver bo : observers){
            bo.update();
        }
    }

    @Override
    public Image getImage() {
        return this.image;
    }

    @Override
    public Vector2D getPosition() {
        return kinematicState.getPosition();
    }

    @Override
    public void setPosition(Vector2D pos) {
        this.kinematicState.setPosition(pos);
    }

    @Override
    public Vector2D getPositionBeforeLastUpdate() {
        return this.kinematicState.getPreviousPosition();
    }

    @Override
    public Vector2D getVelocity() {
        return this.kinematicState.getVelocity();
    }

    @Override
    public void setVelocity(Vector2D vel) {
        this.kinematicState.setVelocity(vel);
    }

    @Override
    public double getHorizontalAcceleration() {
        return this.kinematicState.getHorizontalAcceleration();
    }

    @Override
    public void setHorizontalAcceleration(double horizontalAcceleration) {
        this.kinematicState.setHorizontalAcceleration(horizontalAcceleration);
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
    public Layer getLayer() {
        return this.layer;
    }

    @Override
    public boolean collidesWith(Entity entity) {
        return volume.collidesWith(entity.getVolume());
    }

    @Override
    public AxisAlignedBoundingBox getVolume() {
        return this.volume;
    }

    @Override
    public CollisionStrategy getCollisionStrategy() {
        return this.collisionStrategy;
    }

    @Override
    public void collideWith(Entity entity) {
        collisionStrategy.collideWith(this, entity);
    }

    @Override
    public void update(
            double milliSeconds,
            double levelGravity) {
        kinematicState.update(milliSeconds, levelGravity);
        behaviourStrategy.behave(this, milliSeconds);
        this.volume.setTopLeft(this.kinematicState.getPosition());
    }

    @Override
    public void die(){
        notifyAllObservers();
    }

    @Override
    public BehaviourStrategy getBehaviourStrategy(){
        return this.behaviourStrategy;
    }
}
