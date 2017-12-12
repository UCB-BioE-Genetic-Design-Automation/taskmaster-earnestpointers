package org.PA.System.UI.models;

import com.sun.rowset.internal.Row;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

import java.util.HashMap;

public class Plate extends GridPane{

    private HashMap<Integer, Character> numAlpha;
    private HashMap<Character, Integer> alphaNum;
    private String location;


    public Plate(String location) {
        this.location = location;
        numAlpha = new HashMap<>();
        numAlpha.put(0, 'A');
        numAlpha.put(1, 'B');
        numAlpha.put(2, 'C');
        numAlpha.put(3, 'D');
        numAlpha.put(4, 'E');
        numAlpha.put(5, 'F');
        numAlpha.put(6, 'G');
        numAlpha.put(7, 'H');

        alphaNum = new HashMap<>();
        alphaNum.put('A', 0);
        alphaNum.put('B', 1);
        alphaNum.put('C', 2);
        alphaNum.put('D', 3);
        alphaNum.put('E', 4);
        alphaNum.put('F', 5);
        alphaNum.put('G', 6);
        alphaNum.put('H', 7);
    }

    // =================================================================================================================
    // Function Name: removeHighlights
    // Feature Description: clears UI of last task's highlights
    // Class: Plate
    // =================================================================================================================
    public void removeHighlights(){
        Node plateGrid = this.getChildren().get(0);
        this.getChildren().clear();
        this.getChildren().add(0, plateGrid);
    }

    // TODO: Add Grid drawing to this function
    // TODO: Hide columns 13 and 14 and rows 9 and 10
    // TODO: generalize to all containers
    // =================================================================================================================
    // Function Name: highlightWells
    // Feature Description: highlights rest of row and column from well for user to quickly see well
    // Implementation Description: horizontal bar = 1, vertical bar = 0
    // Class: Plate
    // =================================================================================================================
    public void highlightWell(String RowCol, String id){

        String highlightColor = "";

        if (id.equals("source")) {
            highlightColor = "#53b57f";
        } else if (id.equals("destination")){
            highlightColor = "#c03a3c";
        }


        int row = alphaNum.get(RowCol.charAt(0));
        int col = 0;

        if (this.location.equals("A1")){
            col = Integer.parseInt(RowCol.substring(1))-1;
        } else if (this.location.equals("A2")){
            col = Integer.parseInt(RowCol.substring(1))+1;
        }

        // subtract 1 bc GridPane index by 0
//        int col = Integer.parseInt(RowCol.substring(1))-1;
        int maxRow = 9;
        int maxCol = 13;

            // row tab

        StackPane rowLabel = new StackPane();
        rowLabel.setStyle("-fx-background-color: " + highlightColor);
        rowLabel.setMinSize(50, 46);
        Label rowLetter = new Label();
        rowLetter.setText(Character.toString(numAlpha.get(row)));
        rowLetter.setFont(Font.font("Verdana", 16));
        rowLetter.setTextFill(Paint.valueOf("white"));
        rowLabel.getChildren().add(rowLetter);

        // make row
        if (this.location.equals("A1")){
            for (int i = col; i < maxCol; i++) {
                PlateWell well = new PlateWell();
                well.fillWell(id);
                this.add(well, i, row);
            }

            this.add(rowLabel, maxCol, row);
        } else if (this.location.equals("A2")){
            for (int i = 1; i < col; i++) {
                PlateWell well = new PlateWell();
                well.fillWell(id);
                this.add(well, i, row);
            }
            this.add(rowLabel, 0, row);
        }

        // make column
        for (int i = row; i < maxRow; i++) {
            PlateWell well = new PlateWell();
            well.fillWell(id);
            this.add(well, col, i);
        }
        // column tab
        StackPane colLabel = new StackPane();
        colLabel.setStyle("-fx-background-color: " + highlightColor);
        colLabel.setMinSize(46, 46);
        Label colNum = new Label();

        colNum.setText(Integer.toString(Integer.parseInt(RowCol.substring(1))));
        colNum.setFont(Font.font("Verdana", 16));
        colNum.setTextFill(Paint.valueOf("white"));
        colLabel.getChildren().add(colNum);

        // column tab
        this.add(colLabel, col, maxRow);
    }

    // =================================================================================================================
    // Function Name: make96Wells
    // Feature Description: creates visual grid that represents wells of 96-well pcr plate
    // Implementation Description: horizontal bar = 1, vertical bar = 0
    // Class: Plate
    // =================================================================================================================
    public void make96Wells (String position) {
        this.setHgap(0);
        this.setVgap(0);
        this.setSnapToPixel(false);

        if (position.equals("A1")) {
            this.setPadding(new Insets(26, 0, 0, 38));

//            for (int i = 0; i < 12; i++) {
//                for (int j = 0; j < 8; j++) {
//                    Pane pane = new Pane();
//                    pane.setStyle("-fx-background-fill: black, #f6f9fc;" +
//                            "-fx-background-insets: 0, 5 4 3 2 ;");
//                    this.add(pane, i, j);
//                }
//            }

        } else if (position.equals("A2")) {
            this.setPadding(new Insets(26, 38, 0, 0));

//            for (int i = 3; i < 14; i++) {
//                for (int j = 0; j < 8; j++) {
//                    Pane pane = new Pane();
//                    pane.setStyle("-fx-background-color: #f6f9fc;" +
//                            "-fx-background-insets: 0, 0 1 1 0 ;");
//                    this.add(pane, i, j);
//                }
//            }
        }
        for (int i = 0; i <= 13; i++) {
            ColumnConstraints column = new ColumnConstraints(46);
            this.getColumnConstraints().add(column);
        }

        for (int i = 0; i <= 9; i++) {
            RowConstraints row = new RowConstraints(46);
            this.getRowConstraints().add(row);
        }

        this.setGridLinesVisible(true);

        this.setPrefSize(354, 546);
    }
}
