package cs2410.assn8.control;

import cs2410.assn8.view.*;
import cs2410.assn8.view.CellBlock;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @author Bryan
 * @version 1.0
 * @lastUpdated 11/29/2016.
 */

/**
 * Main Class to run the MineSweeper-like program
 * extends Application
 */
public class Main extends Application {
    /**
     * 2-Dimensional array to contain the CellBlock elements in a grid pattern
     */
    public static CellBlock[][] gridCells = new CellBlock[20][20];
    /**
     * GridPane to display the CellBlock elements in the actual program window
     */
    private static GridPane grid = new GridPane();
    /**
     * Int variable to determine the number of bombs within the game
     */
    private static int bombCount = 100;
    /**
     * Int variable to determine how many correct clicks have been made
     */
    private static int correctClicks = 0;

    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane mainPane = new BorderPane();
        MineMenu menu = new MineMenu(bombCount);
        /**
         * Set the Menu Button action
         * If a game has already been started, reset the timer
         * Recreate all CellBlocks and repopulate the grid
         */
        menu.setBtn(event -> {
            MineMenu.stopTimer();
            String msg = "Starting New Game";
            Alert alertMessage = new Alert(Alert.AlertType.INFORMATION, msg);
            alertMessage.setHeaderText(null);
            alertMessage.showAndWait();
            createArray();}
        );
        createArray();

        /**
         * Set the Menu/Score area to the top of the Display
         */
        mainPane.setTop(menu);
        /**
         * Set the Game Grid area to the top of the Display
         */
        mainPane.setCenter(grid);
        BorderPane.setAlignment(mainPane.getTop(), Pos.CENTER);

        Scene scene = new Scene(mainPane);

        primaryStage.setScene(scene);
        primaryStage.show();
        /**
         * Make the Window unable to be resized
         */
        primaryStage.setResizable(false);
        primaryStage.sizeToScene();
        /**
         * If the Window is closed, stop the Timer
         */
        primaryStage.setOnCloseRequest(e -> MineMenu.stopTimer());
    }

    /**
     * Create an array of bombs and normal cells
     * Shuffle the array and populate it into the grid
     * If a current game is running, reset all variables
     */
    private void createArray(){
        grid.getChildren().clear();
        ArrayList<CellBlock> gridArray = new ArrayList<>(400);
        gridCells = new CellBlock[20][20];
        correctClicks = 0;
        MineMenu.resetSeconds();
        MineMenu.resetBombCount(bombCount);
        for(int i = 0; i < bombCount; i++) {
            gridArray.add(new CellBlock("mine-cell"));
        }
        for(int i = 0; i < (400-bombCount); i++){
            gridArray.add(new CellBlock("normal-cell"));
        }
        Collections.shuffle(gridArray);
        for (int c = 0; c < 20; c++) {
            for (int r = 0; r < 20; r++) {
                gridCells[c][r] = gridArray.remove(gridArray.size()-1);
                grid.add(gridCells[c][r],c,r);
                gridCells[c][r].setCoordinates(c,r);
            }
        }

        for (int c = 0; c < 20; c++){
            for(int r = 0; r < 20; r++){
                if(gridCells[c][r].getId().equalsIgnoreCase("mine-cell")){
                    try{
                        gridCells[c-1][r-1].incrementMineCount();
                    }catch(ArrayIndexOutOfBoundsException e){}
                    try{
                        gridCells[c][r-1].incrementMineCount();
                    }catch(ArrayIndexOutOfBoundsException e){}
                    try{
                        gridCells[c+1][r-1].incrementMineCount();
                    }catch(ArrayIndexOutOfBoundsException e){}
                    try{
                        gridCells[c-1][r].incrementMineCount();
                    }catch(ArrayIndexOutOfBoundsException e){}
                    try{
                        gridCells[c+1][r].incrementMineCount();
                    }catch(ArrayIndexOutOfBoundsException e){}
                    try{
                        gridCells[c-1][r+1].incrementMineCount();
                    }catch(ArrayIndexOutOfBoundsException e){}
                    try{
                        gridCells[c][r+1].incrementMineCount();
                    }catch(ArrayIndexOutOfBoundsException e){}
                    try{
                        gridCells[c+1][r+1].incrementMineCount();
                    }catch(ArrayIndexOutOfBoundsException e){}
                }
            }
        }
    }

    /**
     * Set the number of Bombs
     * will be used in later version when difficulty is added
     * @param bombs
     */
    public void setBombCount(int bombs){
        bombCount = bombs;
    }

    /**
     * Get the current number of bombs for the chosen difficulty
     * @return
     */
    public static int getBombCount(){
        return bombCount;
    }

    /**
     * Reveal the Bombs in the Game area and stop the timer
     * If the game was completed successfully, display a message displaying their completion time
     * If the game was lost, display an appropriate message
     */
    public static void gameEndReveal(){
        for (int c = 0; c < 20; c++) {
            for (int r = 0; r < 20; r++) {
                gridCells[c][r].endReveal();
            }
        }
        MineMenu.stopTimer();
        if(correctClicks == (400-bombCount)){
            String msg = "You Won! It took you " + MineMenu.getSeconds() + " seconds.";
            Alert alertMessage = new Alert(Alert.AlertType.INFORMATION, msg);
            alertMessage.setHeaderText("Winner!");
            alertMessage.setGraphic(null);
            alertMessage.showAndWait();
        }
        else{
            String msg = "You lost... It took you " + MineMenu.getSeconds() + " seconds.\n Maybe you should try again.";
            Alert alertMessage = new Alert(Alert.AlertType.INFORMATION, msg);
            alertMessage.setHeaderText("Too Bad!");
            alertMessage.setGraphic(null);
            alertMessage.showAndWait();
        }
    }

    /**
     * Increment the number of correct Clicks
     */
    public static void incrementCorrectClicks(){
        correctClicks++;
    }

    /**
     * Get the number of correct Clicks
     * @return
     */
    public static int getCorrectClicks(){
        return correctClicks;
    }
}
