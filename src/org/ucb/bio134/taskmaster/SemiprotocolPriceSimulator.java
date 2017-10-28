package org.ucb.bio134.taskmaster;

import java.util.ArrayList;
import java.util.List;

import java.util.HashMap;
import java.util.Map;

import java.util.HashSet;
import java.util.Set;

import org.ucb.c5.semiprotocol.model.*;
import org.ucb.bio134.taskmaster.model.Tip;
import org.ucb.c5.semiprotocol.ParseSemiprotocol;
import org.ucb.c5.utils.FileUtils;

import static org.ucb.c5.semiprotocol.model.LabOp.removeContainer;


/**
 * @author Bryant
 * @author J. Christopher Anderson
 */
public class SemiprotocolPriceSimulator {

    private PriceCalculator cost;
    private Map<Reagent, Double>     localReagentCount;
    private Map<Container, Integer>  localTubeCount;
    private Map<Tip, Integer>        localTipCount;

    public void initiate() throws Exception {

        // Initialize PriceCalculator
        cost = new PriceCalculator();
        cost.initiate();

        // Initialize HashMaps for Reagents, Containers, and Tips
        localReagentCount  = new HashMap<>();
        localTubeCount     = new HashMap<>();
        localTipCount      = new HashMap<>();

    }

    public double run(Semiprotocol protocol) throws Exception {

        // Initialize values
        localTipCount.put(Tip.P20, 0);
        localTipCount.put(Tip.P200, 0);
        localTipCount.put(Tip.P1000, 0);
        localTubeCount.put(Container.eppendorf_1p5mL, 0);
        localTubeCount.put(Container.eppendorf_2mL, 0);
        localTubeCount.put(Container.pcr_plate_96, 0);
        localTubeCount.put(Container.pcr_strip, 0);
        localTubeCount.put(Container.pcr_tube, 0);

        for (Task task : protocol.getSteps()) {
            LabOp op = task.getOperation();

            switch (op) {
                case addContainer:
                    AddContainer curAdd = (AddContainer) task;

                    if (curAdd.isNew()){
                        Container curTube = curAdd.getTubetype();
                        if (localTubeCount.containsKey(curTube)){
                            int curCount = localTubeCount.get(curTube);
                            localTubeCount.put(curAdd.getTubetype(), curCount + 1);
                        } else {
                            localTubeCount.put(curTube, 1);
                        }
                    }
                    break;

                case removeContainer:
                    break;

                case transfer:
                    Transfer curTransfer = (Transfer) task;
                    double curTransVol = curTransfer.getVolume();

                    if (curTransVol < 20 ){
                        localTipCount.put(Tip.P20, localTipCount.get(Tip.P20) + 1);
                    } else if (curTransVol < 200) {
                        localTipCount.put(Tip.P200, localTipCount.get(Tip.P200) + 1);
                    } else if (curTransVol < 1000) {
                        localTipCount.put(Tip.P1000, localTipCount.get(Tip.P1000) + 1);
                    } else {
                        throw new IllegalArgumentException("There are no tips for the current volume.");
                    }
                    break;

                case dispense:
                    // Cast task to dispense, identify reagent and volume
                    Dispense curDisp = (Dispense) task;
                    Reagent curReag = curDisp.getReagent();
                    double curReagVol = curDisp.getVolume();

                    // Add tip for dispense task to tip list
                    if (curDisp.getVolume() < 20 ){
                        localTipCount.put(Tip.P20, localTipCount.get(Tip.P20) + 1);
                    } else if (curDisp.getVolume() < 200) {
                        localTipCount.put(Tip.P200, localTipCount.get(Tip.P200) + 1);
                    } else if (curDisp.getVolume() < 1000) {
                        localTipCount.put(Tip.P1000, localTipCount.get(Tip.P1000) + 1);
                    } else {
                        System.out.println("Volumes greater than 1 mL still need to be handled.");
                    }

                    // Add reagent to reagent list
                    if (localReagentCount.containsKey(curDisp.getReagent())){
                        localReagentCount.put(curReag, localReagentCount.get(curReag) + curReagVol);
                    } else {
                        localReagentCount.put(curDisp.getReagent(), curReagVol);
                    }
            }
        }

        return cost.run(localReagentCount, localTubeCount, localTipCount);
    }
    
    public static void main(String[] args) throws Exception {
        //Read in the example semiprotocol
        String text = FileUtils.readResourceFile("semiprotocol/data/alibaba_semiprotocol.txt");
        ParseSemiprotocol parser = new ParseSemiprotocol();
        parser.initiate();
        Semiprotocol protocol = parser.run(text);
        
        SemiprotocolPriceSimulator sim = new SemiprotocolPriceSimulator();
        sim.initiate();
        double price = sim.run(protocol);
        System.out.println("$" + price);
    }
}
