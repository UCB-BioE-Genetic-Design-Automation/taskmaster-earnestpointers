package org.ucb.bio134.taskmaster;

import java.util.HashMap;
import java.util.Map;
import org.ucb.bio134.taskmaster.model.Tip;
import org.ucb.c5.semiprotocol.model.Container;
import org.ucb.c5.semiprotocol.model.Reagent;

/**
 *
 * @author J. Christopher Anderson
 */
public class PriceCalculator {

    public void initiate() throws Exception {
        //TODO:  Write me
        
        /*JCA I recommend putting TSV files for price of goods in
            org.ucb.c5.semiprotocol.data
          And then parsing it here
        */
    }

    public double run(Map<Reagent, Integer> reagentCount,
                      Map<Container, Integer> tubeCount,
                      Map<Tip, Integer> tipCount) throws Exception {
        
        //TODO:  Write me
        
        return 0.0;
    }
    
    public static void main(String[] args) throws Exception {
        PriceCalculator calc = new PriceCalculator();
        calc.initiate();
        
        //Construct some example data
        Map<Reagent, Integer> reagentCount = new HashMap<>();
        reagentCount.put(Reagent.BsaI, 32);
        reagentCount.put(Reagent.T4_DNA_Ligase_Buffer_10x, 32);
        reagentCount.put(Reagent.T4_DNA_ligase, 16);
        
        Map<Container, Integer> tubeCount = new HashMap<>();
        tubeCount.put(Container.pcr_strip, 3);
        tubeCount.put(Container.pcr_tube, 8);
        tubeCount.put(Container.pcr_plate_96, 1);
        tubeCount.put(Container.eppendorf_1p5mL, 3);
        tubeCount.put(Container.eppendorf_2mL, 2);
        
        Map<Tip, Integer> tipCount = new HashMap<>();
        tipCount.put(Tip.P20, 86);
        tipCount.put(Tip.P200, 23);
        tipCount.put(Tip.P1000, 6);
        
        double price = calc.run(reagentCount, tubeCount, tipCount);
        System.out.println("$" + price);
    }
}
