package ballboy.model.entities.memento;

import ballboy.model.GameEngine;

public class Caretaker {
    private GameState gameState=null;
    private GameEngine gameEngine;

    public Caretaker(GameEngine gameEngine){
        this.gameEngine = gameEngine;
    }

    public void load(){
        gameEngine.load(gameState);
    }

    public void save(){
        gameState = gameEngine.save();
    }

}
