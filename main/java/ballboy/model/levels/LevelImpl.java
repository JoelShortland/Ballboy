package ballboy.model.levels;

import ballboy.ConfigurationParseException;
import ballboy.model.Entity;
import ballboy.model.Level;
import ballboy.model.entities.*;
import ballboy.model.entities.behaviour.OrbitAllyBehaviourStrategy;
import ballboy.model.entities.collision.BallboyCollisionStrategy;
import ballboy.model.entities.memento.LevelState;
import ballboy.model.entities.utilities.Vector2D;
import ballboy.model.factories.EntityFactory;
import ballboy.view.Score;
import javafx.scene.paint.Color;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

/**
 * Level logic, with abstract factor methods.
 */
public class LevelImpl implements Level {

    private List<Entity> entities = new ArrayList<>();
    private final List<Score> scores = new ArrayList<>();
    private PhysicsEngine engine=null;        //Needs a default value because second constructor is dumb
    private EntityFactory entityFactory=null; //Needs a default value because second constructor is dumb
    private ControllableDynamicEntity<DynamicEntity> hero;
    private Entity finish;
    private double levelHeight;
    private double levelWidth;
    private double levelGravity;
    private double floorHeight;
    private Color floorColor;
    private boolean levelBeaten=false;

    private double frameDurationMilli=0.0;    //Needs a default value because second constructor is dumb

    /**
     * A callback queue for post-update jobs. This is specifically useful for scheduling jobs mid-update
     * that require the level to be in a valid state.
     */
    private final Queue<Runnable> afterUpdateJobQueue = new ArrayDeque<>();

    public LevelImpl(
            JSONObject levelConfiguration,
            PhysicsEngine engine,
            EntityFactory entityFactory,
            double frameDurationMilli) {
        this.engine = engine;
        this.entityFactory = entityFactory;
        this.frameDurationMilli = frameDurationMilli;
        initLevel(levelConfiguration);
    }

    @Override
    public void loadState(LevelState gameState){
        if (gameState == null) { return; }
        this.entities.clear();
        this.entities.addAll(gameState.getEntities());

        for (int i = 0; i < this.entities.size(); i++) {
            if (this.getEntities().get(i) instanceof DynamicEntityImpl){
                ((DynamicEntityImpl) this.getEntities().get(i)).setPosition(new Vector2D(gameState.getEntityStates().get(i)[0], gameState.getEntityStates().get(i)[1]));
                ((DynamicEntityImpl) this.getEntities().get(i)).setVelocity(new Vector2D(gameState.getEntityStates().get(i)[2], gameState.getEntityStates().get(i)[3]));
            } else if (this.getEntities().get(i) instanceof ControllableDynamicEntity){
                ((ControllableDynamicEntity) this.getEntities().get(i)).setPosition(new Vector2D(gameState.getEntityStates().get(i)[0], gameState.getEntityStates().get(i)[1]));
                ((ControllableDynamicEntity) this.getEntities().get(i)).setVelocity(new Vector2D(gameState.getEntityStates().get(i)[2], gameState.getEntityStates().get(i)[3]));
            }
            if (this.getEntities().get(i).getBehaviourStrategy() instanceof OrbitAllyBehaviourStrategy){
                ((OrbitAllyBehaviourStrategy) this.getEntities().get(i).getBehaviourStrategy()).setModeAndTicks(gameState.getCatState()[1] ,gameState.getCatState()[0]);
                ((OrbitAllyBehaviourStrategy) this.getEntities().get(i).getBehaviourStrategy()).setOffset(gameState.getCatPos()[0] ,gameState.getCatPos()[1]);
            }
        }
        //fix scores
        for (int i = 0; i < gameState.getScoreStates().size(); i++){
            scores.get(i).setValue(gameState.getScoreStates().get(i));
        }
        this.levelBeaten = gameState.isFinished();
    }

    @Override
    public LevelState saveState(){
        ArrayList<Double[]> entityStates = new ArrayList<>();
        ArrayList<Entity> entitiesInPlay = new ArrayList<>();
        int[] catState = new int[2];
        double[] catPos = new double[2];
        for (Entity e : this.getEntities()){
            double xSpeed = 0;
            double ySpeed = 0;
            if (e instanceof DynamicEntityImpl){
                xSpeed = ((DynamicEntityImpl) e).getVelocity().getX();
                ySpeed = ((DynamicEntityImpl) e).getVelocity().getY();
            } else if (e instanceof ControllableDynamicEntity){
                xSpeed = ((ControllableDynamicEntity) e).getVelocity().getX();
                ySpeed = ((ControllableDynamicEntity) e).getVelocity().getY();
            }

            if (e.getBehaviourStrategy() instanceof OrbitAllyBehaviourStrategy){
               catState[0] = ((OrbitAllyBehaviourStrategy) e.getBehaviourStrategy()).getMovementMode();
               catState[1] = ((OrbitAllyBehaviourStrategy) e.getBehaviourStrategy()).getCurrentModeTicks();
               catPos[0] = ((OrbitAllyBehaviourStrategy) e.getBehaviourStrategy()).getxOffset();
               catPos[1] = ((OrbitAllyBehaviourStrategy) e.getBehaviourStrategy()).getyOffset();
            }

            entityStates.add(new Double[]{e.getPosition().getX(), e.getPosition().getY(),
            xSpeed, ySpeed});
            entitiesInPlay.add(e);
        }

        ArrayList<Integer> scoreStates = new ArrayList<>();
        for (Score s : scores){
            scoreStates.add(s.getValue());
        }

        return new LevelState(entityStates, entitiesInPlay, scoreStates, levelBeaten, catState, catPos);
    }

    /**
     * Instantiates a level from the level configuration.
     *
     * @param levelConfiguration The configuration for the level.
     */
    private void initLevel(JSONObject levelConfiguration) {
        this.levelWidth = ((Number) levelConfiguration.get("levelWidth")).doubleValue();
        this.levelHeight = ((Number) levelConfiguration.get("levelHeight")).doubleValue();
        this.levelGravity = ((Number) levelConfiguration.get("levelGravity")).doubleValue();

        JSONObject floorJson = (JSONObject) levelConfiguration.get("floor");
        this.floorHeight = ((Number) floorJson.get("height")).doubleValue();
        String floorColorWeb = (String) floorJson.get("color");
        this.floorColor = Color.web(floorColorWeb);

        //Get Scores
        JSONArray jsonScores = (JSONArray) levelConfiguration.get("scores");
        for (Object o : jsonScores) {
            JSONObject currentScore = (JSONObject) o;
            if ((boolean) (currentScore.get("display"))){
                Score.Colour c = Score.Colour.getValue(currentScore.get("type"));
                double xPos = ((double) currentScore.get("xPos"));
                double yPos = ((double) currentScore.get("yPos"));
                this.scores.add(new Score(c, xPos, yPos));
            }
        }
        if (this.scores.size()==0) {
            throw new ConfigurationParseException("At least one score must be displayed (have display=true)");
        }

        JSONArray generalEntities = (JSONArray) levelConfiguration.get("genericEntities");
        for (Object o : generalEntities) {
            this.entities.add(entityFactory.createEntity(this, (JSONObject) o));
        }

        JSONObject heroConfig = (JSONObject) levelConfiguration.get("hero");
        double maxVelX = ((Number) levelConfiguration.get("maxHeroVelocityX")).doubleValue();

        Object hero = entityFactory.createEntity(this, heroConfig);
        if (!(hero instanceof DynamicEntity)) {
            throw new ConfigurationParseException("hero must be a dynamic entity");
        }
        DynamicEntity dynamicHero = (DynamicEntity) hero;
        Vector2D heroStartingPosition = dynamicHero.getPosition();
        this.hero = new ControllableDynamicEntity<>(dynamicHero, heroStartingPosition, maxVelX, floorHeight,
                levelGravity);
        this.entities.add(this.hero);

        //Create cat
        JSONObject catConfig = (JSONObject) levelConfiguration.get("squarecat");
        Object cat = entityFactory.createEntity(this, catConfig);
        if (!(cat instanceof DynamicEntity)) {
            throw new ConfigurationParseException("cat must be a dynamic entity");
        }
        DynamicEntity dynamicCat = (DynamicEntity) cat; //convert to correct datatype
        this.entities.add(dynamicCat);

        JSONObject finishConfig = (JSONObject) levelConfiguration.get("finish");
        this.finish = entityFactory.createEntity(this, finishConfig);
        this.entities.add(finish);
    }

    @Override
    public List<Score> getScores(){
        return this.scores;
    }

    @Override
    public List<Entity> getEntities() {
        return Collections.unmodifiableList(entities);
    }

    private List<DynamicEntity> getDynamicEntities() {
        return entities.stream().filter(e -> e instanceof DynamicEntity).map(e -> (DynamicEntity) e).collect(
                Collectors.toList());
    }

    private List<StaticEntity> getStaticEntities() {
        return entities.stream().filter(e -> e instanceof StaticEntity).map(e -> (StaticEntity) e).collect(
                Collectors.toList());
    }

    @Override
    public double getLevelHeight() {
        return this.levelHeight;
    }

    @Override
    public double getLevelWidth() {
        return this.levelWidth;
    }

    @Override
    public double getHeroHeight() {
        return hero.getHeight();
    }

    @Override
    public double getHeroWidth() {
        return hero.getWidth();
    }

    @Override
    public double getFloorHeight() {
        return floorHeight;
    }

    @Override
    public Color getFloorColor() {
        return floorColor;
    }

    @Override
    public double getGravity() {
        return levelGravity;
    }

    @Override
    public void update() {
        List<DynamicEntity> dynamicEntities = getDynamicEntities();

        dynamicEntities.stream().forEach(e -> {
            e.update(frameDurationMilli, levelGravity);
        });

        for (int i = 0; i < dynamicEntities.size(); ++i) {
            DynamicEntity dynamicEntityA = dynamicEntities.get(i);

            for (int j = i + 1; j < dynamicEntities.size(); ++j) {
                DynamicEntity dynamicEntityB = dynamicEntities.get(j);

                if (dynamicEntityA.collidesWith(dynamicEntityB)) {
                    dynamicEntityA.collideWith(dynamicEntityB);
                    dynamicEntityB.collideWith(dynamicEntityA);
                    if (!isHero(dynamicEntityA) && !isHero(dynamicEntityB)) {
                        engine.resolveCollision(dynamicEntityA, dynamicEntityB);
                    }
                }
            }

            for (StaticEntity staticEntity : getStaticEntities()) {
                if (dynamicEntityA.collidesWith(staticEntity)) {
                    dynamicEntityA.collideWith(staticEntity);
                    engine.resolveCollision(dynamicEntityA, staticEntity, this);
                }
            }
        }

        dynamicEntities.stream().forEach(e -> engine.enforceWorldLimits(e, this));

        afterUpdateJobQueue.forEach(j -> j.run());
        afterUpdateJobQueue.clear();

    }

    @Override
    public double getHeroX() {
        return hero.getPosition().getX();
    }

    @Override
    public double getHeroY() {
        return hero.getPosition().getY();
    }

    @Override
    public boolean boostHeight() {
        return hero.boostHeight();
    }

    @Override
    public boolean dropHeight() {
        return hero.dropHeight();
    }

    @Override
    public boolean moveLeft() {
        return hero.moveLeft();
    }

    @Override
    public boolean moveRight() {
        return hero.moveRight();
    }

    @Override
    public boolean isHero(Entity entity) {
        return entity == hero;
    }

    @Override
    public boolean isFinish(Entity entity) {
        return this.finish == entity;
    }

    @Override
    public void resetHero() {
        afterUpdateJobQueue.add(() -> this.hero.reset());
    }

    @Override
    public void finish() {
        this.levelBeaten=true;
    }

    @Override
    public boolean isFinished(){
        return levelBeaten;
    }

    @Override
    public void killEntity(Entity e1){
        for (Entity e2 : entities){
            if (e1 == e2){
                e2.die();
                entities.remove(e2);
                return;
            }
        }
    }
}
