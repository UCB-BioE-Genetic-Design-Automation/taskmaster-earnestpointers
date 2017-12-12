package org.PA.System.UI.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import org.PA.System.UI.models.BenchSpace;
import org.PA.System.UI.models.Plate;

import java.util.HashMap;

public class View extends BorderPane{

    private int semiprotocolLength;
    private int currentStep;
    private int stepsRemaining;

    private HBox bar;
    private HBox top;
    private BorderPane twoRowBar;

    private Button nextButton;
    private Button playButton;
    private Button progress;

    private Plate left;
    private Plate right;

    private String style1;
    private String style2;
    private String style3;
    private String style4;
    private String style5;

    private HashMap<String, String> ContainerNameToBenchLocation;

    // View Class Functions

    public View() {

       String baseStyle =
                "-fx-font-family: \"Verdana\";\n" +
                "-fx-padding: 10px 12px; " +
                "-fx-border-radius: 4px; " +
                "-fx-effect: dropshadow(two-pass-box, #e6ebf1, 3, 0, 0, 1);";

        style1 = baseStyle +
                "-fx-font-size: 36;" +
                "-fx-background-color: white; ";

        style2 = baseStyle +
                " -fx-font-size: 36;" +
                "-fx-background-color: #f6f9fc; ";


        style3 = baseStyle +
                " -fx-font-size: 24;" +
                "-fx-background-color: #f6f9fc; ";

        style4 = baseStyle +
                " -fx-font-size: 36;" +
                " -fx-background-color: #53b57f; " +
                " -fx-text-fill: white;";

        style5 = baseStyle +
                " -fx-font-size: 36;" +
                " -fx-text-fill: white;" +
                "-fx-background-color: #627de2;";
    }

    public void init(int length, int curr){

        semiprotocolLength = length;
        currentStep = curr;
        calcStepsRemaining();

        ContainerNameToBenchLocation = new HashMap<String, String>();

        Button welcome = new Button("Welcome to PA System!");
        welcome.setStyle(style1);
        top = addHBox(welcome);

        nextButton= new Button("Step-by-Step");
        playButton = new Button("Play");


        playButton.setStyle(style1);
        nextButton.setStyle(style1);

        bar = addHBox("bottom");

        HBox startMenu = addHBox(nextButton);
        startMenu.getChildren().add(playButton);
        twoRowBar = new BorderPane();
        twoRowBar.setTop(top);
        twoRowBar.setCenter(startMenu);
        twoRowBar.setBottom(bar);

        this.setTop(twoRowBar);
        this.setBackground();
    }


    // UI functions
    public void setBackground(){
        // set background to soft grey
        this.setStyle("-fx-background-color: #f6f9fc;");
    }

    public String getStyle1(){
        return style1;
    }

    public String getStyle2(){
        return style2;
    }

    public String getStyle4() { return style4; }

    public String getStyle5() {return style5;}

    public Node getNextButton(){
        return nextButton;
    }

    public Node getPlayButton() { return playButton; }

    public void setCurrentStep(int curr) {currentStep = curr;}

    private void calcStepsRemaining(){ stepsRemaining = semiprotocolLength - currentStep;}

    public void removeHighlights() {
        if (left != null) {
            left.removeHighlights();
        }

        if (right != null){
            right.removeHighlights();
        }

    }

    public void doDisplay(String mode){
        this.setTop(null);
        twoRowBar.setTop(null);
        twoRowBar.setCenter(null);

        progress = new Button();
        progress.setText(Integer.toString(stepsRemaining) + " Steps Remaining");
        progress.setStyle(getStyle2());

        if (mode.equals("manual")){
            nextButton.setText("next step");
            top = addHBox(nextButton);
            twoRowBar.setTop(addHBox(progress));
            twoRowBar.setCenter(top);
        } else if (mode.equals("play")){
            top = addHBox(progress);
            twoRowBar.setTop(top);
        }
        this.setBottom(twoRowBar);
    }

    public void updateProgress(){
        calcStepsRemaining();
        progress.setText(Integer.toString(stepsRemaining) + " Steps Remaining");
    }

    public void displayTaskInfo(String task, boolean startup){

        Button message = new Button(task);
        HBox updated = addHBox(message);

        String styleToUse = "";
        if (startup) {
            styleToUse = style3;
        } else {
            styleToUse = style2;
        }
        twoRowBar.setBottom(updated);
        message.setStyle(styleToUse);
    }

    public void endOfProtocolDisplay(){
        this.removeHighlights();
        Button done = new Button("You have completed the protocol.");
        done.setStyle(style1);
        HBox fini = addHBox(done);
        this.setBottom(fini);
    }

    // =================================================================================================================
    // Function Name: addHBox
    // Feature Description: Next Button box, Progress Box, Task information box
    // Implementation Description: Hbox parent node and Button child node
    // Input: Button
    // =================================================================================================================
    private HBox addHBox(Button message){
        HBox hbox = new HBox();

        hbox.setPadding(new Insets(30, 0, 30, 0));
        hbox.setAlignment(Pos.CENTER);
        hbox.setSpacing(50);
        hbox.setStyle("-fx-background-color: #f6f9fc;");
        hbox.getChildren().add(message);

        return hbox;
    }

    // =================================================================================================================
    // Function Name: addHBox
    // Feature Description: Welcome message box
    // Implementation Description: Hbox parent node and Button child node
    // Input: String
    // =================================================================================================================
    private HBox addHBox(String id) {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(30, 0, 30, 0));
        hbox.setAlignment(Pos.CENTER);
        hbox.setSpacing(50);
        hbox.setStyle("-fx-background-color: #f6f9fc;");

        if (id.equals("bottom")) {
            return hbox;
        }

        String welcome1 = "Welcome to PA System!";
        Button nextB = new Button(welcome1);
        nextB.setStyle(style1);

        hbox.getChildren().add(nextB);

        return hbox;
    }


    // UI functions for tasks
    public boolean LocationIsAvailable(String benchLocation){
        return !ContainerNameToBenchLocation.containsValue(benchLocation);
    }

    public void addPlate(String name, String location){
        Plate plate = new Plate(location);
        BenchSpace plateSpace = new BenchSpace();
        plateSpace.addContainer(plate);

        // A1 = top left
        if (location.equals("A1")) {
            ContainerNameToBenchLocation.put(name, "A1");
            plate.make96Wells("A1");
            this.setLeft(plate);
            left = plate;

            // A2 = top right
        } else if (location.equals("A2")){
            ContainerNameToBenchLocation.put(name, "A2");
            plate.make96Wells("A2");
            this.setRight(plate);
            right = plate;
        } else {
            // TODO: Expand bench space for other containers
            System.out.println("Case not handled yet.");
        }
    }

    public void transfer(String src, String dst){

        int srcForwardSlashIndex = src.indexOf("/");
        int dstForwardSlashIndex = dst.indexOf("/");

        String srcPlate = ContainerNameToBenchLocation.get(src.substring(0, srcForwardSlashIndex));
        String dstPlate = ContainerNameToBenchLocation.get(dst.substring(0, dstForwardSlashIndex));


        // highlight row and column to indicate well to aspirate from or dispense to
        if (srcPlate.equals("A1")) {
            left.highlightWell(src.substring(srcForwardSlashIndex+1, src.length()), "source");
            if (dstPlate.equals("A1")){
                left.highlightWell(dst.substring(dstForwardSlashIndex+1, dst.length()), "destination");
            } else if (dstPlate.equals("A2")) {
                right.highlightWell(dst.substring(dstForwardSlashIndex+1, dst.length()), "destination");
            }
        } else if (srcPlate.equals("A2")) {
            right.highlightWell(src.substring(srcForwardSlashIndex+1, src.length()), "source");
            if (dstPlate.equals("A1")) {
                left.highlightWell(dst.substring(dstForwardSlashIndex+1, dst.length()), "destination");
            } else if (dstPlate.equals("A2")) {
                right.highlightWell(dst.substring(dstForwardSlashIndex+1, dst.length()), "destination");
            }
        }
    }

}
