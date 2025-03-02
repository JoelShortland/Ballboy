package ballboy.model.entities.memento;

import ballboy.model.Entity;
import ballboy.model.Level;

import java.util.ArrayList;

/**
 * A helper class that stores level information (Contained completely in GameState)
 */
public class LevelState {
    private ArrayList<Double[]> entityStates;
    private ArrayList<Entity> entities;
    private ArrayList<Integer> scoreStates;
    private boolean isFinished;
    private int[] catState;
    private double[] catPos;

    public LevelState(ArrayList<Double[]> entityStates, ArrayList<Entity> entities, ArrayList<Integer> scoreStates, boolean isFinished, int[] catState, double[] catPos){
        this.entityStates = entityStates;
        this.entities = entities;
        this.scoreStates = scoreStates;
        this.catState = catState;
        this.catPos = catPos;
    }

    public double[] getCatPos() {
        return catPos;
    }

    public int[] getCatState() {
        return catState;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public ArrayList<Integer> getScoreStates() {
        return scoreStates;
    }

    public ArrayList<Double[]> getEntityStates() {
        return entityStates;
    }

    public ArrayList<Entity> getEntities() {
        return entities;
    }
}
