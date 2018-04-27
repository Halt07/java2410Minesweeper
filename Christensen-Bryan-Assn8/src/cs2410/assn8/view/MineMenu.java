package cs2410.assn8.view;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Bryan
 * @version 1.0
 * @lastUpdated 11/30/2016.
 */

/**
 * Menu for MineSweeper-like game scoring area
 * extends HBox and displays the number of bombs in the game, a start new game button, and a timer
 * future version will also display a dropdown menu to change difficulty of the game
 */
public class MineMenu extends HBox {

    /**
     * Button to start a new game
     */
    private Button btn = new Button("Start New Game");
    /**
     * Text box to display the timer
     */
    private static Text timerDisplay = new Text();
    /**
     * Text box to display the number of bombs still present
     */
    private static Text bombCountDisplay = new Text();
    /**
     * Timer to determine length of time during which the current game has been played
     */
    private static Timer timer = new Timer();
    /**
     * int variable to determine how many bombs are present in the game
     */
    private static int bombCount;
    /**
     * int variable to determine how many seconds the game has lasted
     */
    private static int seconds = 0;

    /**
     * Constructor receives the number of bombs and initializes displayed objects
     * @param bombs
     */
    public MineMenu(int bombs){
        bombCount = bombs;
        setBombCountDisplay();
        setTimerDisplay();
        this.getChildren().addAll(bombCountDisplay,btn,timerDisplay);
        HBox.setMargin(btn,new Insets(0,120,0,120));
        this.setPrefWidth(640);
        this.setAlignment(Pos.CENTER);
    }

    /**
     * Sets the Start New Game button action
     * @param e
     */
    public void setBtn(EventHandler<ActionEvent> e){
        btn.setOnAction(e);
    }

    /**
     * Sets the display to show the number of bombs left in the game
     */
    private static void setBombCountDisplay(){
        bombCountDisplay.setText("Bombs Left: " + bombCount);
    }

    /**
     * Increment the number of bombs that are still unflagged
     */
    public static void incrementBombCount(){
        bombCount++;
        bombCountDisplay.setText("Bombs Left: " + bombCount);
    }

    /**
     * Decrement the number of bombs that are still unflagged
     */
    public static void decrementBombCount(){
        bombCount--;
        setBombCountDisplay();
    }

    /**
     * Reset the number of bombs to the number given
     * @param bombs
     */
    public static void resetBombCount(int bombs){
        bombCount = bombs;
        setBombCountDisplay();
    }

    /**
     * Sets the display to show the amount of time that has passed in the current game
     */
    private static void setTimerDisplay(){
        timerDisplay.setText(seconds + " Seconds");
    }

    /**
     * Reset the time display
     */
    public static void resetSeconds(){
        createNewTimer();
        seconds = 0;
        setTimerDisplay();
    }

    /**
     * Get the number of seconds
     * @return
     */
    public static int getSeconds(){
        return seconds;
    }

    /**
     * Start the Timer
     */
    public static void startTimer(){
        seconds = 0;
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                seconds++;
                timerDisplay.setText(seconds + " Seconds");
            }
        },0,1000);
    }

    /**
     * Stop the timer
     */
    public static void stopTimer(){
        timer.cancel();
    }

    /**
     * Create a new timer
     */
    private static void createNewTimer(){
        timer = new Timer();
    }
}
