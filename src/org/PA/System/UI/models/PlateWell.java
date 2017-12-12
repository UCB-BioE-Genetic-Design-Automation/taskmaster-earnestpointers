package org.PA.System.UI.models;

import javafx.scene.layout.StackPane;

public class PlateWell extends StackPane {

    public PlateWell(){};

    // =================================================================================================================
    // Function Name: fillWell
    // Feature Description: highlights individual wells of 96-well PCR plate
    // Implementation Description: add a StackPane to each GridPane cell and set the background
    // Class: PlateWell
    // =================================================================================================================
    void fillWell(String id) {

        String highlightColor = "";

        if (id.equals("source")) {
            highlightColor = "#53b57f";
        } else if (id.equals("destination")){
            highlightColor = "#c03a3c";
        }
        this.setStyle("-fx-background-color: " + highlightColor);
    }

}
