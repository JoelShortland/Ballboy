package ballboy.view;

import ballboy.model.GameEngine;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Used to display scores
 */
public class ScoreDisplay implements BackgroundDrawer{

    private Text score1 = new Text();
    private Text score2 = new Text();
    private Text score3 = new Text();
    private int[] scoresToAdd = new int[]{0,0,0};
    private ArrayList<Text> scoreValues = new ArrayList<>();

    public ScoreDisplay(GameEngine model, Pane pane){
        scoreValues.add(score1);
        scoreValues.add(score2);
        scoreValues.add(score3);
        pane.getChildren().addAll(score1, score2, score3);
    }


    @Override
    public void draw(GameEngine model, Pane pane) {
        List<Score> scores = model.getCurrentLevel().getScores();
        scoresToAdd = model.getPreviousScores();

        int index = 0;
        for (Score s : scores){
            Text t = scoreValues.get(index);

            t.setX(s.getxPos());
            t.setY(s.getyPos());
            t.setText(s.getColour().toString() + ": " + (s.getValue()+ scoresToAdd[index]));
            index++;
            if (s.getColour().toString().equalsIgnoreCase("blue")){
                t.setFill(Color.BLUE);
            } else if (s.getColour().toString().equalsIgnoreCase("red")){
                t.setFill(Color.RED);
            } else{
                t.setFill(Color.GREEN);
            }
        }
    }

    /**
     * Display winner on game win
     * @param model the GameEngine
     * @param pane pane being drawn on
     */
    public void victoryDisplay(GameEngine model, Pane pane){
        //First get winners text
        Text t1 = new Text();
        t1.setText("WINNER");
        t1.setX(210);
        t1.setY(100);
        t1.setFill(Color.BLUE);
        t1.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 48));
        pane.getChildren().add(t1);
    }

    @Override
    public void update(double xViewportOffset, double yViewportOffset) {}
}
