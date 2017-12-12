package org.PA.System.UI.models;

import javafx.scene.Node;
import javafx.scene.layout.StackPane;

public class BenchSpace extends StackPane {

    public BenchSpace(){}

    // =================================================================================================================
    // Function Name: addContainer
    // Feature Description: Area on UI for 96-well plates
    // Implementation Description: StackPane parent node and GridPane child node
    // Input: GridPane
    // Output: StackPane that contains GridPane
    // =================================================================================================================
    public void addContainer(Node object) {
        this.getChildren().addAll(object);
    }
}
