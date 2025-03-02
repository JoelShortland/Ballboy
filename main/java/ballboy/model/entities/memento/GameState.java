package ballboy.model.entities.memento;

import ballboy.model.Level;

import java.util.ArrayList;
import java.util.List;

public class GameState {
    private ArrayList<LevelState> levelStates;
    private int levelToLoad;
    private int[] previousScores;

    public GameState(ArrayList<LevelState> levelStates, int currentLevel, int[] previousScores){
        this.levelStates = levelStates;
        this.levelToLoad = currentLevel;
        this.previousScores = previousScores;
    }

    public ArrayList<LevelState> getLevelState(){
        return this.levelStates;
    }

    public int getLevelToLoad() {
        return levelToLoad;
    }

    public int[] getPreviousScores() {
        return previousScores;
    }
}


