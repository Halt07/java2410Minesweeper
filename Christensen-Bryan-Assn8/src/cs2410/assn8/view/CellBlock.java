package cs2410.assn8.view;

import cs2410.assn8.control.*;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;

import static cs2410.assn8.control.Markings.*;

/**
 * @author Bryan
 * @version 1.0
 * @lastUpdated 11/29/2016.
 */

/**
 * CellBlock for MineSweeper-like game
 * extends Button and can be one of two different types:
 * a bomb or normal cell
 */
public class CellBlock extends Button {
    /**
     * int variable to keep track of the neighboring cells that contain bombs
     */
    private int mineNeighbors = 0;
    /**
     * int variables to keep track of the grid position of the cell
     */
    private int row, col;
    /**
     * Markings variable to keep track of whether the cell has been flagged or is thought to be a bomb
     */
    private Markings marked = BLANK;
    /**
     * boolean variable to determine whether the cell has already been clicked or not
     */
    private boolean clicked = false;

    /**
     * Constructor sets the stylesheet to determine visual aspects of the different states of the cell blocks
     * and to set a specified size to avoid graphic issues when changing states
     */
    private  CellBlock(){
        this.getStylesheets().addAll("file:css/custom.css");
        this.setPrefSize(32,32);
    }

    /**
     * Constructor that receives the type of cell block and initializes the setOnMouseReleased method for each type of cell
     * @param cellType
     */
    public CellBlock(String cellType){
        this();
        this.setId(cellType);

        this.setOnMouseReleased(e -> {
            /**If right button is clicked, call setMarked method
             * If game has not yet begun, start timer
             */
            if (e.getButton() == MouseButton.SECONDARY) {
                setMarked();
                if(Main.getCorrectClicks() == 0 && MineMenu.getSeconds() == 0){
                    MineMenu.startTimer();
                }
            }
            /**If left button is clicked and the cell block has not been clicked or disabled determine whether the cell is a bomb or normal
             * If game has not yet begun, start timer
             * If cell is a bomb, end game, disable all cells, and reveal all bombs
             * If cell is normal, display the number of neighboring bombs
             * If cell has 0 neighboring bombs, reveal all neighboring cells
             */
            else{
                if(!clicked && !this.isDisabled()){
                    if(Main.getCorrectClicks() == 0 && MineMenu.getSeconds() == 0){
                        MineMenu.startTimer();
                    }
                    if (this.getId().equalsIgnoreCase("mine-cell")) {
                        this.setId("clicked-mine");
                        clicked = true;
                        this.setDisable(true);
                        Main.gameEndReveal();
                    } else {
                        this.revealNeighbors();
                    }
                }
            }
            /**
             * If there are no more normal cells, reveal all bombs and display winning message
             */
            if(Main.getCorrectClicks() == (400-Main.getBombCount())){
                Main.gameEndReveal();
            }
        });
    }

    /**
     * Increment mineNeighbors variable
     */
    public void incrementMineCount(){
        mineNeighbors++;
    }

    /**
     * Set the Coordinates of the Cell in the game Grid
     * @param c receives column
     * @param r receives row
     */
    public void setCoordinates(int c, int r){
        row = r;
        col = c;
    }

    /**
     * "Increment" Marked state of the Cell
     * If blank, change to unknown and display a question mark
     * If unknown, change to flagged and display a flag image
     * If flagged, change to blank and remove images
     */
    private void setMarked(){
        switch(marked){
            case BLANK:
                marked = UNKNOWN;
                this.setGraphic(new ImageView(new Image("file:css/unknown.png")));
                this.setDisabled(true);
                break;
            case UNKNOWN:
                marked = FLAGGED;
                this.setGraphic(new ImageView(new Image("file:css/flag.png")));
                MineMenu.decrementBombCount();
                break;
            case FLAGGED:
                marked = BLANK;
                this.setGraphic(null);
                this.setDisabled(false);
                MineMenu.incrementBombCount();
                break;
        }
    }

    /**
     * Reveal Neighboring cells if there are no neighboring bombs, and disable all revealed cells as though they had been clicked
     */
    private void revealNeighbors(){
        this.setDisable(true);
        this.setId("clicked-normal");
        if(mineNeighbors == 0 && !clicked){
            clicked = true;
            Main.incrementCorrectClicks();
            try{
                Main.gridCells[col-1][row-1].revealNeighbors();
            }catch(ArrayIndexOutOfBoundsException e){}
            try{
                Main.gridCells[col][row-1].revealNeighbors();
            }catch(ArrayIndexOutOfBoundsException e){}
            try{
                Main.gridCells[col+1][row-1].revealNeighbors();
            }catch(ArrayIndexOutOfBoundsException e){}
            try{
                Main.gridCells[col-1][row].revealNeighbors();
            }catch(ArrayIndexOutOfBoundsException e){}
            try{
                Main.gridCells[col+1][row].revealNeighbors();
            }catch(ArrayIndexOutOfBoundsException e){}
            try{
                Main.gridCells[col-1][row+1].revealNeighbors();
            }catch(ArrayIndexOutOfBoundsException e){}
            try{
                Main.gridCells[col][row+1].revealNeighbors();
            }catch(ArrayIndexOutOfBoundsException e){}
            try{
                Main.gridCells[col+1][row+1].revealNeighbors();
            }catch(ArrayIndexOutOfBoundsException e){}
        }
        else if(mineNeighbors > 0) {
            clicked = true;
            Main.incrementCorrectClicks();
            this.setText((Integer.toString(mineNeighbors)));
        }
    }

    /**
     * Reveal the state of the cells when the game ends
     * If correctly marked, display in green
     * If incorrectly marked, display in yellow
     * If unmarked and a bomb was hit, display in red
     */
    public void endReveal(){
        if(this.getId().equalsIgnoreCase("mine-cell")) {
            clicked = true;
            this.setDisable(true);
            switch (marked) {
                case FLAGGED:
                    this.setId("flagged-mine");
                    break;
                default:
                    if(Main.getCorrectClicks() == (400 - Main.getBombCount())){
                        this.setId("flagged-mine");
                    }
                    else {
                        this.setId("clicked-mine");
                    }
                    break;
            }
        }
        else if(this.getId().equalsIgnoreCase("normal-cell")){
            clicked = true;
            this.setDisable(true);
            switch(marked){
                case FLAGGED:
                    this.setId("flagged-normal");
                    break;
                default:
                    break;
            }
        }
    }
}
