package ballboy.model;

import ballboy.model.entities.memento.Caretaker;
import ballboy.model.entities.memento.GameState;
import ballboy.model.entities.memento.LevelState;
import ballboy.model.levels.LevelImpl;
import ballboy.view.Score;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Implementation of the GameEngine interface.
 * This provides a common interface for the entire game.
 */
public class GameEngineImpl implements GameEngine {
    private ArrayList<Level> levels;
    private int currentLevel=0;
    private boolean gameOver=false;
    private int[] previousScores=new int[]{0,0,0};
    private final Caretaker gameStateCaretaker= new Caretaker(this);

    public GameEngineImpl(ArrayList<Level> level) {
        this.levels = level;
    }

    public Caretaker getGameStateCaretaker(){
        return this.gameStateCaretaker;
    }

    public Level getCurrentLevel() {
        return levels.get(currentLevel);
    }

    public void startLevel() {
        currentLevel++;
    }

    public int[] getPreviousScores(){
        return previousScores;
    }

    /**
     * Save the game state
     * @return the state of the game at this moment
     */
    public GameState save(){
        ArrayList<LevelState> newLevels = new ArrayList<>();
        for (Level l : levels){
            newLevels.add(l.saveState());
        }
        int[] newPreviousScores = new int[3];
        newPreviousScores[0] = previousScores[0];
        newPreviousScores[1] = previousScores[1];
        newPreviousScores[2] = previousScores[2];
        return new GameState(newLevels, currentLevel, previousScores);
    }


    /**
     * Load the game into a previous state
     * @param gameState the state of the game to load to
     */
    public void load(GameState gameState){
        //If we havent got a save leave
        if (gameState == null) {return;}

        //Load all the levels to the correct state
        for (int i = 0; i < gameState.getLevelState().size(); i++){
            (this.levels.get(i)).loadState(gameState.getLevelState().get(i));
        }

        //Adjust score if the level is changing
        this.previousScores = new int[]{0,0,0};
        for (int i = 0; i < gameState.getLevelToLoad(); i++){
            for (int j = 0; j < gameState.getLevelState().get(i).getScoreStates().size(); j++) {
                this.previousScores[j] += gameState.getLevelState().get(i).getScoreStates().get(j);
            }
        }

        //And adjust GameEngine variables
        this.currentLevel = gameState.getLevelToLoad();
    }

    public boolean boostHeight() {
        return getCurrentLevel().boostHeight();
    }

    public boolean dropHeight() {
        return getCurrentLevel().dropHeight();
    }

    public boolean moveLeft() {
        return getCurrentLevel().moveLeft();
    }

    public boolean moveRight() {
        return getCurrentLevel().moveRight();
    }

    public boolean isGameOver(){ return this.gameOver; }

    public void tick() {
        getCurrentLevel().update();

        //Check if we need to change levels
        if (getCurrentLevel().isFinished()){
            //Update values to increment to new levels score
            int i=0;
            for (Score s : getCurrentLevel().getScores()){
                previousScores[i] += s.getValue();
                i++;
            }

            currentLevel++;
            if (currentLevel >= levels.size()){
                currentLevel--;
                gameOver=true;
            }
        }
    }
}