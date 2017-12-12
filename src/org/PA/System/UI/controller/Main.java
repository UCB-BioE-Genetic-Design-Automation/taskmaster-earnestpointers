package org.PA.System.UI.controller;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import org.PA.System.UI.view.View;
import org.ucb.c5.semiprotocol.model.*;
import org.ucb.c5.semiprotocol.ParseSemiprotocol;
import org.ucb.c5.utils.FileUtils;

public class Main extends Application {

    // =================================================================================================================
    // replace pathToSemiprotocol with the path to your semiprotocol
    // =================================================================================================================
    String pathToSemiprotocol = "semiprotocol/data/test.txt";



    private Semiprotocol semiprotocol;
    private int semiprotocolLength;
    private int currStep = 0;
    private View bench;
    private Node step;
    private Node next;
    private Node play;
    private String mode = "manual";

    // =================================================================================================================
    // Function Name: start
    // Feature Description: Launches application, sets UI, listens for clicks and space bar taps, main application loop
    // Class: Main
    // =================================================================================================================
    @Override
    public void start(Stage primaryStage) throws Exception{

        // read Semiprotocol text file at PATH and create Semiprotocol object
        String text = FileUtils.readResourceFile(pathToSemiprotocol);
        ParseSemiprotocol parser = new ParseSemiprotocol();
        parser.initiate();
        semiprotocol = parser.run(text);
        semiprotocolLength = semiprotocol.getSteps().size();

        // initialize UI application window by adding bar and
        // set background color
        bench = new View();
        bench.init(semiprotocolLength, currStep);
        bench.displayTaskInfo("Begin protocol by selecting an option above.",
                true);


        // standard JavaFX boilerplate to set scene and start window (stage)
        Scene scene = new Scene(bench, 1440, 900);
        primaryStage.setTitle("PA System");
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.show();


        // animation class that implements Play feature
        AnimationTimer playLoop = new AnimationTimer() {

            float maximumStep = Float.MAX_VALUE;
            long previousTime = 0;
            float accumulatedTime = 0;
            int size = semiprotocol.getSteps().size();

            @Override
            public void handle(long currentTime) {

                if (previousTime == 0) {
                    previousTime = currentTime;
                    return;
                }

                float secondsElapsed = (currentTime - previousTime) / 1e9f; /* nanoseconds to seconds */
                float secondsElapsedCapped = Math.min(secondsElapsed, maximumStep);
                accumulatedTime += secondsElapsedCapped;
                previousTime = currentTime;

                if (accumulatedTime > 2) {
                    if (currStep < size) {
                        nextStep();
                    } else if (currStep == size) {
                        bench.endOfProtocolDisplay();
                        System.out.printf("END OF PROTOCOL");
                    }
                    accumulatedTime = 0;
                }
            }

        };

        // buttons and their mouse and space bar triggers
        next = bench.getNextButton();
        play = bench.getPlayButton();

        play.setOnMouseEntered((new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                play.setStyle(bench.getStyle4());
                String playText = "Click and hold Play until UI changes to watch your protocol.";
                bench.displayTaskInfo(playText, true);
            }
        }));
        play.setOnMouseExited((new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                play.setStyle(bench.getStyle1());
                bench.displayTaskInfo("Begin protocol by selecting an option above.",
                        true);
            }
        }));

        play.setOnMousePressed((new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                play.setStyle(bench.getStyle5());
                mode = "play";
                playLoop.start();
            }
        }));
        play.setOnMouseReleased((new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                play.setStyle(bench.getStyle4());
            }
        }));

        next.setOnMouseEntered((new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                next.setStyle(bench.getStyle4());
                String playText = "Left click to step through your protocol.";
                if (currStep == 0) {
                    bench.displayTaskInfo(playText, true);
                }
            }
        }));
        next.setOnMouseExited((new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                next.setStyle(bench.getStyle1());
                if (currStep == 0 ) {
                    bench.displayTaskInfo("Begin protocol by selecting an option above.",
                            true);
                }
            }
        }));

        next.setOnMousePressed((new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                next.setStyle(bench.getStyle1());

                if (currStep == semiprotocolLength) {
                    bench.endOfProtocolDisplay();
                    System.out.println("END OF PROTOCOL.");
                } else {
                    nextStep();
                }
            }
        }));
        next.setOnMouseReleased((new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                next.setStyle(bench.getStyle4());
            }
        }));

        next.setOnKeyPressed((new EventHandler<KeyEvent>() {
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.SPACE) {
                    next.setStyle(bench.getStyle4());
                }
            }
        }));

        next.setOnKeyReleased((new EventHandler<KeyEvent>() {
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.SPACE) {

                    next.setStyle(bench.getStyle1());

                    if (currStep == semiprotocolLength) {
                        bench.endOfProtocolDisplay();
                        System.out.println("END OF PROTOCOL.");
                    } else {
                        nextStep();
                    }

                }
            }
        }));
    }

    // =================================================================================================================
    // Function Name: nextStep
    // Feature Description: Determines next step of protocol, processes semiprotocol, and updates UI
    // Class: Main
    // =================================================================================================================
    private void nextStep() {
        // determine next step
        Task task = semiprotocol.getSteps().get(currStep);


        // check if application is at the beginning of protocol to use the correct UI
        if (currStep == 0){
            bench.doDisplay(mode);
        }

        // clear UI of last task's highlights if they exist
        bench.removeHighlights();
        // update current step count in View object so progress bar updates
        bench.setCurrentStep(currStep);
        // update progress bar
        bench.updateProgress();

        // execute the task
        switch(task.getOperation()) {
            case addContainer:
                AddContainer addcon = (AddContainer) task;
                String containerName = addcon.getName();
                Container container = addcon.getTubetype();

                // find index of forward slash to determine
                int separator = addcon.getLocation().indexOf("/");
                String containerLocation = addcon.getLocation().substring(separator+1);

                if (bench.LocationIsAvailable(containerLocation)){
                    if (container == Container.pcr_plate_96) {
                        bench.displayTaskInfo("Add 96-well PCR plate to " + containerLocation, false);
                        bench.addPlate(containerName, containerLocation);
                    } else if (container == Container.pcr_tube){
                        bench.displayTaskInfo("Add PCR tube to " + containerLocation, false);
                        // TODO
                    } else if (container == Container.pcr_strip){
                        bench.displayTaskInfo("Add PCR strip to " + containerLocation, false);
                        // TODO
                    } else if (container == Container.eppendorf_1p5mL){
                        bench.displayTaskInfo("Add 1.5 mL Eppendorf tube to " + containerLocation, false);
                        // TODO
                    } else if (container == Container.eppendorf_2mL){
                        bench.displayTaskInfo("Add 2.0 mL Eppendorf tube to " + containerLocation, false);
                        // TODO
                    }
                } else {
                    // inform user that the location is already being used
                    StringBuilder outputMessage = new StringBuilder();
                    outputMessage.append("Cannot add ");
                    outputMessage.append(containerName);
                    outputMessage.append(" to ");
                    outputMessage.append(containerLocation);
                    outputMessage.append(". Space is being used!");
                    bench.displayTaskInfo(outputMessage.toString(), false);
                    System.out.println("PLATE SPACE IS OCCUPIED.");
                }
                break;

            case removeContainer:
                // TODO
                break;

            case transfer:
                Transfer transfer = (Transfer) task;
                int lastSrc = transfer.getSource().indexOf("/");
                int lastDst = transfer.getDest().indexOf("/");
                String src = transfer.getSource();
                String dst = transfer.getDest();
                System.out.println("source = " + src + " " + src.substring(0, lastSrc));
                System.out.println("destination = " + dst + " " + dst.substring(0, lastDst));
                bench.displayTaskInfo("Transfer " + transfer.getVolume() + " uL from " +
                                src.substring(0, lastSrc) + " to " + dst.substring(0, lastDst), false);
                bench.transfer(src, dst);
                break;

            case multichannel:
                // TODO
                break;
            case dispense:
                // TODO
                Dispense dispense = (Dispense) task;
                String dstDispense = dispense.getDstContainer();
                String reagent = dispense.getReagent().toString();
                String vol = dispense.getVolume().toString();
                bench.displayTaskInfo("Dispense " + vol + " uL of " + reagent + " to " + dstDispense
                        , false);
                // TODO Implement bench.dispense(dstDispense);
                break;
        }
        currStep++;
    }

    public static void main(String[] args) throws Exception{ launch(args); }
}

