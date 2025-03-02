package ballboy.view;

import com.sun.tools.attach.AgentInitializationException;

/**
 * Class used to keep track of each score in each level
 */
public class Score {
    private Colour colour;
    private double xPos;
    private double yPos;
    private int value=0;

    public Score(Colour colour, double xPos, double yPos){
        this.colour = colour;
        this.xPos = xPos;
        this.yPos = yPos;
    }

    public void setValue(int i){
        this.value = i;
    }

    /**
     * Increments score by 1
     */
    public void incrementScore(){
        this.value++;
    }

    public Colour getColour() {
        return colour;
    }

    public double getxPos() {
        return xPos;
    }

    public double getyPos() {
        return yPos;
    }

    public int getValue() {
        return value;
    }

    /**
     * Colour enum used to give each score RGB colours
     */
    public enum Colour{
        BLUE ("Blue"),
        RED ("Red"),
        GREEN ("Green");

        public final String colour;

        Colour(String colour){
            this.colour = colour;
        }

        public static Colour getValue(Object o){
            if (o.toString().equals("blue")){
                return BLUE;
            } else if (o.toString().equals("red")){
                return RED;
            } else{
                return GREEN;
            }
        }
    }
}

